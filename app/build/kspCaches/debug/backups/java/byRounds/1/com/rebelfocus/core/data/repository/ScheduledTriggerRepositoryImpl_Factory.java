package com.rebelfocus.core.data.repository;

import com.rebelfocus.core.database.dao.ScheduledTriggerDao;
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
public final class ScheduledTriggerRepositoryImpl_Factory implements Factory<ScheduledTriggerRepositoryImpl> {
  private final Provider<ScheduledTriggerDao> daoProvider;

  public ScheduledTriggerRepositoryImpl_Factory(Provider<ScheduledTriggerDao> daoProvider) {
    this.daoProvider = daoProvider;
  }

  @Override
  public ScheduledTriggerRepositoryImpl get() {
    return newInstance(daoProvider.get());
  }

  public static ScheduledTriggerRepositoryImpl_Factory create(
      Provider<ScheduledTriggerDao> daoProvider) {
    return new ScheduledTriggerRepositoryImpl_Factory(daoProvider);
  }

  public static ScheduledTriggerRepositoryImpl newInstance(ScheduledTriggerDao dao) {
    return new ScheduledTriggerRepositoryImpl(dao);
  }
}
