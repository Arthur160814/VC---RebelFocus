package com.rebelfocus.app.di;

import com.rebelfocus.core.domain.session.SessionStateMachine;
import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.Preconditions;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class SessionModule_ProvideSessionStateMachineFactory implements Factory<SessionStateMachine> {
  @Override
  public SessionStateMachine get() {
    return provideSessionStateMachine();
  }

  public static SessionModule_ProvideSessionStateMachineFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SessionStateMachine provideSessionStateMachine() {
    return Preconditions.checkNotNullFromProvides(SessionModule.INSTANCE.provideSessionStateMachine());
  }

  private static final class InstanceHolder {
    private static final SessionModule_ProvideSessionStateMachineFactory INSTANCE = new SessionModule_ProvideSessionStateMachineFactory();
  }
}
