package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.domain.session.SessionEngine;
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
public final class StopFocusSessionUseCase_Factory implements Factory<StopFocusSessionUseCase> {
  private final Provider<SessionEngine> sessionEngineProvider;

  public StopFocusSessionUseCase_Factory(Provider<SessionEngine> sessionEngineProvider) {
    this.sessionEngineProvider = sessionEngineProvider;
  }

  @Override
  public StopFocusSessionUseCase get() {
    return newInstance(sessionEngineProvider.get());
  }

  public static StopFocusSessionUseCase_Factory create(
      Provider<SessionEngine> sessionEngineProvider) {
    return new StopFocusSessionUseCase_Factory(sessionEngineProvider);
  }

  public static StopFocusSessionUseCase newInstance(SessionEngine sessionEngine) {
    return new StopFocusSessionUseCase(sessionEngine);
  }
}
