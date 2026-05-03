package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.data.repository.SessionRepository;
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
public final class StartFocusSessionUseCase_Factory implements Factory<StartFocusSessionUseCase> {
  private final Provider<SessionEngine> sessionEngineProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  public StartFocusSessionUseCase_Factory(Provider<SessionEngine> sessionEngineProvider,
      Provider<SessionRepository> sessionRepositoryProvider) {
    this.sessionEngineProvider = sessionEngineProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
  }

  @Override
  public StartFocusSessionUseCase get() {
    return newInstance(sessionEngineProvider.get(), sessionRepositoryProvider.get());
  }

  public static StartFocusSessionUseCase_Factory create(
      Provider<SessionEngine> sessionEngineProvider,
      Provider<SessionRepository> sessionRepositoryProvider) {
    return new StartFocusSessionUseCase_Factory(sessionEngineProvider, sessionRepositoryProvider);
  }

  public static StartFocusSessionUseCase newInstance(SessionEngine sessionEngine,
      SessionRepository sessionRepository) {
    return new StartFocusSessionUseCase(sessionEngine, sessionRepository);
  }
}
