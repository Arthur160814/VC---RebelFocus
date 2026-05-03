package com.rebelfocus.services.calendar;

import android.content.Context;
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
public final class DeviceCalendarSource_Factory implements Factory<DeviceCalendarSource> {
  private final Provider<Context> contextProvider;

  public DeviceCalendarSource_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public DeviceCalendarSource get() {
    return newInstance(contextProvider.get());
  }

  public static DeviceCalendarSource_Factory create(Provider<Context> contextProvider) {
    return new DeviceCalendarSource_Factory(contextProvider);
  }

  public static DeviceCalendarSource newInstance(Context context) {
    return new DeviceCalendarSource(context);
  }
}
