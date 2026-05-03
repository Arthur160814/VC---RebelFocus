package com.rebelfocus.core.data.repository.impl;

import com.rebelfocus.core.database.dao.FocusProfileDao;
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
public final class FocusProfileRepositoryImpl_Factory implements Factory<FocusProfileRepositoryImpl> {
  private final Provider<FocusProfileDao> focusProfileDaoProvider;

  public FocusProfileRepositoryImpl_Factory(Provider<FocusProfileDao> focusProfileDaoProvider) {
    this.focusProfileDaoProvider = focusProfileDaoProvider;
  }

  @Override
  public FocusProfileRepositoryImpl get() {
    return newInstance(focusProfileDaoProvider.get());
  }

  public static FocusProfileRepositoryImpl_Factory create(
      Provider<FocusProfileDao> focusProfileDaoProvider) {
    return new FocusProfileRepositoryImpl_Factory(focusProfileDaoProvider);
  }

  public static FocusProfileRepositoryImpl newInstance(FocusProfileDao focusProfileDao) {
    return new FocusProfileRepositoryImpl(focusProfileDao);
  }
}
