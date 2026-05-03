package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.data.repository.ScheduledTriggerRepository;
import com.rebelfocus.core.domain.calendar.CalendarSource;
import com.rebelfocus.core.domain.scheduling.AlarmScheduler;
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
public final class SyncCalendarEventsUseCase_Factory implements Factory<SyncCalendarEventsUseCase> {
  private final Provider<AutomationRuleRepository> ruleRepositoryProvider;

  private final Provider<ScheduledTriggerRepository> triggerRepositoryProvider;

  private final Provider<CalendarSource> calendarSourceProvider;

  private final Provider<AlarmScheduler> alarmSchedulerProvider;

  public SyncCalendarEventsUseCase_Factory(
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduledTriggerRepository> triggerRepositoryProvider,
      Provider<CalendarSource> calendarSourceProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    this.ruleRepositoryProvider = ruleRepositoryProvider;
    this.triggerRepositoryProvider = triggerRepositoryProvider;
    this.calendarSourceProvider = calendarSourceProvider;
    this.alarmSchedulerProvider = alarmSchedulerProvider;
  }

  @Override
  public SyncCalendarEventsUseCase get() {
    return newInstance(ruleRepositoryProvider.get(), triggerRepositoryProvider.get(), calendarSourceProvider.get(), alarmSchedulerProvider.get());
  }

  public static SyncCalendarEventsUseCase_Factory create(
      Provider<AutomationRuleRepository> ruleRepositoryProvider,
      Provider<ScheduledTriggerRepository> triggerRepositoryProvider,
      Provider<CalendarSource> calendarSourceProvider,
      Provider<AlarmScheduler> alarmSchedulerProvider) {
    return new SyncCalendarEventsUseCase_Factory(ruleRepositoryProvider, triggerRepositoryProvider, calendarSourceProvider, alarmSchedulerProvider);
  }

  public static SyncCalendarEventsUseCase newInstance(AutomationRuleRepository ruleRepository,
      ScheduledTriggerRepository triggerRepository, CalendarSource calendarSource,
      AlarmScheduler alarmScheduler) {
    return new SyncCalendarEventsUseCase(ruleRepository, triggerRepository, calendarSource, alarmScheduler);
  }
}
