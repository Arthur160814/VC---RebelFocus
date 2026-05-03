package com.rebelfocus.core.data.repository.impl;

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
public final class SessionRepositoryImpl_Factory implements Factory<SessionRepositoryImpl> {
  private final Provider<SessionDao> sessionDaoProvider;

  public SessionRepositoryImpl_Factory(Provider<SessionDao> sessionDaoProvider) {
    this.sessionDaoProvider = sessionDaoProvider;
  }

  @Override
  public SessionRepositoryImpl get() {
    return newInstance(sessionDaoProvider.get());
  }

  public static SessionRepositoryImpl_Factory create(Provider<SessionDao> sessionDaoProvider) {
    return new SessionRepositoryImpl_Factory(sessionDaoProvider);
  }

  public static SessionRepositoryImpl newInstance(SessionDao sessionDao) {
    return new SessionRepositoryImpl(sessionDao);
  }
}
