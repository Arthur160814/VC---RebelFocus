package com.rebelfocus.core.domain.session

import android.os.SystemClock
import javax.inject.Inject

/**
 * Production [TimeSource] backed by real Android clocks.
 */
class SystemTimeSource @Inject constructor() : TimeSource {

    override fun elapsedRealtime(): Long = SystemClock.elapsedRealtime()

    override fun currentTimeMillis(): Long = System.currentTimeMillis()
}
