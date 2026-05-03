package com.rebelfocus.features.apps;

import com.rebelfocus.core.common.apps.LauncherAppDiscovery;
import com.rebelfocus.core.data.repository.BlockedAppRepository;
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
public final class AppPickerViewModel_Factory implements Factory<AppPickerViewModel> {
  private final Provider<LauncherAppDiscovery> appDiscoveryProvider;

  private final Provider<BlockedAppRepository> blockedAppRepositoryProvider;

  private final Provider<BlockingDecisionEngine> blockingDecisionEngineProvider;

  public AppPickerViewModel_Factory(Provider<LauncherAppDiscovery> appDiscoveryProvider,
      Provider<BlockedAppRepository> blockedAppRepositoryProvider,
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider) {
    this.appDiscoveryProvider = appDiscoveryProvider;
    this.blockedAppRepositoryProvider = blockedAppRepositoryProvider;
    this.blockingDecisionEngineProvider = blockingDecisionEngineProvider;
  }

  @Override
  public AppPickerViewModel get() {
    return newInstance(appDiscoveryProvider.get(), blockedAppRepositoryProvider.get(), blockingDecisionEngineProvider.get());
  }

  public static AppPickerViewModel_Factory create(
      Provider<LauncherAppDiscovery> appDiscoveryProvider,
      Provider<BlockedAppRepository> blockedAppRepositoryProvider,
      Provider<BlockingDecisionEngine> blockingDecisionEngineProvider) {
    return new AppPickerViewModel_Factory(appDiscoveryProvider, blockedAppRepositoryProvider, blockingDecisionEngineProvider);
  }

  public static AppPickerViewModel newInstance(LauncherAppDiscovery appDiscovery,
      BlockedAppRepository blockedAppRepository, BlockingDecisionEngine blockingDecisionEngine) {
    return new AppPickerViewModel(appDiscovery, blockedAppRepository, blockingDecisionEngine);
  }
}
