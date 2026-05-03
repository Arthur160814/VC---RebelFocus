package com.rebelfocus.core.data.repository.impl;

import com.rebelfocus.core.database.dao.AutomationRuleDao;
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
public final class AutomationRuleRepositoryImpl_Factory implements Factory<AutomationRuleRepositoryImpl> {
  private final Provider<AutomationRuleDao> automationRuleDaoProvider;

  public AutomationRuleRepositoryImpl_Factory(
      Provider<AutomationRuleDao> automationRuleDaoProvider) {
    this.automationRuleDaoProvider = automationRuleDaoProvider;
  }

  @Override
  public AutomationRuleRepositoryImpl get() {
    return newInstance(automationRuleDaoProvider.get());
  }

  public static AutomationRuleRepositoryImpl_Factory create(
      Provider<AutomationRuleDao> automationRuleDaoProvider) {
    return new AutomationRuleRepositoryImpl_Factory(automationRuleDaoProvider);
  }

  public static AutomationRuleRepositoryImpl newInstance(AutomationRuleDao automationRuleDao) {
    return new AutomationRuleRepositoryImpl(automationRuleDao);
  }
}
