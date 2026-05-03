package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
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
public final class ExecuteScheduleUseCase_Factory implements Factory<ExecuteScheduleUseCase> {
  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  private final Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider;

  private final Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider;

  public ExecuteScheduleUseCase_Factory(Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider) {
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.startFocusSessionUseCaseProvider = startFocusSessionUseCaseProvider;
    this.scheduleSessionUseCaseProvider = scheduleSessionUseCaseProvider;
  }

  @Override
  public ExecuteScheduleUseCase get() {
    return newInstance(ruleRepositoryProvider.get(), profileRepositoryProvider.get(), startFocusSessionUseCaseProvider.get(), scheduleSessionUseCaseProvider.get());
  }

  public static ExecuteScheduleUseCase_Factory create(
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider) {
    return new ExecuteScheduleUseCase_Factory(ruleRepositoryProvider, profileRepositoryProvider, startFocusSessionUseCaseProvider, scheduleSessionUseCaseProvider);
  }

  public static ExecuteScheduleUseCase newInstance(AutomationRuleRepository ruleRepository,
      FocusProfileRepository profileRepository, StartFocusSessionUseCase startFocusSessionUseCase,
      ScheduleSessionUseCase scheduleSessionUseCase) {
    return new ExecuteScheduleUseCase(ruleRepository, profileRepository, startFocusSessionUseCase, scheduleSessionUseCase);
  }
}
