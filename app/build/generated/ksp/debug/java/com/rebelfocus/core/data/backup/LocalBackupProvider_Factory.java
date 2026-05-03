package com.rebelfocus.core.data.backup;

import android.content.Context;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;
import javax.inject.Provider;

@ScopeMetadata
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
public final class LocalBackupProvider_Factory implements Factory<LocalBackupProvider> {
  private final Provider<Context> contextProvider;

  public LocalBackupProvider_Factory(Provider<Context> contextProvider) {
    this.contextProvider = contextProvider;
  }

  @Override
  public LocalBackupProvider get() {
    return newInstance(contextProvider.get());
  }

  public static LocalBackupProvider_Factory create(Provider<Context> contextProvider) {
    return new LocalBackupProvider_Factory(contextProvider);
  }

  public static LocalBackupProvider newInstance(Context context) {
    return new LocalBackupProvider(context);
  }
}
