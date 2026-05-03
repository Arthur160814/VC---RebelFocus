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
public final class TransitionToActiveFocusUseCase_Factory implements Factory<TransitionToActiveFocusUseCase> {
  private final Provider<SessionEngine> sessionEngineProvider;

  public TransitionToActiveFocusUseCase_Factory(Provider<SessionEngine> sessionEngineProvider) {
    this.sessionEngineProvider = sessionEngineProvider;
  }

  @Override
  public TransitionToActiveFocusUseCase get() {
    return newInstance(sessionEngineProvider.get());
  }

  public static TransitionToActiveFocusUseCase_Factory create(
      Provider<SessionEngine> sessionEngineProvider) {
    return new TransitionToActiveFocusUseCase_Factory(sessionEngineProvider);
  }

  public static TransitionToActiveFocusUseCase newInstance(SessionEngine sessionEngine) {
    return new TransitionToActiveFocusUseCase(sessionEngine);
  }
}
