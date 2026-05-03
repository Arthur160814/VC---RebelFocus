package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.domain.scheduling.AlarmScheduler;
import com.rebelfocus.core.domain.scheduling.NextOccurrenceCalculator;
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
public final class ScheduleSessionUseCase_Factory implements Factory<ScheduleSessionUseCase> {
  private final Provider<NextOccurrenceCalculator> calculatorProvider;

  private final Provider<AlarmScheduler> schedulerProvider;

  public ScheduleSessionUseCase_Factory(Provider<NextOccurrenceCalculator> calculatorProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    this.calculatorProvider = calculatorProvider;
    this.schedulerProvider = schedulerProvider;
  }

  @Override
  public ScheduleSessionUseCase get() {
    return newInstance(calculatorProvider.get(), schedulerProvider.get());
  }

  public static ScheduleSessionUseCase_Factory create(
      Provider<NextOccurrenceCalculator> calculatorProvider,
      Provider<AlarmScheduler> schedulerProvider) {
    return new ScheduleSessionUseCase_Factory(calculatorProvider, schedulerProvider);
  }

  public static ScheduleSessionUseCase newInstance(NextOccurrenceCalculator calculator,
      AlarmScheduler scheduler) {
    return new ScheduleSessionUseCase(calculator, scheduler);
  }
}
