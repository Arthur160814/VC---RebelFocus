package com.rebelfocus.features.stats;

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

  public StatsViewModel_Factory(Provider<GetFocusStatsUseCase> getFocusStatsUseCaseProvider) {
    this.getFocusStatsUseCaseProvider = getFocusStatsUseCaseProvider;
  }

  @Override
  public StatsViewModel get() {
    return newInstance(getFocusStatsUseCaseProvider.get());
  }

  public static StatsViewModel_Factory create(
      Provider<GetFocusStatsUseCase> getFocusStatsUseCaseProvider) {
    return new StatsViewModel_Factory(getFocusStatsUseCaseProvider);
  }

  public static StatsViewModel newInstance(GetFocusStatsUseCase getFocusStatsUseCase) {
    return new StatsViewModel(getFocusStatsUseCase);
  }
}
