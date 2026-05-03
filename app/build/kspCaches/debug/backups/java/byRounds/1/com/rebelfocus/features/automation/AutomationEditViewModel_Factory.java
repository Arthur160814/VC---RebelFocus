package com.rebelfocus.features.automation;

import android.content.Context;
import androidx.lifecycle.SavedStateHandle;
import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.domain.calendar.CalendarSource;
import com.rebelfocus.core.domain.scheduling.AlarmScheduler;
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class AutomationEditViewModel_Factory implements Factory<AutomationEditViewModel> {
  private final Provider<Context> appContextProvider;

  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  private final Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  private final Provider<CalendarSource> calendarSourceProvider;

  public AutomationEditViewModel_Factory(Provider<Context> appContextProvider,
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider,
      Provider<CalendarSource> calendarSourceProvider) {
    this.appContextProvider = appContextProvider;
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.scheduleSessionUseCaseProvider = scheduleSessionUseCaseProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
    this.calendarSourceProvider = calendarSourceProvider;
  }

  @Override
  public AutomationEditViewModel get() {
    return newInstance(appContextProvider.get(), savedStateHandleProvider.get(), ruleRepositoryProvider.get(), profileRepositoryProvider.get(), scheduleSessionUseCaseProvider.get(), alarmSchedulerProvider.get(), calendarSourceProvider.get());
  }

  public static AutomationEditViewModel_Factory create(Provider<Context> appContextProvider,
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<ScheduleSessionUseCase> scheduleSessionUseCaseProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider,
      Provider<CalendarSource> calendarSourceProvider) {
    return new AutomationEditViewModel_Factory(appContextProvider, savedStateHandleProvider, ruleRepositoryProvider, profileRepositoryProvider, scheduleSessionUseCaseProvider, alarmSchedulerProvider, calendarSourceProvider);
  }

  public static AutomationEditViewModel newInstance(Context appContext,
      SavedStateHandle savedStateHandle, AutomationRuleRepository ruleRepository,
      FocusProfileRepository profileRepository, ScheduleSessionUseCase scheduleSessionUseCase,
      AlarmScheduler alarmScheduler, CalendarSource calendarSource) {
    return new AutomationEditViewModel(appContext, savedStateHandle, ruleRepository, profileRepository, scheduleSessionUseCase, alarmScheduler, calendarSource);
  }
}
