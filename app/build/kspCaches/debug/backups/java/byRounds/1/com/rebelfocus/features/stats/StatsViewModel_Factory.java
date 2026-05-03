package com.rebelfocus.features.stats;

import com.rebelfocus.core.data.datastore.UserPreferencesDataStore;
import com.rebelfocus.core.domain.usecase.GetFocusStatsUseCase;
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
public final class StatsViewModel_Factory implements Factory<StatsViewModel> {
  private final Provider<GetFocusStatsUseCase> getFocusStatsUseCaseProvider;

  private final Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider;

  public StatsViewModel_Factory(Provider<GetFocusStatsUseCase> getFocusStatsUseCaseProvider,
      Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider) {
    this.getFocusStatsUseCaseProvider = getFocusStatsUseCaseProvider;
    this.userPreferencesDataStoreProvider = userPreferencesDataStoreProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(getFocusStatsUseCaseProvider.get(), userPreferencesDataStoreProvider.get());
  }

  public static StatsViewModel_Factory create(
      Provider<GetFocusStatsUseCase> getFocusStatsUseCaseProvider,
      Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider) {
    return new StatsViewModel_Factory(getFocusStatsUseCaseProvider, userPreferencesDataStoreProvider);
  }

  public static StatsViewModel newInstance(GetFocusStatsUseCase getFocusStatsUseCase,
      UserPreferencesDataStore userPreferencesDataStore) {
    return new StatsViewModel(getFocusStatsUseCase, userPreferencesDataStore);
  }
}
