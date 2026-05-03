package com.rebelfocus.services.accessibility

import android.accessibilityservice.AccessibilityService
import android.content.Intent
import android.view.accessibility.AccessibilityEvent
import com.rebelfocus.core.data.repository.AuditEventRepository
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.domain.blocking.BlockDecision
import com.rebelfocus.core.domain.blocking.BlockingDecisionEngine
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase
import com.rebelfocus.core.model.AuditEvent
import com.rebelfocus.core.model.AuditEventType
import com.rebelfocus.services.overlay.OverlayController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.firstOrNull
import java.util.UUID
import javax.inject.Inject

@AndroidEntryPoint
class RebelFocusAccessibilityService : AccessibilityService() {

    @Inject lateinit var blockingDecisionEngine: BlockingDecisionEngine
    @Inject lateinit var overlayController: OverlayController
    @Inject lateinit var auditEventRepository: AuditEventRepository
    @Inject lateinit var sessionRepository: SessionRepository
    @Inject lateinit var stopFocusSessionUseCase: StopFocusSessionUseCase
    @Inject lateinit var diagnosticLogDao: com.rebelfocus.core.database.dao.DiagnosticLogDao

    private val job = SupervisorJob()
    private val scope = CoroutineScope(Dispatchers.Main + job)
    
    private var lastForegroundPackage: String? = null
    private var enforcedPackage: String? = null

    override fun onServiceConnected() {
        super.onServiceConnected()
        
        // CRITICAL: Provide the service context (with window token) to the overlay controller
        overlayController.updateContext(this)
        
        scope.launch {
            logAuditEvent(AuditEventType.ServiceStarted, details = "AccessibilityService connected")
            // Ensure cache is clear when service starts
            blockingDecisionEngine.invalidateCache()
            
            // Start observing session state for immediate Extreme Mode overlay
            observeSessionState()
            
            // Resolve default launcher
            try {
                val intent = android.content.Intent(android.content.Intent.ACTION_MAIN).apply {
                    addCategory(android.content.Intent.CATEGORY_HOME)
                }
                val resolveInfo = packageManager.resolveActivity(intent, android.content.pm.PackageManager.MATCH_DEFAULT_ONLY)
                blockingDecisionEngine.launcherPackage = resolveInfo?.activityInfo?.packageName
            } catch (e: Exception) {
                // Fallback to a common launcher package if resolution fails
                blockingDecisionEngine.launcherPackage = "com.google.android.apps.nexuslauncher"
            }
        }
    }

