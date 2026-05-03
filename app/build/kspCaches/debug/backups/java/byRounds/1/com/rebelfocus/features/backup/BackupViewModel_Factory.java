package com.rebelfocus.features.backup;

import com.rebelfocus.core.domain.usecase.ExportBackupUseCase;
import com.rebelfocus.core.domain.usecase.ImportBackupUseCase;
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
public final class BackupViewModel_Factory implements Factory<BackupViewModel> {
  private final Provider<ExportBackupUseCase> exportBackupUseCaseProvider;

  private final Provider<ImportBackupUseCase> importBackupUseCaseProvider;

  public BackupViewModel_Factory(Provider<ExportBackupUseCase> exportBackupUseCaseProvider,
      Provider<ImportBackupUseCase> importBackupUseCaseProvider) {
    this.exportBackupUseCaseProvider = exportBackupUseCaseProvider;
    this.importBackupUseCaseProvider = importBackupUseCaseProvider;
  }

  @Override
  public BackupViewModel get() {
    return newInstance(exportBackupUseCaseProvider.get(), importBackupUseCaseProvider.get());
  }

  public static BackupViewModel_Factory create(
      Provider<ExportBackupUseCase> exportBackupUseCaseProvider,
      Provider<ImportBackupUseCase> importBackupUseCaseProvider) {
    return new BackupViewModel_Factory(exportBackupUseCaseProvider, importBackupUseCaseProvider);
  }

  public static BackupViewModel newInstance(ExportBackupUseCase exportBackupUseCase,
      ImportBackupUseCase importBackupUseCase) {
    return new BackupViewModel(exportBackupUseCase, importBackupUseCase);
  }
}
