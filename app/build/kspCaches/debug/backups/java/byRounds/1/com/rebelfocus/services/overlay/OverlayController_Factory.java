package com.rebelfocus.services.overlay;

import android.content.Context;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.data.repository.SessionRepository;
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
public final class OverlayController_Factory implements Factory<OverlayController> {
  private final Provider<Context> contextProvider;

  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  public OverlayController_Factory(Provider<Context> contextProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider) {
    this.contextProvider = contextProvider;
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public OverlayController get() {
    return newInstance(contextProvider.get(), sessionRepositoryProvider.get(), profileRepositoryProvider.get());
  }

  public static OverlayController_Factory create(Provider<Context> contextProvider,
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider) {
    return new OverlayController_Factory(contextProvider, sessionRepositoryProvider, profileRepositoryProvider);
  }

  public static OverlayController newInstance(Context context, SessionRepository sessionRepository,
      FocusProfileRepository profileRepository) {
    return new OverlayController(context, sessionRepository, profileRepository);
  }
}
