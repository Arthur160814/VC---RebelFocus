package com.rebelfocus.core.data.repository.impl;

import com.rebelfocus.core.database.dao.UserProgressDao;
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
public final class UserProgressRepositoryImpl_Factory implements Factory<UserProgressRepositoryImpl> {
  private final Provider<UserProgressDao> userProgressDaoProvider;

  public UserProgressRepositoryImpl_Factory(Provider<UserProgressDao> userProgressDaoProvider) {
    this.userProgressDaoProvider = userProgressDaoProvider;
  }

  @Override
  public UserProgressRepositoryImpl get() {
    return newInstance(userProgressDaoProvider.get());
  }

  public static UserProgressRepositoryImpl_Factory create(
      Provider<UserProgressDao> userProgressDaoProvider) {
    return new UserProgressRepositoryImpl_Factory(userProgressDaoProvider);
  }

  public static UserProgressRepositoryImpl newInstance(UserProgressDao userProgressDao) {
    return new UserProgressRepositoryImpl(userProgressDao);
  }
}
