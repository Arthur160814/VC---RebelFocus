package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.model.ScheduleType
import com.rebelfocus.core.model.SessionType
import javax.inject.Inject

class ExecuteScheduleUseCase @Inject constructor(
    private val ruleRepository: AutomationRuleRepository,
    private val profileRepository: FocusProfileRepository,
    private val startFocusSessionUseCase: StartFocusSessionUseCase,
    private val scheduleSessionUseCase: ScheduleSessionUseCase
) {
    suspend operator fun invoke(ruleId: String) {
        val rule = ruleRepository.getById(ruleId) ?: return
        if (!rule.isEnabled) return

        val profile = profileRepository.getById(rule.profileId) ?: return

        // 1. Start the session
        startFocusSessionUseCase(
            profileId = profile.id,
            type = profile.sessionType,
            focusDurationMillis = profile.focusDurationMillis,
            breakDurationMillis = profile.breakDurationMillis,
            pomodoroTarget = profile.pomodoroTarget
        )

        // 2. Handle recurring logic
        if (rule.scheduleType == ScheduleType.RecurringWeekday) {
            // Re-schedule for the next occurrence
            scheduleSessionUseCase(rule)
        } else if (rule.scheduleType == ScheduleType.OneTime) {
            // Disable or delete the one-time rule since it has fired
            ruleRepository.update(rule.copy(isEnabled = false))
        }
    }
}
