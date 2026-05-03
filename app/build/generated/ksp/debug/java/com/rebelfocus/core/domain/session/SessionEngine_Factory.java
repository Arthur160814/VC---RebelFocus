package com.rebelfocus.core.domain.session;

import com.rebelfocus.core.data.repository.AuditEventRepository;
import com.rebelfocus.core.data.repository.SessionRepository;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class SessionEngine_Factory implements Factory<SessionEngine> {
  private final Provider<TimeSource> timeSourceProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<AuditEventRepository> auditEventRepositoryProvider;

  private final Provider<SessionStateMachine> stateMachineProvider;

  private final Provider<SessionTimingCalculator> timingCalculatorProvider;

  public SessionEngine_Factory(Provider<TimeSource> timeSourceProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<AuditEventRepository> auditEventRepositoryProvider,
      Provider<SessionStateMachine> stateMachineProvider,
      Provider<SessionTimingCalculator> timingCalculatorProvider) {
    this.timeSourceProvider = timeSourceProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.auditEventRepositoryProvider = auditEventRepositoryProvider;
    this.stateMachineProvider = stateMachineProvider;
    this.timingCalculatorProvider = timingCalculatorProvider;
  }

  @Override
  public SessionEngine get() {
    return newInstance(timeSourceProvider.get(), sessionRepositoryProvider.get(), auditEventRepositoryProvider.get(), stateMachineProvider.get(), timingCalculatorProvider.get());
  }

  public static SessionEngine_Factory create(Provider<TimeSource> timeSourceProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<AuditEventRepository> auditEventRepositoryProvider,
      Provider<SessionStateMachine> stateMachineProvider,
      Provider<SessionTimingCalculator> timingCalculatorProvider) {
    return new SessionEngine_Factory(timeSourceProvider, sessionRepositoryProvider, auditEventRepositoryProvider, stateMachineProvider, timingCalculatorProvider);
  }

  public static SessionEngine newInstance(TimeSource timeSource,
      SessionRepository sessionRepository, AuditEventRepository auditEventRepository,
      SessionStateMachine stateMachine, SessionTimingCalculator timingCalculator) {
    return new SessionEngine(timeSource, sessionRepository, auditEventRepository, stateMachine, timingCalculator);
  }
}
