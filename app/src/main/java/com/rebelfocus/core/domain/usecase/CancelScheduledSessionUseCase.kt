package com.rebelfocus.core.domain.usecase

import com.rebelfocus.core.domain.scheduling.AlarmScheduler
import javax.inject.Inject

class CancelScheduledSessionUseCase @Inject constructor(
    private val scheduler: AlarmScheduler
) {
    operator fun invoke(ruleId: String) {
        // For manual rules, the requestCode is always ruleId.hashCode()
        scheduler.cancel(ruleId, ruleId.hashCode())
    }
}
