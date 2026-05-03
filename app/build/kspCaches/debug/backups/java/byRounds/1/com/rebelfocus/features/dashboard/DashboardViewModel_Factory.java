package com.rebelfocus.features.dashboard;

import com.rebelfocus.core.common.permissions.PermissionChecker;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.data.repository.SessionRepository;
import com.rebelfocus.core.domain.usecase.StartFocusSessionUseCase;
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase;
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
public final class DashboardViewModel_Factory implements Factory<DashboardViewModel> {
  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<PermissionChecker> permissionCheckerProvider;

  private final Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider;

  private final Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider;

  public DashboardViewModel_Factory(Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<PermissionChecker> permissionCheckerProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider,
      Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.permissionCheckerProvider = permissionCheckerProvider;
    this.stopFocusSessionUseCaseProvider = stopFocusSessionUseCaseProvider;
    this.startFocusSessionUseCaseProvider = startFocusSessionUseCaseProvider;
  }

  @Override
  public DashboardViewModel get() {
    return newInstance(profileRepositoryProvider.get(), sessionRepositoryProvider.get(), permissionCheckerProvider.get(), stopFocusSessionUseCaseProvider.get(), startFocusSessionUseCaseProvider.get());
  }

  public static DashboardViewModel_Factory create(
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<PermissionChecker> permissionCheckerProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider,
      Provider<StartFocusSessionUseCase> startFocusSessionUseCaseProvider) {
    return new DashboardViewModel_Factory(profileRepositoryProvider, sessionRepositoryProvider, permissionCheckerProvider, stopFocusSessionUseCaseProvider, startFocusSessionUseCaseProvider);
  }

  public static DashboardViewModel newInstance(FocusProfileRepository profileRepository,
      SessionRepository sessionRepository, PermissionChecker permissionChecker,
      StopFocusSessionUseCase stopFocusSessionUseCase,
      StartFocusSessionUseCase startFocusSessionUseCase) {
    return new DashboardViewModel(profileRepository, sessionRepository, permissionChecker, stopFocusSessionUseCase, startFocusSessionUseCase);
  }
}
