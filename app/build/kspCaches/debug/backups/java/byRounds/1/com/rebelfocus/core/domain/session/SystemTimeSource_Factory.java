package com.rebelfocus.core.domain.session;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SystemTimeSource_Factory implements Factory<SystemTimeSource> {
  @Override
  public SystemTimeSource get() {
    return newInstance();
  }

  public static SystemTimeSource_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SystemTimeSource newInstance() {
    return new SystemTimeSource();
  }

  private static final class InstanceHolder {
    private static final SystemTimeSource_Factory INSTANCE = new SystemTimeSource_Factory();
  }
}
