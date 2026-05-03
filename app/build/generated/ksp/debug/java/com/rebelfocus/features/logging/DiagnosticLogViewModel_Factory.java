package com.rebelfocus.features.logging;

import com.rebelfocus.core.database.dao.DiagnosticLogDao;
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
public final class DiagnosticLogViewModel_Factory implements Factory<DiagnosticLogViewModel> {
  private final Provider<DiagnosticLogDao> diagnosticLogDaoProvider;

  public DiagnosticLogViewModel_Factory(Provider<DiagnosticLogDao> diagnosticLogDaoProvider) {
    this.diagnosticLogDaoProvider = diagnosticLogDaoProvider;
  }

  @Override
  public DiagnosticLogViewModel get() {
    return newInstance(diagnosticLogDaoProvider.get());
  }

  public static DiagnosticLogViewModel_Factory create(
      Provider<DiagnosticLogDao> diagnosticLogDaoProvider) {
    return new DiagnosticLogViewModel_Factory(diagnosticLogDaoProvider);
  }

  public static DiagnosticLogViewModel newInstance(DiagnosticLogDao diagnosticLogDao) {
    return new DiagnosticLogViewModel(diagnosticLogDao);
  }
}
