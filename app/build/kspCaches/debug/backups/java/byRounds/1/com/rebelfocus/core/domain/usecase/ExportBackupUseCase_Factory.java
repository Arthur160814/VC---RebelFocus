package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.database.RebelFocusDatabase;
import com.rebelfocus.core.database.dao.AutomationRuleDao;
import com.rebelfocus.core.database.dao.BlockedAppDao;
import com.rebelfocus.core.database.dao.FocusProfileDao;
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
public final class ExportBackupUseCase_Factory implements Factory<ExportBackupUseCase> {
  private final Provider<FocusProfileDao> profileDaoProvider;

  private final Provider<BlockedAppDao> appDaoProvider;

  private final Provider<AutomationRuleDao> ruleDaoProvider;

  private final Provider<RebelFocusDatabase> databaseProvider;

  private final Provider<BackupProvider> backupProvider;

  public ExportBackupUseCase_Factory(Provider<FocusProfileDao> profileDaoProvider,
      Provider<BlockedAppDao> appDaoProvider, Provider<AutomationRuleDao> ruleDaoProvider,
      Provider<RebelFocusDatabase> databaseProvider, Provider<BackupProvider> backupProvider) {
    this.profileDaoProvider = profileDaoProvider;
    this.appDaoProvider = appDaoProvider;
    this.ruleDaoProvider = ruleDaoProvider;
    this.databaseProvider = databaseProvider;
    this.backupProvider = backupProvider;
  }

  @Override
  public ExportBackupUseCase get() {
    return newInstance(profileDaoProvider.get(), appDaoProvider.get(), ruleDaoProvider.get(), databaseProvider.get(), backupProvider.get());
  }

  public static ExportBackupUseCase_Factory create(Provider<FocusProfileDao> profileDaoProvider,
      Provider<BlockedAppDao> appDaoProvider, Provider<AutomationRuleDao> ruleDaoProvider,
      Provider<RebelFocusDatabase> databaseProvider, Provider<BackupProvider> backupProvider) {
    return new ExportBackupUseCase_Factory(profileDaoProvider, appDaoProvider, ruleDaoProvider, databaseProvider, backupProvider);
  }

  public static ExportBackupUseCase newInstance(FocusProfileDao profileDao, BlockedAppDao appDao,
      AutomationRuleDao ruleDao, RebelFocusDatabase database, BackupProvider backupProvider) {
    return new ExportBackupUseCase(profileDao, appDao, ruleDao, database, backupProvider);
  }
}
