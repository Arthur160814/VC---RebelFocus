package com.rebelfocus.app.di;

import com.rebelfocus.core.database.RebelFocusDatabase;
import com.rebelfocus.core.database.dao.DiagnosticLogDao;
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
public final class DatabaseModule_ProvideDiagnosticLogDaoFactory implements Factory<DiagnosticLogDao> {
  private final Provider<RebelFocusDatabase> dbProvider;

  public DatabaseModule_ProvideDiagnosticLogDaoFactory(Provider<RebelFocusDatabase> dbProvider) {
    this.dbProvider = dbProvider;
  }

  @Override
  public DiagnosticLogDao get() {
    return provideDiagnosticLogDao(dbProvider.get());
  }

  public static DatabaseModule_ProvideDiagnosticLogDaoFactory create(
      Provider<RebelFocusDatabase> dbProvider) {
    return new DatabaseModule_ProvideDiagnosticLogDaoFactory(dbProvider);
  }

  public static DiagnosticLogDao provideDiagnosticLogDao(RebelFocusDatabase db) {
    return Preconditions.checkNotNullFromProvides(DatabaseModule.INSTANCE.provideDiagnosticLogDao(db));
  }
}
