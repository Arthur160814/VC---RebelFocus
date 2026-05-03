package com.rebelfocus.core.domain.scheduling;

import dagger.internal.DaggerGenerated;
import dagger.internal.Factory;
import dagger.internal.QualifierMetadata;
import dagger.internal.ScopeMetadata;
import javax.annotation.processing.Generated;

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
public final class NextOccurrenceCalculator_Factory implements Factory<NextOccurrenceCalculator> {
  @Override
  public NextOccurrenceCalculator get() {
    return newInstance();
  }

  public static NextOccurrenceCalculator_Factory create() {
    return InstanceHolder.INSTANCE;
  }

  public static NextOccurrenceCalculator newInstance() {
    return new NextOccurrenceCalculator();
  }

  private static final class InstanceHolder {
    private static final NextOccurrenceCalculator_Factory INSTANCE = new NextOccurrenceCalculator_Factory();
  }
}
