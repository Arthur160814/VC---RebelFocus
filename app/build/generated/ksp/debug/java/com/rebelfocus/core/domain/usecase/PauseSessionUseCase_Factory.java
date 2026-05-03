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
public final class PauseSessionUseCase_Factory implements Factory<PauseSessionUseCase> {
  private final Provider<SessionEngine> sessionEngineProvider;

  public PauseSessionUseCase_Factory(Provider<SessionEngine> sessionEngineProvider) {
    this.sessionEngineProvider = sessionEngineProvider;
  }

  @Override
  public PauseSessionUseCase get() {
    return newInstance(sessionEngineProvider.get());
  }

  public static PauseSessionUseCase_Factory create(Provider<SessionEngine> sessionEngineProvider) {
    return new PauseSessionUseCase_Factory(sessionEngineProvider);
  }

  public static PauseSessionUseCase newInstance(SessionEngine sessionEngine) {
    return new PauseSessionUseCase(sessionEngine);
  }
}
