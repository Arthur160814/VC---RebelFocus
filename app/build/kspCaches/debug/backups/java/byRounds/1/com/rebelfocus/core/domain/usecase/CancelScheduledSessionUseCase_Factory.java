package com.rebelfocus.core.domain.usecase;

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
public final class CancelScheduledSessionUseCase_Factory implements Factory<CancelScheduledSessionUseCase> {
  private final Provider<AlarmScheduler> schedulerProvider;

  public CancelScheduledSessionUseCase_Factory(Provider<AlarmScheduler> schedulerProvider) {
    this.schedulerProvider = schedulerProvider;
  }

  @Override
  public CancelScheduledSessionUseCase get() {
    return newInstance(schedulerProvider.get());
  }

  public static CancelScheduledSessionUseCase_Factory create(
      Provider<AlarmScheduler> schedulerProvider) {
    return new CancelScheduledSessionUseCase_Factory(schedulerProvider);
  }

  public static CancelScheduledSessionUseCase newInstance(AlarmScheduler scheduler) {
    return new CancelScheduledSessionUseCase(scheduler);
  }
}
