package com.rebelfocus.app.di;

import com.rebelfocus.core.database.RebelFocusDatabase;
import com.rebelfocus.core.database.dao.AutomationRuleDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideAutomationRuleDaoFactory implements Factory<AutomationRuleDao> {
  private final Provider<RebelFocusDatabase> dbProvider;

  public DatabaseModule_ProvideAutomationRuleDaoFactory(Provider<RebelFocusDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public AutomationRuleDao get() {
    return provideAutomationRuleDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideAutomationRuleDaoFactory create(
      Provider<RebelFocusDatabase> dbProvider) {
    return new DatabaseModule_ProvideAutomationRuleDaoFactory(dbProvider);
  }

  public static AutomationRuleDao provideAutomationRuleDao(RebelFocusDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideAutomationRuleDao(db));
  }
}
