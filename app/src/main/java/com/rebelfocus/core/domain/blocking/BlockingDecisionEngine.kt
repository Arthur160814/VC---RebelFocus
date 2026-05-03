package com.rebelfocus.core.domain.blocking

import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.model.SessionState
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Evaluates whether a given foreground package should be blocked.
 *
 * Decision logic:
 * 1. No active session → Allow
 * 2. Session not in ActiveFocus → Allow (breaks and paused are unblocked)
 * 3. Package is whitelisted (our app, system UI, settings) → Allow
 * 4. Session has no profileId → Allow (no blocking rules)
 * 5. Package is in the profile's blocked-app list → Block
 * 6. Otherwise → Allow
 *
 * Maintains an in-memory cache of blocked packages per session to avoid
 * database queries on every window-change event.
 */
@Singleton
class BlockingDecisionEngine @Inject constructor(
    private val sessionRepository: SessionRepository,
    private val profileRepository: FocusProfileRepository
) {
    companion object {
        private val WHITELISTED = setOf(
            "com.rebelfocus",
            "com.android.systemui",
            "com.android.settings"
        )
    }

    // ── Cache ─────────────────────────────────────────
    private var cachedSessionId: String? = null
    private var cachedBlockedPackages: Set<String> = emptySet()
    
    /** The package name of the device's default launcher. Set by the service. */
    var launcherPackage: String? = null

    suspend fun evaluate(packageName: String): BlockDecision {
        // 1. Mandatory Exemptions (Self-exclusion and System UI)
        // In Extreme Mode, we even block Rebel Focus itself once the session is active.
        if (packageName == "com.android.systemui") {
            return BlockDecision.Allow
        }

        // 2. Active session check
        val session = sessionRepository.getActiveSession()
            ?: run {
                return BlockDecision.Allow
            }

        val isExtreme = session.isExtremeMode

        // In Extreme Mode, we also exempt our own package ONLY if the session state is not active yet?
        // Actually, once session is active, even Rebel Focus is blocked.
        if (!isExtreme && packageName == "com.rebelfocus") {
            return BlockDecision.Allow
        }

        // 3. Only block during ActiveFocus or Break (Extreme Mode covers full flow)
        val isEnforceableState = session.state == SessionState.ActiveFocus || 
                               (isExtreme && session.state == SessionState.Break)

        if (!isEnforceableState) {
            return BlockDecision.Allow
        }

        // 4. Decision based on mode
        return if (isExtreme) {
            // Extreme Mode: Any external app AND Rebel Focus itself are blocked.
            // (System UI remains exempt above)
            BlockDecision.Block(packageName)
        } else {
            // Normal Mode: Decide based on whitelist and user-selected blocked apps.
            if (packageName in WHITELISTED) {
                return BlockDecision.Allow
            }

            // Refresh cache if session changed
            if (session.id != cachedSessionId) {
                cachedSessionId = session.id
                cachedBlockedPackages = loadBlockedPackages(session.profileId)
            }

            if (packageName in cachedBlockedPackages) {
                BlockDecision.Block(packageName)
            } else {
                BlockDecision.Allow
            }
        }
    }

    private suspend fun loadBlockedPackages(profileId: String?): Set<String> {
        if (profileId == null) return emptySet()
        val profile = profileRepository.getById(profileId) ?: return emptySet()
        return profile.blockedApps.map { it.packageName }.toSet()
    }

    /** Call when the session or profile changes to force a cache refresh. */
    fun invalidateCache() {
        cachedSessionId = null
        cachedBlockedPackages = emptySet()
    }
}
