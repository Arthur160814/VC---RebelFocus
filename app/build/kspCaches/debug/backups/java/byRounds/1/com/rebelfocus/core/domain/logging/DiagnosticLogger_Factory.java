package com.rebelfocus.core.domain.logging;

import com.rebelfocus.core.database.dao.DiagnosticLogDao;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
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
public final class DiagnosticLogger_Factory implements Factory<DiagnosticLogger> {
  private final Provider<DiagnosticLogDao> diagnosticLogDaoProvider;

  public DiagnosticLogger_Factory(Provider<DiagnosticLogDao> diagnosticLogDaoProvider) {
    this.diagnosticLogDaoProvider = diagnosticLogDaoProvider;
  }

  @Override
  public DiagnosticLogger get() {
    return newInstance(diagnosticLogDaoProvider.get());
  }

  public static DiagnosticLogger_Factory create(
      Provider<DiagnosticLogDao> diagnosticLogDaoProvider) {
    return new DiagnosticLogger_Factory(diagnosticLogDaoProvider);
  }

  public static DiagnosticLogger newInstance(DiagnosticLogDao diagnosticLogDao) {
    return new DiagnosticLogger(diagnosticLogDao);
  }
}
