package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.data.repository.ScheduledTriggerRepository
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.domain.logging.DiagnosticLogger
import com.rebelfocus.core.model.ScheduledTriggerStatus
import com.rebelfocus.core.model.SessionState
import javax.inject.Inject

/**
 * Validates and executes a materialized ScheduledTrigger.
 *
 * Pre-execution checks (per architecture requirements):
 *  1. Trigger is still PENDING
 *  2. Source rule is still enabled
 *  3. Target profile still exists
 *  4. Trigger is not obsolete (>30 min past)
 *  5. No incompatible already-active session
 *
 * After execution, the trigger is updated to the correct terminal state:
 * FIRED, FAILED, CANCELLED, or OBSOLETE.
 */
class ExecuteScheduledTriggerUseCase @Inject constructor(
    private val triggerRepository: ScheduledTriggerRepository,
    private val ruleRepository: AutomationRuleRepository,
    private val profileRepository: FocusProfileRepository,
    private val sessionRepository: SessionRepository,
    private val startFocusSessionUseCase: StartFocusSessionUseCase,
    private val logger: DiagnosticLogger
) {
    suspend operator fun invoke(triggerId: String) {
        val trigger = triggerRepository.getById(triggerId) ?: run {
            logger.warn("SCHEDULING", "Trigger not found: $triggerId")
            return
        }
        val nowMillis = System.currentTimeMillis()

        // 1. Trigger must be PENDING
        if (trigger.status != ScheduledTriggerStatus.PENDING) {
            logger.warn("SCHEDULING", "Trigger $triggerId is not PENDING (Status: ${trigger.status})")
            return
        }

        // 2. Source rule must still be enabled
        val rule = ruleRepository.getById(trigger.sourceRuleId)
        if (rule == null || !rule.isEnabled) {
            logger.info("SCHEDULING", "Rule not found or disabled, cancelling trigger: $triggerId")
            triggerRepository.update(trigger.copy(
                status = ScheduledTriggerStatus.CANCELLED,
                updatedAt = nowMillis
            ))
            return
        }

        // 3. Target profile must still exist
        val profile = profileRepository.getById(trigger.profileId)
        if (profile == null) {
            logger.error("SCHEDULING", "Profile not found for trigger: $triggerId")
            triggerRepository.update(trigger.copy(
                status = ScheduledTriggerStatus.CANCELLED,
                updatedAt = nowMillis
            ))
            return
        }

        // 4. Not obsolete (>30 minutes past the scheduled time)
        val obsolescenceThresholdMillis = 30 * 60 * 1000L
        if (nowMillis - trigger.triggerAtMillis > obsolescenceThresholdMillis) {
            logger.warn("SCHEDULING", "Trigger $triggerId is obsolete")
            triggerRepository.update(trigger.copy(
                status = ScheduledTriggerStatus.OBSOLETE,
                updatedAt = nowMillis
            ))
            return
        }

        // 5. No incompatible already-active session
        val activeSession = sessionRepository.getActiveSession()
        if (activeSession != null) {
            // If there is an active focus or break session, we cannot override it
            val incompatibleStates = setOf(
                SessionState.ActiveFocus,
                SessionState.Break,
                SessionState.Paused
            )
            if (activeSession.state in incompatibleStates) {
                triggerRepository.update(trigger.copy(
                    status = ScheduledTriggerStatus.FAILED,
                    updatedAt = nowMillis
                ))
                return
            }
        }

        // Execute: start the focus session
        try {
            startFocusSessionUseCase(
                profileId = profile.id,
                type = profile.sessionType,
                focusDurationMillis = profile.focusDurationMillis,
                breakDurationMillis = profile.breakDurationMillis,
                pomodoroTarget = profile.pomodoroTarget
            )

            triggerRepository.update(trigger.copy(
                status = ScheduledTriggerStatus.FIRED,
                updatedAt = nowMillis
            ))
        } catch (e: Exception) {
            e.printStackTrace()
            triggerRepository.update(trigger.copy(
                status = ScheduledTriggerStatus.FAILED,
                updatedAt = nowMillis
            ))
        }
    }
}
