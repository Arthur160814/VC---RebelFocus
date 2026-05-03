package com.rebelfocus.services.scheduling;

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
public final class RestoreSchedulesWorker_AssistedFactory_Impl implements RestoreSchedulesWorker_AssistedFactory {
  private final RestoreSchedulesWorker_Factory delegateFactory;

  RestoreSchedulesWorker_AssistedFactory_Impl(RestoreSchedulesWorker_Factory delegateFactory) {
    this.delegateFactory = delegateFactory;
  }

  @Override
  public RestoreSchedulesWorker create(Context p0, WorkerParameters p1) {
    return delegateFactory.get(p0, p1);
  }

  public static Provider<RestoreSchedulesWorker_AssistedFactory> create(
      RestoreSchedulesWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RestoreSchedulesWorker_AssistedFactory_Impl(delegateFactory));
  }

  public static dagger.internal.Provider<RestoreSchedulesWorker_AssistedFactory> createFactoryProvider(
      RestoreSchedulesWorker_Factory delegateFactory) {
    return InstanceFactory.create(new RestoreSchedulesWorker_AssistedFactory_Impl(delegateFactory));
  }
}
