package com.rebelfocus.core.model

/**
 * Domain model for an audit trail event.
 */
data class AuditEvent(
    val id: String,
    val eventType: AuditEventType,
    /** Associated session ID, if applicable. */
    val sessionId: String?,
    /** Associated package name, if applicable. */
    val packageName: String?,
    /** Free-form detail text for diagnostics. */
    val details: String?,
    /** Wall-clock timestamp of the event. */
    val timestamp: Long
)
