package com.rebelfocus.core.data.repository.impl;

import com.rebelfocus.core.database.dao.BlockedAppDao;
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
public final class BlockedAppRepositoryImpl_Factory implements Factory<BlockedAppRepositoryImpl> {
  private final Provider<BlockedAppDao> blockedAppDaoProvider;

  public BlockedAppRepositoryImpl_Factory(Provider<BlockedAppDao> blockedAppDaoProvider) {
    this.blockedAppDaoProvider = blockedAppDaoProvider;
  }

  @Override
  public BlockedAppRepositoryImpl get() {
    return newInstance(blockedAppDaoProvider.get());
  }

  public static BlockedAppRepositoryImpl_Factory create(
      Provider<BlockedAppDao> blockedAppDaoProvider) {
    return new BlockedAppRepositoryImpl_Factory(blockedAppDaoProvider);
  }

  public static BlockedAppRepositoryImpl newInstance(BlockedAppDao blockedAppDao) {
    return new BlockedAppRepositoryImpl(blockedAppDao);
  }
}
