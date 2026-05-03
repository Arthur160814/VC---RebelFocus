package com.rebelfocus.services.foreground;

import com.rebelfocus.core.data.repository.SessionRepository;
import com.rebelfocus.core.domain.logging.DiagnosticLogger;
import com.rebelfocus.core.domain.session.SessionTimingCalculator;
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase;
import com.rebelfocus.core.domain.usecase.TransitionToActiveFocusUseCase;
import com.rebelfocus.core.domain.usecase.TransitionToBreakUseCase;
import dagger.MembersInjector;
import dagger.internal.DaggerGenerated;
import dagger.internal.InjectedFieldSignature;
import dagger.internal.QualifierMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

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
public final class FocusSessionService_MembersInjector implements MembersInjector<FocusSessionService> {
  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  private final Provider<SessionTimingCalculator> timingCalculatorProvider;

  private final Provider<TransitionToBreakUseCase> transitionToBreakUseCaseProvider;

  private final Provider<TransitionToActiveFocusUseCase> transitionToActiveFocusUseCaseProvider;

  private final Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider;

  private final Provider<DiagnosticLogger> diagnosticLoggerProvider;

  public FocusSessionService_MembersInjector(Provider<SessionRepository> sessionRepositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<SessionTimingCalculator> timingCalculatorProvider,
      Provider<TransitionToBreakUseCase> transitionToBreakUseCaseProvider,
      Provider<TransitionToActiveFocusUseCase> transitionToActiveFocusUseCaseProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider,
      Provider<DiagnosticLogger> diagnosticLoggerProvider) {
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.notificationHelperProvider = notificationHelperProvider;
    this.timingCalculatorProvider = timingCalculatorProvider;
    this.transitionToBreakUseCaseProvider = transitionToBreakUseCaseProvider;
    this.transitionToActiveFocusUseCaseProvider = transitionToActiveFocusUseCaseProvider;
    this.stopFocusSessionUseCaseProvider = stopFocusSessionUseCaseProvider;
    this.diagnosticLoggerProvider = diagnosticLoggerProvider;
  }

  public static MembersInjector<FocusSessionService> create(
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<NotificationHelper> notificationHelperProvider,
      Provider<SessionTimingCalculator> timingCalculatorProvider,
      Provider<TransitionToBreakUseCase> transitionToBreakUseCaseProvider,
      Provider<TransitionToActiveFocusUseCase> transitionToActiveFocusUseCaseProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider,
      Provider<DiagnosticLogger> diagnosticLoggerProvider) {
    return new FocusSessionService_MembersInjector(sessionRepositoryProvider, notificationHelperProvider, timingCalculatorProvider, transitionToBreakUseCaseProvider, transitionToActiveFocusUseCaseProvider, stopFocusSessionUseCaseProvider, diagnosticLoggerProvider);
  }

  @Override
  public void injectMembers(FocusSessionService instance) {
    injectSessionRepository(instance, sessionRepositoryProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
    injectTimingCalculator(instance, timingCalculatorProvider.get());
    injectTransitionToBreakUseCase(instance, transitionToBreakUseCaseProvider.get());
    injectTransitionToActiveFocusUseCase(instance, transitionToActiveFocusUseCaseProvider.get());
    injectStopFocusSessionUseCase(instance, stopFocusSessionUseCaseProvider.get());
    injectDiagnosticLogger(instance, diagnosticLoggerProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.sessionRepository")
  public static void injectSessionRepository(FocusSessionService instance,
      SessionRepository sessionRepository) {
    instance.sessionRepository = sessionRepository;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.notificationHelper")
  public static void injectNotificationHelper(FocusSessionService instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.timingCalculator")
  public static void injectTimingCalculator(FocusSessionService instance,
      SessionTimingCalculator timingCalculator) {
    instance.timingCalculator = timingCalculator;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.transitionToBreakUseCase")
  public static void injectTransitionToBreakUseCase(FocusSessionService instance,
      TransitionToBreakUseCase transitionToBreakUseCase) {
    instance.transitionToBreakUseCase = transitionToBreakUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.transitionToActiveFocusUseCase")
  public static void injectTransitionToActiveFocusUseCase(FocusSessionService instance,
      TransitionToActiveFocusUseCase transitionToActiveFocusUseCase) {
    instance.transitionToActiveFocusUseCase = transitionToActiveFocusUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.stopFocusSessionUseCase")
  public static void injectStopFocusSessionUseCase(FocusSessionService instance,
      StopFocusSessionUseCase stopFocusSessionUseCase) {
    instance.stopFocusSessionUseCase = stopFocusSessionUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.FocusSessionService.diagnosticLogger")
  public static void injectDiagnosticLogger(FocusSessionService instance,
      DiagnosticLogger diagnosticLogger) {
    instance.diagnosticLogger = diagnosticLogger;
  }
}
