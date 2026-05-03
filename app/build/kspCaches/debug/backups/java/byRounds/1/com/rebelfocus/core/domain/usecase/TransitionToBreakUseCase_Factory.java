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
public final class TransitionToBreakUseCase_Factory implements Factory<TransitionToBreakUseCase> {
  private final Provider<SessionEngine> sessionEngineProvider;

  public TransitionToBreakUseCase_Factory(Provider<SessionEngine> sessionEngineProvider) {
    this.sessionEngineProvider = sessionEngineProvider;
  }

  @Override
  public TransitionToBreakUseCase get() {
    return newInstance(sessionEngineProvider.get());
  }

  public static TransitionToBreakUseCase_Factory create(
      Provider<SessionEngine> sessionEngineProvider) {
    return new TransitionToBreakUseCase_Factory(sessionEngineProvider);
  }

  public static TransitionToBreakUseCase newInstance(SessionEngine sessionEngine) {
    return new TransitionToBreakUseCase(sessionEngine);
  }
}
