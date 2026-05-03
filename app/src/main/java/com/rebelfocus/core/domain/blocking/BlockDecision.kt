package com.rebelfocus.core.domain.blocking

/**
 * Result of evaluating whether a foreground app should be blocked.
 */
sealed class BlockDecision {
    /** The app is blocked — overlay should be shown. */
    data class Block(val packageName: String) : BlockDecision()
    /** The app is allowed — overlay should be dismissed. */
    data object Allow : BlockDecision()
}
