package com.rebelfocus.services.accessibility;

import com.rebelfocus.core.data.repository.AuditEventRepository;
import com.rebelfocus.core.data.repository.SessionRepository;
import com.rebelfocus.core.database.dao.DiagnosticLogDao;
import com.rebelfocus.core.domain.blocking.BlockingDecisionEngine;
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase;
import com.rebelfocus.services.overlay.OverlayController;
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
public final class RebelFocusAccessibilityService_MembersInjector implements MembersInjector<RebelFocusAccessibilityService> {
  private final Provider<BlockingDecisionEngine> blockingDecisionEngineProvider;

  private final Provider<OverlayController> overlayControllerProvider;

  private final Provider<AuditEventRepository> auditEventRepositoryProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider;

  private final Provider<DiagnosticLogDao> diagnosticLogDaoProvider;

  public RebelFocusAccessibilityService_MembersInjector(
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider,
      Provider<OverlayController> overlayControllerProvider,
      Provider<AuditEventRepository> auditEventRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider,
      Provider<DiagnosticLogDao> diagnosticLogDaoProvider) {
    this.blockingDecisionEngineProvider = blockingDecisionEngineProvider;
    this.overlayControllerProvider = overlayControllerProvider;
    this.auditEventRepositoryProvider = auditEventRepositoryProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.stopFocusSessionUseCaseProvider = stopFocusSessionUseCaseProvider;
    this.diagnosticLogDaoProvider = diagnosticLogDaoProvider;
  }

  public static MembersInjector<RebelFocusAccessibilityService> create(
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider,
      Provider<OverlayController> overlayControllerProvider,
      Provider<AuditEventRepository> auditEventRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider,
      Provider<DiagnosticLogDao> diagnosticLogDaoProvider) {
    return new RebelFocusAccessibilityService_MembersInjector(blockingDecisionEngineProvider, overlayControllerProvider, auditEventRepositoryProvider, sessionRepositoryProvider, stopFocusSessionUseCaseProvider, diagnosticLogDaoProvider);
  }

  @Override
  public void injectMembers(RebelFocusAccessibilityService instance) {
    injectBlockingDecisionEngine(instance, blockingDecisionEngineProvider.get());
    injectOverlayController(instance, overlayControllerProvider.get());
    injectAuditEventRepository(instance, auditEventRepositoryProvider.get());
    injectSessionRepository(instance, sessionRepositoryProvider.get());
    injectStopFocusSessionUseCase(instance, stopFocusSessionUseCaseProvider.get());
    injectDiagnosticLogDao(instance, diagnosticLogDaoProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.services.accessibility.RebelFocusAccessibilityService.blockingDecisionEngine")
  public static void injectBlockingDecisionEngine(RebelFocusAccessibilityService instance,
      BlockingDecisionEngine blockingDecisionEngine) {
    instance.blockingDecisionEngine = blockingDecisionEngine;
  }

  @InjectedFieldSignature("com.rebelfocus.services.accessibility.RebelFocusAccessibilityService.overlayController")
  public static void injectOverlayController(RebelFocusAccessibilityService instance,
      OverlayController overlayController) {
    instance.overlayController = overlayController;
  }

  @InjectedFieldSignature("com.rebelfocus.services.accessibility.RebelFocusAccessibilityService.auditEventRepository")
  public static void injectAuditEventRepository(RebelFocusAccessibilityService instance,
      AuditEventRepository auditEventRepository) {
    instance.auditEventRepository = auditEventRepository;
  }

  @InjectedFieldSignature("com.rebelfocus.services.accessibility.RebelFocusAccessibilityService.sessionRepository")
  public static void injectSessionRepository(RebelFocusAccessibilityService instance,
      SessionRepository sessionRepository) {
    instance.sessionRepository = sessionRepository;
  }

  @InjectedFieldSignature("com.rebelfocus.services.accessibility.RebelFocusAccessibilityService.stopFocusSessionUseCase")
  public static void injectStopFocusSessionUseCase(RebelFocusAccessibilityService instance,
      StopFocusSessionUseCase stopFocusSessionUseCase) {
    instance.stopFocusSessionUseCase = stopFocusSessionUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.accessibility.RebelFocusAccessibilityService.diagnosticLogDao")
  public static void injectDiagnosticLogDao(RebelFocusAccessibilityService instance,
      DiagnosticLogDao diagnosticLogDao) {
    instance.diagnosticLogDao = diagnosticLogDao;
  }
}
