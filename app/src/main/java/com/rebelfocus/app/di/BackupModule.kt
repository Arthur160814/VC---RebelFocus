package com.rebelfocus.app.di

import com.rebelfocus.core.data.backup.LocalBackupProvider
import com.rebelfocus.core.domain.backup.BackupProvider
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class BackupModule {

    @Binds
    abstract fun bindBackupProvider(
        localBackupProvider: LocalBackupProvider
    ): BackupProvider
}
