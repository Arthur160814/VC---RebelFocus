package com.rebelfocus.features.automation;

import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.domain.usecase.CancelScheduledSessionUseCase;
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase;
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
public final class AutomationListViewModel_Factory implements Factory<AutomationListViewModel> {
  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider;

  private final Provider<CancelScheduledSessionUseCase> cancelScheduledSessionUseCaseProvider;

  public AutomationListViewModel_Factory(Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider,
      Provider<CancelScheduledSessionUseCase> cancelScheduledSessionUseCaseProvider) {
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.scheduleSessionUseCaseProvider = scheduleSessionUseCaseProvider;
    this.cancelScheduledSessionUseCaseProvider = cancelScheduledSessionUseCaseProvider;
  }

  @Override
  public AutomationListViewModel get() {
    return newInstance(ruleRepositoryProvider.get(), scheduleSessionUseCaseProvider.get(), cancelScheduledSessionUseCaseProvider.get());
  }

  public static AutomationListViewModel_Factory create(
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider,
      Provider<CancelScheduledSessionUseCase> cancelScheduledSessionUseCaseProvider) {
    return new AutomationListViewModel_Factory(ruleRepositoryProvider, scheduleSessionUseCaseProvider, cancelScheduledSessionUseCaseProvider);
  }

  public static AutomationListViewModel newInstance(AutomationRuleRepository ruleRepository,
      ScheduleSessionUseCase scheduleSessionUseCase,
      CancelScheduledSessionUseCase cancelScheduledSessionUseCase) {
    return new AutomationListViewModel(ruleRepository, scheduleSessionUseCase, cancelScheduledSessionUseCase);
  }
}
