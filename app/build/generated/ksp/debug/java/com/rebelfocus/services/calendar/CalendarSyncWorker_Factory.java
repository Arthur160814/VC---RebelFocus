package com.rebelfocus.services.calendar;

import android.content.Context;
import androidx.work.WorkerParameters;
import com.rebelfocus.core.domain.usecase.SyncCalendarEventsUseCase;
import dagger.internal.DaggerGenerated;
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
public final class CalendarSyncWorker_Factory {
  private final Provider<SyncCalendarEventsUseCase> syncCalendarEventsUseCaseProvider;

  public CalendarSyncWorker_Factory(
      Provider<SyncCalendarEventsUseCase> syncCalendarEventsUseCaseProvider) {
    this.syncCalendarEventsUseCaseProvider = syncCalendarEventsUseCaseProvider;
  }

  public CalendarSyncWorker get(Context appContext, WorkerParameters workerParams) {
    return newInstance(appContext, workerParams, syncCalendarEventsUseCaseProvider.get());
  }

  public static CalendarSyncWorker_Factory create(
      Provider<SyncCalendarEventsUseCase> syncCalendarEventsUseCaseProvider) {
    return new CalendarSyncWorker_Factory(syncCalendarEventsUseCaseProvider);
  }

  public static CalendarSyncWorker newInstance(Context appContext, WorkerParameters workerParams,
      SyncCalendarEventsUseCase syncCalendarEventsUseCase) {
    return new CalendarSyncWorker(appContext, workerParams, syncCalendarEventsUseCase);
  }
}
