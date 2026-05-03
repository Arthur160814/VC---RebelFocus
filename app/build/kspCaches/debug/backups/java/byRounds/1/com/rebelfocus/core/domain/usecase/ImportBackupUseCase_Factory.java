package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.database.dao.RestoreDao;
import com.rebelfocus.core.domain.backup.BackupProvider;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata
@DaggerGenerated
@Generated(
    value = "dagger.internal.codegen.ComponentProcessor",
    comments = "https://dagger.dev"
)
@SuppressWarnings({
    "unchecked",
    "rawtypes",
    "KotlinInternal",
    "KotlinInternalInJava",
    "cast"
})
public final class ImportBackupUseCase_Factory implements Factory<ImportBackupUseCase> {
  private final Provider<RestoreDao> restoreDaoProvider;

  private final Provider<BackupProvider> backupProvider;

  public ImportBackupUseCase_Factory(Provider<RestoreDao> restoreDaoProvider,
      Provider<BackupProvider> backupProvider) {
    this.restoreDaoProvider = restoreDaoProvider;
    this.backupProvider = backupProvider;
  }

  @Override
  public ImportBackupUseCase get() {
    return newInstance(restoreDaoProvider.get(), backupProvider.get());
  }

  public static ImportBackupUseCase_Factory create(Provider<RestoreDao> restoreDaoProvider,
      Provider<BackupProvider> backupProvider) {
    return new ImportBackupUseCase_Factory(restoreDaoProvider, backupProvider);
  }

  public static ImportBackupUseCase newInstance(RestoreDao restoreDao,
      BackupProvider backupProvider) {
    return new ImportBackupUseCase(restoreDao, backupProvider);
  }
}
