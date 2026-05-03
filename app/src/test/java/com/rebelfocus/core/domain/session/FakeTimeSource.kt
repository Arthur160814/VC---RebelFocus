package com.rebelfocus.core.domain.session

/**
 * A controllable [TimeSource] for deterministic unit tests.
 *
 * Time does not advance automatically — call [advanceBy] or set [elapsedRealtimeMillis]
 * and [currentTimeMillisValue] directly.
 */
class FakeTimeSource(
    var elapsedRealtimeMillis: Long = 0L,
    var currentTimeMillisValue: Long = 1_700_000_000_000L // some arbitrary epoch
) : TimeSource {

    override fun elapsedRealtime(): Long = elapsedRealtimeMillis

    override fun currentTimeMillis(): Long = currentTimeMillisValue

    fun advanceBy(millis: Long) {
        elapsedRealtimeMillis += millis
        currentTimeMillisValue += millis
    }
}
