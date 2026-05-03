package com.rebelfocus.core.model

/**
 * Domain model for an app that can be blocked during focus sessions.
 */
data class BlockedApp(
    /** Android package name — serves as the unique identifier. */
    val packageName: String,
    /** Human-readable app name for display. */
    val appName: String,
    /** Whether this app is currently enabled for blocking. */
    val isEnabled: Boolean,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
