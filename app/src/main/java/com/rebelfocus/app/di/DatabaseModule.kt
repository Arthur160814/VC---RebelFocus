package com.rebelfocus.app.di

import android.content.Context
import androidx.room.Room
import com.rebelfocus.core.database.RebelFocusDatabase
import com.rebelfocus.core.database.dao.AuditEventDao
import com.rebelfocus.core.database.dao.AutomationRuleDao
import com.rebelfocus.core.database.dao.BlockedAppDao
import com.rebelfocus.core.database.dao.FocusProfileDao
import com.rebelfocus.core.database.dao.SessionDao
import com.rebelfocus.core.database.dao.SyncStateDao
import com.rebelfocus.core.database.dao.UserProgressDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(
        @ApplicationContext context: Context
    ): RebelFocusDatabase {
        return Room.databaseBuilder(
            context,
            RebelFocusDatabase::class.java,
            RebelFocusDatabase.DATABASE_NAME
        )
            .addMigrations(
                RebelFocusDatabase.MIGRATION_2_3,
                RebelFocusDatabase.MIGRATION_3_4,
                RebelFocusDatabase.MIGRATION_4_5
            )
            .fallbackToDestructiveMigration()
            .build()
    }

    @Provides
    fun provideSessionDao(db: RebelFocusDatabase): SessionDao = db.sessionDao()

    @Provides
    fun provideBlockedAppDao(db: RebelFocusDatabase): BlockedAppDao = db.blockedAppDao()

    @Provides
    fun provideFocusProfileDao(db: RebelFocusDatabase): FocusProfileDao = db.focusProfileDao()

    @Provides
    fun provideAutomationRuleDao(db: RebelFocusDatabase): AutomationRuleDao = db.automationRuleDao()

    @Provides
    fun provideUserProgressDao(db: RebelFocusDatabase): UserProgressDao = db.userProgressDao()

    @Provides
    fun provideAuditEventDao(db: RebelFocusDatabase): AuditEventDao = db.auditEventDao()

    @Provides
    fun provideSyncStateDao(db: RebelFocusDatabase): SyncStateDao = db.syncStateDao()

    @Provides
    fun provideScheduledTriggerDao(db: RebelFocusDatabase): com.rebelfocus.core.database.dao.ScheduledTriggerDao = db.scheduledTriggerDao()

    @Provides
    fun provideRestoreDao(db: RebelFocusDatabase): com.rebelfocus.core.database.dao.RestoreDao = db.restoreDao()

    @Provides
    fun provideDiagnosticLogDao(db: RebelFocusDatabase): com.rebelfocus.core.database.dao.DiagnosticLogDao = db.diagnosticLogDao()
}
