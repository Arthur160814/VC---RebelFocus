package com.rebelfocus.services.foreground

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import com.rebelfocus.app.MainActivity
import com.rebelfocus.core.model.Session
import com.rebelfocus.core.model.SessionState
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NotificationHelper @Inject constructor(
    @ApplicationContext private val context: Context
) {
    companion object {
        const val SESSION_CHANNEL_ID = "focus_session_channel"
        const val SYSTEM_CHANNEL_ID = "system_reliability_channel"
        const val NOTIFICATION_ID = 1001

        const val ACTION_PAUSE = "com.rebelfocus.action.PAUSE"
        const val ACTION_RESUME = "com.rebelfocus.action.RESUME"
        const val ACTION_STOP = "com.rebelfocus.action.STOP"
        
        const val EXTRA_SESSION_ID = "extra_session_id"
    }

    private val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    init {
        createChannels()
    }

    fun createChannels() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val sessionChannel = NotificationChannel(
                SESSION_CHANNEL_ID,
                "Focus Sessions",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows the status of your active focus session"
                setShowBadge(false)
            }
            
            val systemChannel = NotificationChannel(
                SYSTEM_CHANNEL_ID,
                "System & Sync",
                NotificationManager.IMPORTANCE_MIN
            ).apply {
                description = "Used for background synchronization and system reliability tasks"
                setShowBadge(false)
            }

            notificationManager.createNotificationChannels(listOf(sessionChannel, systemChannel))
        }
    }

    fun buildNotification(session: Session): Notification {
        val openAppIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingOpenApp = PendingIntent.getActivity(
            context, 0, openAppIntent, PendingIntent.FLAG_IMMUTABLE
        )

        val remaining = if (session.state == SessionState.Break) {
            val breakElapsed = (android.os.SystemClock.elapsedRealtime() - (session.startTimeElapsedRealtime ?: android.os.SystemClock.elapsedRealtime())).coerceAtLeast(0)
            (session.breakDurationMillis - breakElapsed).coerceAtLeast(0)
        } else {
            val focusPerInterval = session.plannedDurationMillis
            val focusElapsed = session.elapsedAtPauseMillis + if (session.state == SessionState.ActiveFocus) {
                (android.os.SystemClock.elapsedRealtime() - (session.startTimeElapsedRealtime ?: android.os.SystemClock.elapsedRealtime())).coerceAtLeast(0)
            } else 0L
            
            val elapsedThisInterval = if (session.pomodoroTarget > 1) {
                focusElapsed - (session.pomodoroCount.toLong() * focusPerInterval)
            } else {
                focusElapsed
            }
            (focusPerInterval - elapsedThisInterval).coerceAtLeast(0)
        }
        val minutes = remaining / 60000
        val seconds = (remaining % 60000) / 1000
        val timer = "%02d:%02d".format(minutes, seconds)

        val title = when(session.state) {
            SessionState.ActiveFocus -> "Focusing - $timer"
            SessionState.Break -> "Break - $timer"
            SessionState.Paused -> "Paused - $timer"
            else -> "Session - $timer"
        }

        val builder = NotificationCompat.Builder(context, SESSION_CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .setContentTitle(title)
            .setContentText("Pomodoro: ${session.pomodoroCount}/${session.pomodoroTarget}")
            .setContentIntent(pendingOpenApp)
            .setOngoing(true)
            .setOnlyAlertOnce(true)

        // Add actions based on state
        when (session.state) {
            SessionState.ActiveFocus, SessionState.Break -> {
                builder.addAction(
                    android.R.drawable.ic_media_pause,
                    "Pause",
                    createActionIntent(ACTION_PAUSE, session.id)
                )
            }
            SessionState.Paused -> {
                builder.addAction(
                    android.R.drawable.ic_media_play,
                    "Resume",
                    createActionIntent(ACTION_RESUME, session.id)
                )
            }
            else -> {} // No pause/resume for Scheduled or terminal states
        }

        // Stop action is always available
        builder.addAction(
            android.R.drawable.ic_menu_close_clear_cancel,
            "Stop",
            createActionIntent(ACTION_STOP, session.id)
        )

        return builder.build()
    }

    private fun createActionIntent(action: String, sessionId: String): PendingIntent {
        val intent = Intent(context, ServiceActionReceiver::class.java).apply {
            this.action = action
            putExtra(EXTRA_SESSION_ID, sessionId)
        }
        return PendingIntent.getBroadcast(
            context,
            action.hashCode(),
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
    }

    fun updateNotification(session: Session) {
        notificationManager.notify(NOTIFICATION_ID, buildNotification(session))
    }
}
