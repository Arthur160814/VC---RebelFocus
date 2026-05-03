package com.rebelfocus.core.model

/**
 * Types of events recorded in the audit trail.
 */
enum class AuditEventType {
    SessionStarted,
    SessionPaused,
    SessionResumed,
    SessionCompleted,
    SessionCancelled,
    SessionEmergencyExit,
    BlockedAppDetected,
    OverlayShown,
    OverlayDismissed,
    PermissionMissing,
    PermissionGranted,
    PermissionRevoked,
    ScheduledTriggerFired,
    CalendarTriggerMatched,
    SyncSuccess,
    SyncFailure,
    AppLaunched,
    ServiceStarted,
    ServiceStopped
}
