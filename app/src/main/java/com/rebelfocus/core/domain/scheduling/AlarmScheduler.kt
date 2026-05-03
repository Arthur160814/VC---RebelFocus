package com.rebelfocus.core.domain.scheduling

/**
 * Interface abstracting the underlying system scheduling mechanism.
 */
interface AlarmScheduler {
    
    /**
     * Schedules a session to start at the exact specified time.
     * 
     * @param triggerId The deterministic identity of the scheduled trigger (e.g. ruleId or ruleId_eventId).
     * @param triggerAtMillis The exact epoch timestamp to trigger the session.
     * @param requestCode The unique integer hash identifying this pending intent.
     */
    fun scheduleExact(triggerId: String, triggerAtMillis: Long, requestCode: Int)

    /**
     * Cancels any pending schedule for the given trigger.
     * 
     * @param triggerId The deterministic identity of the scheduled trigger.
     * @param requestCode The unique integer hash identifying this pending intent.
     */
    fun cancel(triggerId: String, requestCode: Int)

    /**
     * Checks whether the application currently has the system capability
     * to schedule exact alarms.
     */
    fun canScheduleExactAlarms(): Boolean
}
