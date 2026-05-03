package com.rebelfocus.app.di;

import com.rebelfocus.core.domain.session.TimeSource;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

@ScopeMetadata("javax.inject.Singleton")
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
public final class SessionModule_ProvideTimeSourceFactory implements Factory<TimeSource> {
  @Override
  public TimeSource get() {
    return provideTimeSource();
  }

  public static SessionModule_ProvideTimeSourceFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static TimeSource provideTimeSource() {
    return Preconditions.checkNotNullFromProvides(SessionModule.INSTANCE.provideTimeSource());
  }

  private static final class InstanceHolder {
    private static final SessionModule_ProvideTimeSourceFactory INSTANCE = new SessionModule_ProvideTimeSourceFactory();
  }
}
