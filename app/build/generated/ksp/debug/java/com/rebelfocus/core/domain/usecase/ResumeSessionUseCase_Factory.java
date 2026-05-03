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
public final class ResumeSessionUseCase_Factory implements Factory<ResumeSessionUseCase> {
  private final Provider<SessionEngine> sessionEngineProvider;

  public ResumeSessionUseCase_Factory(Provider<SessionEngine> sessionEngineProvider) {
    this.sessionEngineProvider = sessionEngineProvider;
  }

  @Override
  public ResumeSessionUseCase get() {
    return newInstance(sessionEngineProvider.get());
  }

  public static ResumeSessionUseCase_Factory create(Provider<SessionEngine> sessionEngineProvider) {
    return new ResumeSessionUseCase_Factory(sessionEngineProvider);
  }

  public static ResumeSessionUseCase newInstance(SessionEngine sessionEngine) {
    return new ResumeSessionUseCase(sessionEngine);
  }
}