    override fun onAccessibilityEvent(event: AccessibilityEvent?) {
        if (event == null || event.eventType != AccessibilityEvent.TYPE_WINDOW_STATE_CHANGED) return

        val packageName = event.packageName?.toString() ?: return
        
        scope.launch {
            val session = sessionRepository.getActiveSession()
            val isExtreme = session?.isExtremeMode == true
            
            // Bypass debounce if session is in Extreme Mode to ensure immediate re-blocking
            if (!isExtreme && packageName == lastForegroundPackage) return@launch
            lastForegroundPackage = packageName

            val decision = blockingDecisionEngine.evaluate(packageName)
            
            println("EXTREME_DEBUG: [Service] Decision for $packageName: ${decision::class.simpleName}")

            when (decision) {
                is BlockDecision.Block -> {
                    enforcedPackage = packageName
                    println("EXTREME_DEBUG: [Service] REQUESTING OVERLAY for $packageName")
                    if (isExtreme) {
                        diagnosticLogDao.insert(com.rebelfocus.core.database.entity.DiagnosticLogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "WARN",
                            component = "ExtremeFocus",
                            message = "Access attempt blocked while in Extreme Mode",
                            extraData = "package: $packageName, session: ${session?.id}"
                        ))
                    }
                    logAuditEvent(AuditEventType.BlockedAppDetected, packageName = packageName)
                    overlayController.showOverlay(
                        packageName = packageName,
                        onEmergencyExit = { handleEmergencyExit(isExtreme) }
                    )
                    logAuditEvent(AuditEventType.OverlayShown, packageName = packageName)
                }
                is BlockDecision.Allow -> {
                    // CRITICAL FIX: Do not dismiss the overlay if the 'Allow' is for our own package
                    // while we are in the middle of enforcing a block on an external package.
                    if (packageName == "com.rebelfocus" && enforcedPackage != null) {
                        println("EXTREME_DEBUG: [Service] Ignoring Allow for com.rebelfocus - Enforcement active for $enforcedPackage")
                        return@launch
                    }
                    
                    // NEW: Do not dismiss if we are showing the Completion Summary
                    val currentSession = sessionRepository.observeEnforcementSession().firstOrNull()
                    if (currentSession?.state == com.rebelfocus.core.model.SessionState.Completed) {
                        println("EXTREME_DEBUG: [Service] Ignoring Allow - Completion Summary active")
                        return@launch
                    }
                    
                    enforcedPackage = null
                    overlayController.hideOverlay(reason = "Allow decision for $packageName")
                }
            }
        }
    }

    private fun observeSessionState() {
        scope.launch {
            sessionRepository.observeEnforcementSession().collect { session ->
                if (session != null && session.isExtremeMode) {
                    val isEnforceable = session.state == com.rebelfocus.core.model.SessionState.ActiveFocus || 
                                       session.state == com.rebelfocus.core.model.SessionState.Break
                    
                    if (isEnforceable) {
                        println("EXTREME_DEBUG: [Service] Session-driven overlay trigger for ${session.state}")
                        enforcedPackage = "com.rebelfocus" // Representing internal/general block
                        overlayController.showOverlay(
                            packageName = "com.rebelfocus",
                            onEmergencyExit = { handleEmergencyExit(isExtreme = true) }
                        )
                    } else if (session.state == com.rebelfocus.core.model.SessionState.Completed) {
                        // CRITICAL FIX: Do NOT hide the overlay if it just transitioned to Completed.
                        // Let the overlay content show the "Well Done" screen.
                        println("EXTREME_DEBUG: [Service] Session COMPLETED. Keeping overlay for summary.")
                        enforcedPackage = "com.rebelfocus.summary"
                    } else {
                        // If session is paused or cancelled, hide if it was an extreme block
                        if (enforcedPackage != null) {
                            enforcedPackage = null
                            overlayController.hideOverlay(reason = "Session state changed to ${session.state}")
                        }
                    }
                } else if (session == null && enforcedPackage != null) {
                    // Session ended
                    enforcedPackage = null
                    overlayController.hideOverlay(reason = "No active session (session-driven)")
                }
            }
        }
    }

    private fun handleEmergencyExit(isExtreme: Boolean = false) {
        scope.launch {
            val session = sessionRepository.getActiveSession()
            println("EXTREME_DEBUG: [Service] handleEmergencyExit - session: ${session?.id}, state: ${session?.state}")
            
            if (session != null) {
                if (session.state != com.rebelfocus.core.model.SessionState.Completed) {
                    if (isExtreme) {
                        diagnosticLogDao.insert(com.rebelfocus.core.database.entity.DiagnosticLogEntity(
                            timestamp = System.currentTimeMillis(),
                            level = "CRITICAL",
                            component = "ExtremeFocus",
                            message = "Emergency Exit performed under Extreme Mode enforcement",
                            extraData = "sessionId: ${session.id}"
                        ))
                    }
                    stopFocusSessionUseCase(session.id, reason = com.rebelfocus.core.domain.usecase.StopReason.EmergencyExit)
                    logAuditEvent(AuditEventType.SessionEmergencyExit, sessionId = session.id)
                } else {
                    println("EXTREME_DEBUG: [Service] Session already completed. Just dismissing overlay.")
                }
            }
            enforcedPackage = null
            overlayController.hideOverlay(reason = "User dismissed (Exit or Done)")
            logAuditEvent(AuditEventType.OverlayDismissed, details = "User dismissed")
            blockingDecisionEngine.invalidateCache()
        }
    }

    override fun onInterrupt() {
        overlayController.hideOverlay(reason = "Service Interrupt")
    }

    override fun onUnbind(intent: Intent?): Boolean {
        scope.launch {
            logAuditEvent(AuditEventType.ServiceStopped, details = "AccessibilityService unbound")
        }
        job.cancel()
        overlayController.hideOverlay(reason = "Service Unbound")
        return super.onUnbind(intent)
    }

    private suspend fun logAuditEvent(
        type: AuditEventType,
        sessionId: String? = null,
        packageName: String? = null,
        details: String? = null
    ) {
        val event = AuditEvent(
            id = UUID.randomUUID().toString(),
            eventType = type,
            sessionId = sessionId,
            packageName = packageName,
            details = details,
            timestamp = System.currentTimeMillis()
        )
        auditEventRepository.insert(event)
    }
}
