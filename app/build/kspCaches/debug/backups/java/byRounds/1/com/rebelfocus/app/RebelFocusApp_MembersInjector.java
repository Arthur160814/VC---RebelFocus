package com.rebelfocus.app;

import androidx.hilt.work.HiltWorkerFactory;
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
public final class RebelFocusApp_MembersInjector implements MembersInjector<RebelFocusApp> {
  private final Provider<HiltWorkerFactory> workerFactoryProvider;

  public RebelFocusApp_MembersInjector(Provider<HiltWorkerFactory> workerFactoryProvider) {
    this.workerFactoryProvider = workerFactoryProvider;
  }

  public static MembersInjector<RebelFocusApp> create(
      Provider<HiltWorkerFactory> workerFactoryProvider) {
    return new RebelFocusApp_MembersInjector(workerFactoryProvider);
  }

  @Override
  public void injectMembers(RebelFocusApp instance) {
    injectWorkerFactory(instance, workerFactoryProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.app.RebelFocusApp.workerFactory")
  public static void injectWorkerFactory(RebelFocusApp instance, HiltWorkerFactory workerFactory) {
    instance.workerFactory = workerFactory;
  }
}
