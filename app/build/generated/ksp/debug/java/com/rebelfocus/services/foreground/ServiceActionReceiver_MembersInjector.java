package com.rebelfocus.services.foreground;

import com.rebelfocus.core.domain.usecase.PauseSessionUseCase;
import com.rebelfocus.core.domain.usecase.ResumeSessionUseCase;
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase;
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
public final class ServiceActionReceiver_MembersInjector implements MembersInjector<ServiceActionReceiver> {
  private final Provider<PauseSessionUseCase> pauseSessionUseCaseProvider;

  private final Provider<ResumeSessionUseCase> resumeSessionUseCaseProvider;

  private final Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider;

  public ServiceActionReceiver_MembersInjector(
      Provider<PauseSessionUseCase> pauseSessionUseCaseProvider,
      Provider<ResumeSessionUseCase> resumeSessionUseCaseProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider) {
    this.pauseSessionUseCaseProvider = pauseSessionUseCaseProvider;
    this.resumeSessionUseCaseProvider = resumeSessionUseCaseProvider;
    this.stopFocusSessionUseCaseProvider = stopFocusSessionUseCaseProvider;
  }

  public static MembersInjector<ServiceActionReceiver> create(
      Provider<PauseSessionUseCase> pauseSessionUseCaseProvider,
      Provider<ResumeSessionUseCase> resumeSessionUseCaseProvider,
      Provider<StopFocusSessionUseCase> stopFocusSessionUseCaseProvider) {
    return new ServiceActionReceiver_MembersInjector(pauseSessionUseCaseProvider, resumeSessionUseCaseProvider, stopFocusSessionUseCaseProvider);
  }

  @Override
  public void injectMembers(ServiceActionReceiver instance) {
    injectPauseSessionUseCase(instance, pauseSessionUseCaseProvider.get());
    injectResumeSessionUseCase(instance, resumeSessionUseCaseProvider.get());
    injectStopFocusSessionUseCase(instance, stopFocusSessionUseCaseProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.ServiceActionReceiver.pauseSessionUseCase")
  public static void injectPauseSessionUseCase(ServiceActionReceiver instance,
      PauseSessionUseCase pauseSessionUseCase) {
    instance.pauseSessionUseCase = pauseSessionUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.ServiceActionReceiver.resumeSessionUseCase")
  public static void injectResumeSessionUseCase(ServiceActionReceiver instance,
      ResumeSessionUseCase resumeSessionUseCase) {
    instance.resumeSessionUseCase = resumeSessionUseCase;
  }

  @InjectedFieldSignature("com.rebelfocus.services.foreground.ServiceActionReceiver.stopFocusSessionUseCase")
  public static void injectStopFocusSessionUseCase(ServiceActionReceiver instance,
      StopFocusSessionUseCase stopFocusSessionUseCase) {
    instance.stopFocusSessionUseCase = stopFocusSessionUseCase;
  }
}
