package com.rebelfocus.services.scheduling

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.rebelfocus.core.domain.usecase.ExecuteScheduleUseCase
import com.rebelfocus.core.domain.usecase.ExecuteScheduledTriggerUseCase
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Thin BroadcastReceiver that dispatches alarm firings to domain use cases.
 *
 * Supports two intent shapes:
 *  - EXTRA_TRIGGER_ID → calendar-derived or new-style scheduled trigger
 *  - EXTRA_RULE_ID (legacy) → Phase 6 manual schedule rules
 */
@AndroidEntryPoint
class ScheduleAlarmReceiver : BroadcastReceiver() {

    companion object {
        const val EXTRA_TRIGGER_ID = "extra_trigger_id"
        const val EXTRA_RULE_ID = "extra_rule_id"
    }

    @Inject
    lateinit var executeScheduleUseCase: ExecuteScheduleUseCase

    @Inject
    lateinit var executeScheduledTriggerUseCase: ExecuteScheduledTriggerUseCase

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    override fun onReceive(context: Context, intent: Intent) {
        val triggerId = intent.getStringExtra(EXTRA_TRIGGER_ID)
        val ruleId = intent.getStringExtra(EXTRA_RULE_ID)

        val pendingResult = goAsync()
        scope.launch {
            try {
                when {
                    // New-style: materialized trigger identity (calendar or future triggers)
                    triggerId != null -> executeScheduledTriggerUseCase(triggerId)
                    // Legacy: Phase 6 manual schedule rules
                    ruleId != null -> executeScheduleUseCase(ruleId)
                }
            } finally {
                pendingResult.finish()
            }
        }
    }
}
