package com.rebelfocus.features.profiles;

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
public final class ProfileListViewModel_Factory implements Factory<ProfileListViewModel> {
  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  private final Provider<BlockingDecisionEngine> blockingDecisionEngineProvider;

  public ProfileListViewModel_Factory(Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider) {
    this.profileRepositoryProvider = profileRepositoryProvider;
    this.blockingDecisionEngineProvider = blockingDecisionEngineProvider;
  }

  @Override
  public ProfileListViewModel get() {
    return newInstance(profileRepositoryProvider.get(), blockingDecisionEngineProvider.get());
  }

  public static ProfileListViewModel_Factory create(
      Provider<FocusProfileRepository> profileRepositoryProvider,
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider) {
    return new ProfileListViewModel_Factory(profileRepositoryProvider, blockingDecisionEngineProvider);
  }

  public static ProfileListViewModel newInstance(FocusProfileRepository profileRepository,
      BlockingDecisionEngine blockingDecisionEngine) {
    return new ProfileListViewModel(profileRepository, blockingDecisionEngine);
  }
}
