package com.rebelfocus.core.data.repository.impl;

import com.rebelfocus.core.database.dao.SyncStateDao;
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
public final class SyncStateRepositoryImpl_Factory implements Factory<SyncStateRepositoryImpl> {
  private final Provider<SyncStateDao> syncStateDaoProvider;

  public SyncStateRepositoryImpl_Factory(Provider<SyncStateDao> syncStateDaoProvider) {
    this.syncStateDaoProvider = syncStateDaoProvider;
  }

  @Override
  public SyncStateRepositoryImpl get() {
    return newInstance(syncStateDaoProvider.get());
  }

  public static SyncStateRepositoryImpl_Factory create(
      Provider<SyncStateDao> syncStateDaoProvider) {
    return new SyncStateRepositoryImpl_Factory(syncStateDaoProvider);
  }

  public static SyncStateRepositoryImpl newInstance(SyncStateDao syncStateDao) {
    return new SyncStateRepositoryImpl(syncStateDao);
  }
}
