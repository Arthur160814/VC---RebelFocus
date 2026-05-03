package com.rebelfocus.core.data.repository.impl;

import com.rebelfocus.core.database.dao.AuditEventDao;
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
public final class AuditEventRepositoryImpl_Factory implements Factory<AuditEventRepositoryImpl> {
  private final Provider<AuditEventDao> auditEventDaoProvider;

  public AuditEventRepositoryImpl_Factory(Provider<AuditEventDao> auditEventDaoProvider) {
    this.auditEventDaoProvider = auditEventDaoProvider;
  }

  @Override
  public AuditEventRepositoryImpl get() {
    return newInstance(auditEventDaoProvider.get());
  }

  public static AuditEventRepositoryImpl_Factory create(
      Provider<AuditEventDao> auditEventDaoProvider) {
    return new AuditEventRepositoryImpl_Factory(auditEventDaoProvider);
  }

  public static AuditEventRepositoryImpl newInstance(AuditEventDao auditEventDao) {
    return new AuditEventRepositoryImpl(auditEventDao);
  }
}
