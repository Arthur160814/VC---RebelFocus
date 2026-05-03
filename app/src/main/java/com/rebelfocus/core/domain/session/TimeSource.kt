package com.rebelfocus.core.domain.session

/**
 * Abstraction over time sources to make the session engine fully testable.
 *
 * Production implementation uses [android.os.SystemClock.elapsedRealtime]
 * for monotonic timing and [System.currentTimeMillis] for wall-clock timestamps.
 */
interface TimeSource {
    /**
     * Returns milliseconds since boot (monotonic, not affected by wall-clock changes).
     * This is the source of truth for all session timing math.
     */
    fun elapsedRealtime(): Long

    /**
     * Returns the current wall-clock time in milliseconds (UTC).
     * Used only for [com.rebelfocus.core.model.Session.createdAt],
     * [com.rebelfocus.core.model.Session.updatedAt], and audit event timestamps.
     */
    fun currentTimeMillis(): Long
}
