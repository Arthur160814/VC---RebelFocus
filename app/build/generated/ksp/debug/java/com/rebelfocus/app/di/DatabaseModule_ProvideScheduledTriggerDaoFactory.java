package com.rebelfocus.app.di;

import com.rebelfocus.core.database.RebelFocusDatabase;
import com.rebelfocus.core.database.dao.ScheduledTriggerDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
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
public final class DatabaseModule_ProvideScheduledTriggerDaoFactory implements Factory<ScheduledTriggerDao> {
  private final Provider<RebelFocusDatabase> dbProvider;

  public DatabaseModule_ProvideScheduledTriggerDaoFactory(Provider<RebelFocusDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public ScheduledTriggerDao get() {
    return provideScheduledTriggerDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideScheduledTriggerDaoFactory create(
      Provider<RebelFocusDatabase> dbProvider) {
    return new DatabaseModule_ProvideScheduledTriggerDaoFactory(dbProvider);
  }

  public static ScheduledTriggerDao provideScheduledTriggerDao(RebelFocusDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideScheduledTriggerDao(db));
  }
}
