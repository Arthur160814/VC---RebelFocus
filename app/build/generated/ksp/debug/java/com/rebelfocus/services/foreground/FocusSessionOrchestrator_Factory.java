package com.rebelfocus.services.foreground;

import android.content.Context;
import com.rebelfocus.core.data.repository.SessionRepository;
import com.rebelfocus.core.domain.logging.DiagnosticLogger;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class FocusSessionOrchestrator_Factory implements Factory<FocusSessionOrchestrator> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<DiagnosticLogger> loggerProvider;

  public FocusSessionOrchestrator_Factory(Provider<Context> contextProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<DiagnosticLogger> loggerProvider) {
    this.contextProvider = contextProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.loggerProvider = loggerProvider;
  }

  @Override
  public FocusSessionOrchestrator get() {
    return newInstance(contextProvider.get(), sessionRepositoryProvider.get(), loggerProvider.get());
  }

  public static FocusSessionOrchestrator_Factory create(Provider<Context> contextProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<DiagnosticLogger> loggerProvider) {
    return new FocusSessionOrchestrator_Factory(contextProvider, sessionRepositoryProvider, loggerProvider);
  }

  public static FocusSessionOrchestrator newInstance(Context context,
      SessionRepository sessionRepository, DiagnosticLogger logger) {
    return new FocusSessionOrchestrator(context, sessionRepository, logger);
  }
}
