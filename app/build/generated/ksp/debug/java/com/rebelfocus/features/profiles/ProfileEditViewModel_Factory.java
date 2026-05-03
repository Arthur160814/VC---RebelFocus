package com.rebelfocus.features.profiles;

import androidx.lifecycle.SavedStateHandle;
import com.rebelfocus.core.data.repository.BlockedAppRepository;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.domain.blocking.BlockingDecisionEngine;
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
public final class ProfileEditViewModel_Factory implements Factory<ProfileEditViewModel> {
  private final Provider<SavedStateHandle> savedStateHandleProvider;

  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  private final Provider<BlockedAppRepository> blockedAppRepositoryProvider;

  private final Provider<BlockingDecisionEngine> blockingDecisionEngineProvider;

  public ProfileEditViewModel_Factory(Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<BlockedAppRepository> blockedAppRepositoryProvider,
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider) {
    this.savedStateHandleProvider = savedStateHandleProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.blockedAppRepositoryProvider = blockedAppRepositoryProvider;
    this.blockingDecisionEngineProvider = blockingDecisionEngineProvider;
  }

  @Override
  public ProfileEditViewModel get() {
    return newInstance(savedStateHandleProvider.get(), profileRepositoryProvider.get(), blockedAppRepositoryProvider.get(), blockingDecisionEngineProvider.get());
  }

  public static ProfileEditViewModel_Factory create(
      Provider<SavedStateHandle> savedStateHandleProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<BlockedAppRepository> blockedAppRepositoryProvider,
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider) {
    return new ProfileEditViewModel_Factory(savedStateHandleProvider, profileRepositoryProvider, blockedAppRepositoryProvider, blockingDecisionEngineProvider);
  }

  public static ProfileEditViewModel newInstance(SavedStateHandle savedStateHandle,
      FocusProfileRepository profileRepository, BlockedAppRepository blockedAppRepository,
      BlockingDecisionEngine blockingDecisionEngine) {
    return new ProfileEditViewModel(savedStateHandle, profileRepository, blockedAppRepository, blockingDecisionEngine);
  }
}
