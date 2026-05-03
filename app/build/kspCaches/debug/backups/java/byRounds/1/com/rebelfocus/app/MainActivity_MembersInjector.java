package com.rebelfocus.app;

import com.rebelfocus.core.data.datastore.UserPreferencesDataStore;
import com.rebelfocus.services.foreground.FocusSessionOrchestrator;
import com.rebelfocus.services.foreground.NotificationHelper;
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
public final class MainActivity_MembersInjector implements MembersInjector<MainActivity> {
  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  private final Provider<FocusSessionOrchestrator> focusSessionOrchestratorProvider;

  private final Provider<NotificationHelper> notificationHelperProvider;

  public MainActivity_MembersInjector(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<FocusSessionOrchestrator> focusSessionOrchestratorProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
    this.focusSessionOrchestratorProvider = focusSessionOrchestratorProvider;
    this.notificationHelperProvider = notificationHelperProvider;
  }

  public static MembersInjector<MainActivity> create(
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider,
      Provider<FocusSessionOrchestrator> focusSessionOrchestratorProvider,
      Provider<NotificationHelper> notificationHelperProvider) {
    return new MainActivity_MembersInjector(preferencesDataStoreProvider, focusSessionOrchestratorProvider, notificationHelperProvider);
  }

  @Override
  public void injectMembers(MainActivity instance) {
    injectPreferencesDataStore(instance, preferencesDataStoreProvider.get());
    injectFocusSessionOrchestrator(instance, focusSessionOrchestratorProvider.get());
    injectNotificationHelper(instance, notificationHelperProvider.get());
  }

  @InjectedFieldSignature("com.rebelfocus.app.MainActivity.preferencesDataStore")
  public static void injectPreferencesDataStore(MainActivity instance,
      UserPreferencesDataStore preferencesDataStore) {
    instance.preferencesDataStore = preferencesDataStore;
  }

  @InjectedFieldSignature("com.rebelfocus.app.MainActivity.focusSessionOrchestrator")
  public static void injectFocusSessionOrchestrator(MainActivity instance,
      FocusSessionOrchestrator focusSessionOrchestrator) {
    instance.focusSessionOrchestrator = focusSessionOrchestrator;
  }

  @InjectedFieldSignature("com.rebelfocus.app.MainActivity.notificationHelper")
  public static void injectNotificationHelper(MainActivity instance,
      NotificationHelper notificationHelper) {
    instance.notificationHelper = notificationHelper;
  }
}
