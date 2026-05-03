package com.rebelfocus.app.di;

import com.rebelfocus.core.domain.session.SessionTimingCalculator;
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
public final class SessionModule_ProvideSessionTimingCalculatorFactory implements Factory<SessionTimingCalculator> {
  @Override
  public SessionTimingCalculator get() {
    return provideSessionTimingCalculator();
  }

  public static SessionModule_ProvideSessionTimingCalculatorFactory create() {
    return InstanceHolder.INSTANCE;
  }

  public static SessionTimingCalculator provideSessionTimingCalculator() {
    return Preconditions.checkNotNullFromProvides(SessionModule.INSTANCE.provideSessionTimingCalculator());
  }

  private static final class InstanceHolder {
    private static final SessionModule_ProvideSessionTimingCalculatorFactory INSTANCE = new SessionModule_ProvideSessionTimingCalculatorFactory();
  }
}
