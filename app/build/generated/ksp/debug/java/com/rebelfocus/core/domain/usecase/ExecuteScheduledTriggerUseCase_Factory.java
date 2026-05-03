package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.data.repository.ScheduledTriggerRepository;
import com.rebelfocus.core.data.repository.SessionRepository;
import com.rebelfocus.core.domain.logging.DiagnosticLogger;
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
public final class ExecuteScheduledTriggerUseCase_Factory implements Factory<ExecuteScheduledTriggerUseCase> {
  private final Provider<ScheduledTriggerRepository> triggerRepositoryProvider;

  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider;

  private final Provider<DiagnosticLogger> loggerProvider;

  public ExecuteScheduledTriggerUseCase_Factory(
      Provider<ScheduledTriggerRepository> triggerRepositoryProvider,
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider,
      Provider<DiagnosticLogger> loggerProvider) {
    this.triggerRepositoryProvider = triggerRepositoryProvider;
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.startFocusSessionUseCaseProvider = startFocusSessionUseCaseProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public ExecuteScheduledTriggerUseCase get() {
    return newInstance(triggerRepositoryProvider.get(), ruleRepositoryProvider.get(), profileRepositoryProvider.get(), sessionRepositoryProvider.get(), startFocusSessionUseCaseProvider.get(), loggerProvider.get());
  }

  public static ExecuteScheduledTriggerUseCase_Factory create(
      Provider<ScheduledTriggerRepository> triggerRepositoryProvider,
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider,
      Provider<DiagnosticLogger> loggerProvider) {
    return new ExecuteScheduledTriggerUseCase_Factory(triggerRepositoryProvider, ruleRepositoryProvider, profileRepositoryProvider, sessionRepositoryProvider, startFocusSessionUseCaseProvider, loggerProvider);
  }

  public static ExecuteScheduledTriggerUseCase newInstance(
      ScheduledTriggerRepository triggerRepository, AutomationRuleRepository ruleRepository,
      FocusProfileRepository profileRepository, SessionRepository sessionRepository,
      StartFocusSessionUseCase startFocusSessionUseCase, DiagnosticLogger logger) {
    return new ExecuteScheduledTriggerUseCase(triggerRepository, ruleRepository, profileRepository, sessionRepository, startFocusSessionUseCase, logger);
  }
}
