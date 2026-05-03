package com.rebelfocus.services.scheduling;

import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.domain.logging.DiagnosticLogger;
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class TimeChangeReceiver_MembersInjector implements MembersInjector<TimeChangeReceiver> {
  private final Provider<DiagnosticLogger> loggerProvider;

  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider;

  public TimeChangeReceiver_MembersInjector(Provider<DiagnosticLogger> loggerProvider,
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider) {
    this.loggerProvider = loggerProvider;
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.scheduleSessionUseCaseProvider = scheduleSessionUseCaseProvider;
  }

  public static MembersInjector<TimeChangeReceiver> create(
      Provider<DiagnosticLogger> loggerProvider,
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider) {
    return new TimeChangeReceiver_MembersInjector(loggerProvider, ruleRepositoryProvider, scheduleSessionUseCaseProvider);
  }

  @Override
  public void injectMembers(TimeChangeReceiver instance) {
    injectLogger(instance, loggerProvider.get());
    injectRuleRepository(instance, ruleRepositoryProvider.get());
    injectScheduleSessionUseCase(instance, scheduleSessionUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.services.scheduling.TimeChangeReceiver.logger")
  public static void injectLogger(TimeChangeReceiver instance, DiagnosticLogger logger) {
    instance.logger = logger;
  }

  @InjectedFieldSignature("com.rebelfocus.services.scheduling.TimeChangeReceiver.ruleRepository")
  public static void injectRuleRepository(TimeChangeReceiver instance,
      AutomationRuleRepository ruleRepository) {
    instance.ruleRepository = ruleRepository;
  }

  @InjectedFieldSignature("com.rebelfocus.services.scheduling.TimeChangeReceiver.scheduleSessionUseCase")
  public static void injectScheduleSessionUseCase(TimeChangeReceiver instance,
      ScheduleSessionUseCase scheduleSessionUseCase) {
    instance.scheduleSessionUseCase = scheduleSessionUseCase;
  }
}
