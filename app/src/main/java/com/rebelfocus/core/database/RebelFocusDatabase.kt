package com.rebelfocus.core.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.rebelfocus.core.database.dao.AuditEventDao
import com.rebelfocus.core.database.dao.AutomationRuleDao
import com.rebelfocus.core.database.dao.BlockedAppDao
import com.rebelfocus.core.database.dao.FocusProfileDao
import com.rebelfocus.core.database.dao.SessionDao
import com.rebelfocus.core.database.dao.SyncStateDao
import com.rebelfocus.core.database.dao.UserProgressDao
import com.rebelfocus.core.database.dao.DiagnosticLogDao
import com.rebelfocus.core.database.dao.RestoreDao
import com.rebelfocus.core.database.dao.ScheduledTriggerDao
import com.rebelfocus.core.database.entity.AuditEventEntity
import com.rebelfocus.core.database.entity.AutomationRuleEntity
import com.rebelfocus.core.database.entity.BlockedAppEntity
import com.rebelfocus.core.database.entity.FocusProfileEntity
import com.rebelfocus.core.database.entity.ProfileBlockedAppCrossRef
import com.rebelfocus.core.database.entity.SessionEntity
import com.rebelfocus.core.database.entity.SyncStateEntity
import com.rebelfocus.core.database.entity.UserProgressEntity
import com.rebelfocus.core.database.entity.ScheduledTriggerEntity
import com.rebelfocus.core.database.entity.DiagnosticLogEntity

/**
 * The single Room database for Rebel Focus.
 */
@Database(
    entities = [
        FocusProfileEntity::class,
        BlockedAppEntity::class,
        ProfileBlockedAppCrossRef::class,
        SessionEntity::class,
        AutomationRuleEntity::class,
        AuditEventEntity::class,
        SyncStateEntity::class,
        ScheduledTriggerEntity::class,
        DiagnosticLogEntity::class,
        UserProgressEntity::class
    ],
    version = 5,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class RebelFocusDatabase : RoomDatabase() {

    abstract fun sessionDao(): SessionDao
    abstract fun blockedAppDao(): BlockedAppDao
    abstract fun focusProfileDao(): FocusProfileDao
    abstract fun automationRuleDao(): AutomationRuleDao
    abstract fun userProgressDao(): UserProgressDao
    abstract fun auditEventDao(): AuditEventDao
    abstract fun syncStateDao(): SyncStateDao
    abstract fun scheduledTriggerDao(): ScheduledTriggerDao
    abstract fun restoreDao(): RestoreDao
    abstract fun diagnosticLogDao(): DiagnosticLogDao

    companion object {
        const val DATABASE_NAME = "rebel_focus.db"

        val MIGRATION_2_3 = object : androidx.room.migration.Migration(2, 3) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                // 1. Add timestamps to blocked_apps
                db.execSQL("ALTER TABLE blocked_apps ADD COLUMN created_at INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE blocked_apps ADD COLUMN updated_at INTEGER NOT NULL DEFAULT 0")
                
                // Set default to current time for existing apps (approximate)
                val now = System.currentTimeMillis()
                db.execSQL("UPDATE blocked_apps SET created_at = $now, updated_at = $now")

                // 2. Create diagnostic_logs table
                db.execSQL("""
                    CREATE TABLE IF NOT EXISTS diagnostic_logs (
                        id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,
                        timestamp INTEGER NOT NULL,
                        level TEXT NOT NULL,
                        component TEXT NOT NULL,
                        message TEXT NOT NULL,
                        extraData TEXT
                    )
                """.trimIndent())
            }
        }

        val MIGRATION_3_4 = object : androidx.room.migration.Migration(3, 4) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE focus_profiles ADD COLUMN is_extreme_mode INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE sessions ADD COLUMN is_extreme_mode INTEGER NOT NULL DEFAULT 0")
            }
        }
        val MIGRATION_4_5 = object : androidx.room.migration.Migration(4, 5) {
            override fun migrate(db: androidx.sqlite.db.SupportSQLiteDatabase) {
                db.execSQL("ALTER TABLE focus_profiles ADD COLUMN is_ultimate_mode INTEGER NOT NULL DEFAULT 0")
                db.execSQL("ALTER TABLE sessions ADD COLUMN is_ultimate_mode INTEGER NOT NULL DEFAULT 0")
            }
        }
    }
}
