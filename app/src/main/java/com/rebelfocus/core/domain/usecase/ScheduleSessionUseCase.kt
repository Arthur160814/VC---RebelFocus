package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.domain.scheduling.AlarmScheduler
import com.rebelfocus.core.domain.scheduling.NextOccurrenceCalculator
import com.rebelfocus.core.model.AutomationRule
import javax.inject.Inject

class ScheduleSessionUseCase @Inject constructor(
    private val calculator: NextOccurrenceCalculator,
    private val scheduler: AlarmScheduler
) {
    operator fun invoke(rule: AutomationRule) {
        val nextMillis = calculator.calculateNextOccurrence(rule)
        // For manual (non-calendar) rules, use the ruleId as the trigger identity
        // and ruleId.hashCode() as the request code for PendingIntent uniqueness.
        val requestCode = rule.id.hashCode()
        if (nextMillis != null) {
            scheduler.scheduleExact(rule.id, nextMillis, requestCode)
        } else {
            scheduler.cancel(rule.id, requestCode)
        }
    }
}
