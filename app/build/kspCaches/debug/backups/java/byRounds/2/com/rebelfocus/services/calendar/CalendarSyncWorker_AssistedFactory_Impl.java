package com.rebelfocus.services.calendar;

import android.content.Context;
import androidx.work.WorkerParameters;
import dagger.internal.DaggerGenerated;
import dagger.internal.InstanceFactory;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class CalendarSyncWorker_AssistedFactory_Impl implements CalendarSyncWorker_AssistedFactory {
  private final CalendarSyncWorker_Factory delegateFactory;

  CalendarSyncWorker_AssistedFactory_Impl(CalendarSyncWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public CalendarSyncWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<CalendarSyncWorker_AssistedFactory> create(
      CalendarSyncWorker_Factory delegateFactory) {
    return InstanceFactory.create(new CalendarSyncWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<CalendarSyncWorker_AssistedFactory> createFactoryProvider(
      CalendarSyncWorker_Factory delegateFactory) {
    return InstanceFactory.create(new CalendarSyncWorker_AssistedFactory_Impl(delegateFactory));
  }
}
