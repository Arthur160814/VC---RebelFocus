package com.rebelfocus.core.domain.usecase;

import com.rebelfocus.core.data.datastore.UserPreferencesDataStore;
import com.rebelfocus.core.database.dao.SessionDao;
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
public final class GetFocusStatsUseCase_Factory implements Factory<GetFocusStatsUseCase> {
  private final Provider<SessionDao> sessionDaoProvider;

  private final Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider;

  public GetFocusStatsUseCase_Factory(Provider<SessionDao> sessionDaoProvider,
      Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
    this.userPreferencesDataStoreProvider = userPreferencesDataStoreProvider;
  }

  @Override
  public GetFocusStatsUseCase get() {
    return newInstance(sessionDaoProvider.get(), userPreferencesDataStoreProvider.get());
  }

  public static GetFocusStatsUseCase_Factory create(Provider<SessionDao> sessionDaoProvider,
      Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider) {
    return new GetFocusStatsUseCase_Factory(sessionDaoProvider, userPreferencesDataStoreProvider);
  }

  public static GetFocusStatsUseCase newInstance(SessionDao sessionDao,
      UserPreferencesDataStore userPreferencesDataStore) {
    return new GetFocusStatsUseCase(sessionDao, userPreferencesDataStore);
  }
}
