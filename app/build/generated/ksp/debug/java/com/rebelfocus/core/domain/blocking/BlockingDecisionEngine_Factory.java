package com.rebelfocus.core.domain.blocking;

import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.data.repository.SessionRepository;
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
public final class BlockingDecisionEngine_Factory implements Factory<BlockingDecisionEngine> {
  private final Provider<SessionRepository> sessionRepositoryProvider;

  private final Provider<FocusProfileRepository> profileRepositoryProvider;

  public BlockingDecisionEngine_Factory(Provider<SessionRepository> sessionRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider) {
    this.sessionRepositoryProvider = sessionRepositoryProvider;
    this.profileRepositoryProvider = profileRepositoryProvider;
  }

  @Override
  public BlockingDecisionEngine get() {
    return newInstance(sessionRepositoryProvider.get(), profileRepositoryProvider.get());
  }

  public static BlockingDecisionEngine_Factory create(
      Provider<SessionRepository> sessionRepositoryProvider,
      Provider<FocusProfileRepository> profileRepositoryProvider) {
    return new BlockingDecisionEngine_Factory(sessionRepositoryProvider, profileRepositoryProvider);
  }

  public static BlockingDecisionEngine newInstance(SessionRepository sessionRepository,
      FocusProfileRepository profileRepository) {
    return new BlockingDecisionEngine(sessionRepository, profileRepository);
  }
}
