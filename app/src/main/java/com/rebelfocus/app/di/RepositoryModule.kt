package com.rebelfocus.app.di

import com.rebelfocus.core.data.repository.AuditEventRepository
import com.rebelfocus.core.data.repository.AutomationRuleRepository
import com.rebelfocus.core.data.repository.BlockedAppRepository
import com.rebelfocus.core.data.repository.FocusProfileRepository
import com.rebelfocus.core.data.repository.SessionRepository
import com.rebelfocus.core.data.repository.SyncStateRepository
import com.rebelfocus.core.data.repository.UserProgressRepository
import com.rebelfocus.core.data.repository.impl.AuditEventRepositoryImpl
import com.rebelfocus.core.data.repository.impl.AutomationRuleRepositoryImpl
import com.rebelfocus.core.data.repository.impl.BlockedAppRepositoryImpl
import com.rebelfocus.core.data.repository.impl.FocusProfileRepositoryImpl
import com.rebelfocus.core.data.repository.impl.SessionRepositoryImpl
import com.rebelfocus.core.data.repository.impl.SyncStateRepositoryImpl
import com.rebelfocus.core.data.repository.impl.UserProgressRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindSessionRepository(impl: SessionRepositoryImpl): SessionRepository

    @Binds
    @Singleton
    abstract fun bindBlockedAppRepository(impl: BlockedAppRepositoryImpl): BlockedAppRepository

    @Binds
    @Singleton
    abstract fun bindFocusProfileRepository(impl: FocusProfileRepositoryImpl): FocusProfileRepository

    @Binds
    @Singleton
    abstract fun bindAutomationRuleRepository(impl: AutomationRuleRepositoryImpl): AutomationRuleRepository

    @Binds
    @Singleton
    abstract fun bindUserProgressRepository(impl: UserProgressRepositoryImpl): UserProgressRepository

    @Binds
    @Singleton
    abstract fun bindAuditEventRepository(impl: AuditEventRepositoryImpl): AuditEventRepository

    @Binds
    @Singleton
    abstract fun bindSyncStateRepository(impl: SyncStateRepositoryImpl): SyncStateRepository

    @Binds
    @Singleton
    abstract fun bindScheduledTriggerRepository(impl: com.rebelfocus.core.data.repository.ScheduledTriggerRepositoryImpl): com.rebelfocus.core.data.repository.ScheduledTriggerRepository
}
