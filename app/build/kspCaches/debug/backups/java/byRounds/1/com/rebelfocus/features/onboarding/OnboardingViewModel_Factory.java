package com.rebelfocus.features.onboarding;

import com.rebelfocus.core.common.permissions.PermissionChecker;
import com.rebelfocus.core.data.datastore.UserPreferencesDataStore;
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
public final class OnboardingViewModel_Factory implements Factory<OnboardingViewModel> {
  private final Provider<PermissionChecker> permissionCheckerProvider;

  private final Provider<UserPreferencesDataStore> preferencesDataStoreProvider;

  public OnboardingViewModel_Factory(Provider<PermissionChecker> permissionCheckerProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    this.permissionCheckerProvider = permissionCheckerProvider;
    this.preferencesDataStoreProvider = preferencesDataStoreProvider;
  }

  @Override
  public OnboardingViewModel get() {
    return newInstance(permissionCheckerProvider.get(), preferencesDataStoreProvider.get());
  }

  public static OnboardingViewModel_Factory create(
      Provider<PermissionChecker> permissionCheckerProvider,
      Provider<UserPreferencesDataStore> preferencesDataStoreProvider) {
    return new OnboardingViewModel_Factory(permissionCheckerProvider, preferencesDataStoreProvider);
  }

  public static OnboardingViewModel newInstance(PermissionChecker permissionChecker,
      UserPreferencesDataStore preferencesDataStore) {
    return new OnboardingViewModel(permissionChecker, preferencesDataStore);
  }
}
