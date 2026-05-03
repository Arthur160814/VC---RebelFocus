package com.rebelfocus.core.common.permissions;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata("javax.inject.Singleton")
@QualifierMetadata("dagger.hilt.android.qualifiers.ApplicationContext")
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
public final class PermissionChecker_Factory implements Factory<PermissionChecker> {
  private final Provider<Context> contextProvider;

  public PermissionChecker_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public PermissionChecker get() {
    return newInstance(contextProvider.get());
  }

  public static PermissionChecker_Factory create(Provider<Context> contextProvider) {
    return new PermissionChecker_Factory(contextProvider);
  }

  public static PermissionChecker newInstance(Context context) {
    return new PermissionChecker(context);
  }
}
