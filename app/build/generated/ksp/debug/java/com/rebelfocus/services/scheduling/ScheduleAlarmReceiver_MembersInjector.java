package com.rebelfocus.services.scheduling;

import com.rebelfocus.core.domain.usecase.ExecuteScheduleUseCase;
import com.rebelfocus.core.domain.usecase.ExecuteScheduledTriggerUseCase;
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
public final class ScheduleAlarmReceiver_MembersInjector implements MembersInjector<ScheduleAlarmReceiver> {
  private final Provider<ExecuteScheduleUseCase> executeScheduleUseCaseProvider;

  private final Provider<ExecuteScheduledTriggerUseCase> executeScheduledTriggerUseCaseProvider;

  public ScheduleAlarmReceiver_MembersInjector(
      Provider<ExecuteScheduleUseCase> executeScheduleUseCaseProvider,
      Provider<ExecuteScheduledTriggerUseCase> executeScheduledTriggerUseCaseProvider) {
    this.executeScheduleUseCaseProvider = executeScheduleUseCaseProvider;
    this.executeScheduledTriggerUseCaseProvider = executeScheduledTriggerUseCaseProvider;
  }

  public static MembersInjector<ScheduleAlarmReceiver> create(
      Provider<ExecuteScheduleUseCase> executeScheduleUseCaseProvider,
      Provider<ExecuteScheduledTriggerUseCase> executeScheduledTriggerUseCaseProvider) {
    return new ScheduleAlarmReceiver_MembersInjector(executeScheduleUseCaseProvider, executeScheduledTriggerUseCaseProvider);
  }

  @Override
  public void injectMembers(ScheduleAlarmReceiver instance) {
    injectExecuteScheduleUseCase(instance, executeScheduleUseCaseProvider.get());
    injectExecuteScheduledTriggerUseCase(instance, executeScheduledTriggerUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.services.scheduling.ScheduleAlarmReceiver.executeScheduleUseCase")
  public static void injectExecuteScheduleUseCase(ScheduleAlarmReceiver instance,
      ExecuteScheduleUseCase executeScheduleUseCase) {
    instance.executeScheduleUseCase = executeScheduleUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.scheduling.ScheduleAlarmReceiver.executeScheduledTriggerUseCase")
  public static void injectExecuteScheduledTriggerUseCase(ScheduleAlarmReceiver instance,
      ExecuteScheduledTriggerUseCase executeScheduledTriggerUseCase) {
    instance.executeScheduledTriggerUseCase = executeScheduledTriggerUseCase;
  }
}
