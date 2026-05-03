package com.rebelfocus.app.di;

import com.rebelfocus.core.database.RebelFocusDatabase;
import com.rebelfocus.core.database.dao.RestoreDao;
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
public final class DatabaseModule_ProvideRestoreDaoFactory implements Factory<RestoreDao> {
  private final Provider<RebelFocusDatabase> dbProvider;

  public DatabaseModule_ProvideRestoreDaoFactory(Provider<RebelFocusDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public RestoreDao get() {
    return provideRestoreDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideRestoreDaoFactory create(
      Provider<RebelFocusDatabase> dbProvider) {
    return new DatabaseModule_ProvideRestoreDaoFactory(dbProvider);
  }

  public static RestoreDao provideRestoreDao(RebelFocusDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideRestoreDao(db));
  }
}
