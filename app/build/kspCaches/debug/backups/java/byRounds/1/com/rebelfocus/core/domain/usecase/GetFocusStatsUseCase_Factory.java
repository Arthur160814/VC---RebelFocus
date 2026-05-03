package com.rebelfocus.core.domain.usecase;

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

  public GetFocusStatsUseCase_Factory(Provider<SessionDao> sessionDaoProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
  }

  @Override
  public GetFocusStatsUseCase get() {
    return newInstance(sessionDaoProvider.get());
  }

  public static GetFocusStatsUseCase_Factory create(Provider<SessionDao> sessionDaoProvider) {
    return new GetFocusStatsUseCase_Factory(sessionDaoProvider);
  }

  public static GetFocusStatsUseCase newInstance(SessionDao sessionDao) {
    return new GetFocusStatsUseCase(sessionDao);
  }
}
