package com.rebelfocus.services.scheduling;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase;
import com.rebelfocus.core.domain.usecase.SyncCalendarEventsUseCase;
import dagger.internal.DaggerGenerated;
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
public final class RestoreSchedulesWorker_Factory {
  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider;

  private final Provider<SyncCalendarEventsUseCase> syncCalendarEventsUseCaseProvider;

  public RestoreSchedulesWorker_Factory(Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider,
      Provider<SyncCalendarEventsUseCase> syncCalendarEventsUseCaseProvider) {
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.scheduleSessionUseCaseProvider = scheduleSessionUseCaseProvider;
    this.syncCalendarEventsUseCaseProvider = syncCalendarEventsUseCaseProvider;
  }

  public RestoreSchedulesWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, ruleRepositoryProvider.get(), scheduleSessionUseCaseProvider.get(), syncCalendarEventsUseCaseProvider.get());
  }

  public static RestoreSchedulesWorker_Factory create(
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider,
      Provider<SyncCalendarEventsUseCase> syncCalendarEventsUseCaseProvider) {
    return new RestoreSchedulesWorker_Factory(ruleRepositoryProvider, scheduleSessionUseCaseProvider, syncCalendarEventsUseCaseProvider);
  }

  public static RestoreSchedulesWorker newInstance(Context appContext,
      WorkerParameters workerParams, AutomationRuleRepository ruleRepository,
      ScheduleSessionUseCase scheduleSessionUseCase,
      SyncCalendarEventsUseCase syncCalendarEventsUseCase) {
    return new RestoreSchedulesWorker(appContext, workerParams, ruleRepository, scheduleSessionUseCase, syncCalendarEventsUseCase);
  }
}
