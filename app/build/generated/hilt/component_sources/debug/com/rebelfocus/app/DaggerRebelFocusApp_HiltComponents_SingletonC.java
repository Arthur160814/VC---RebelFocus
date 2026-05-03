package com.rebelfocus.app;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.view.View;
import androidx.fragment.app.Fragment;
import androidx.hilt.work.HiltWorkerFactory;
import androidx.hilt.work.WorkerAssistedFactory;
import androidx.hilt.work.WorkerFactoryModule_ProvideFactoryFactory;
import androidx.lifecycle.SavedStateHandle;
import androidx.lifecycle.ViewModel;
import androidx.work.ListenableWorker;
import androidx.work.WorkerParameters;
import com.rebelfocus.app.di.DatabaseModule_ProvideAuditEventDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideAutomationRuleDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideBlockedAppDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideDatabaseFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideDiagnosticLogDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideFocusProfileDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideRestoreDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideScheduledTriggerDaoFactory;
import com.rebelfocus.app.di.DatabaseModule_ProvideSessionDaoFactory;
import com.rebelfocus.app.di.SessionModule_ProvideSessionStateMachineFactory;
import com.rebelfocus.app.di.SessionModule_ProvideSessionTimingCalculatorFactory;
import com.rebelfocus.app.di.SessionModule_ProvideTimeSourceFactory;
import com.rebelfocus.core.common.apps.LauncherAppDiscovery;
import com.rebelfocus.core.common.permissions.PermissionChecker;
import com.rebelfocus.core.data.backup.LocalBackupProvider;
import com.rebelfocus.core.data.datastore.UserPreferencesDataStore;
import com.rebelfocus.core.data.repository.AuditEventRepository;
import com.rebelfocus.core.data.repository.AutomationRuleRepository;
import com.rebelfocus.core.data.repository.BlockedAppRepository;
import com.rebelfocus.core.data.repository.FocusProfileRepository;
import com.rebelfocus.core.data.repository.ScheduledTriggerRepository;
import com.rebelfocus.core.data.repository.ScheduledTriggerRepositoryImpl;
import com.rebelfocus.core.data.repository.SessionRepository;
import com.rebelfocus.core.data.repository.impl.AuditEventRepositoryImpl;
import com.rebelfocus.core.data.repository.impl.AutomationRuleRepositoryImpl;
import com.rebelfocus.core.data.repository.impl.BlockedAppRepositoryImpl;
import com.rebelfocus.core.data.repository.impl.FocusProfileRepositoryImpl;
import com.rebelfocus.core.data.repository.impl.SessionRepositoryImpl;
import com.rebelfocus.core.database.RebelFocusDatabase;
import com.rebelfocus.core.database.dao.AuditEventDao;
import com.rebelfocus.core.database.dao.AutomationRuleDao;
import com.rebelfocus.core.database.dao.BlockedAppDao;
import com.rebelfocus.core.database.dao.DiagnosticLogDao;
import com.rebelfocus.core.database.dao.FocusProfileDao;
import com.rebelfocus.core.database.dao.RestoreDao;
import com.rebelfocus.core.database.dao.ScheduledTriggerDao;
import com.rebelfocus.core.database.dao.SessionDao;
import com.rebelfocus.core.domain.blocking.BlockingDecisionEngine;
import com.rebelfocus.core.domain.logging.DiagnosticLogger;
import com.rebelfocus.core.domain.scheduling.NextOccurrenceCalculator;
import com.rebelfocus.core.domain.session.SessionEngine;
import com.rebelfocus.core.domain.session.SessionStateMachine;
import com.rebelfocus.core.domain.session.SessionTimingCalculator;
import com.rebelfocus.core.domain.session.TimeSource;
import com.rebelfocus.core.domain.usecase.CancelScheduledSessionUseCase;
import com.rebelfocus.core.domain.usecase.ExecuteScheduleUseCase;
import com.rebelfocus.core.domain.usecase.ExecuteScheduledTriggerUseCase;
import com.rebelfocus.core.domain.usecase.ExportBackupUseCase;
import com.rebelfocus.core.domain.usecase.GetFocusStatsUseCase;
import com.rebelfocus.core.domain.usecase.ImportBackupUseCase;
import com.rebelfocus.core.domain.usecase.PauseSessionUseCase;
import com.rebelfocus.core.domain.usecase.ResumeSessionUseCase;
import com.rebelfocus.core.domain.usecase.ScheduleSessionUseCase;
import com.rebelfocus.core.domain.usecase.StartFocusSessionUseCase;
import com.rebelfocus.core.domain.usecase.StopFocusSessionUseCase;
import com.rebelfocus.core.domain.usecase.SyncCalendarEventsUseCase;
import com.rebelfocus.core.domain.usecase.TransitionToActiveFocusUseCase;
import com.rebelfocus.core.domain.usecase.TransitionToBreakUseCase;
import com.rebelfocus.features.apps.AppPickerViewModel;
import com.rebelfocus.features.apps.AppPickerViewModel_HiltModules;
import com.rebelfocus.features.automation.AutomationEditViewModel;
import com.rebelfocus.features.automation.AutomationEditViewModel_HiltModules;
import com.rebelfocus.features.automation.AutomationListViewModel;
import com.rebelfocus.features.automation.AutomationListViewModel_HiltModules;
import com.rebelfocus.features.backup.BackupViewModel;
import com.rebelfocus.features.backup.BackupViewModel_HiltModules;
import com.rebelfocus.features.dashboard.DashboardViewModel;
import com.rebelfocus.features.dashboard.DashboardViewModel_HiltModules;
import com.rebelfocus.features.logging.DiagnosticLogViewModel;
import com.rebelfocus.features.logging.DiagnosticLogViewModel_HiltModules;
import com.rebelfocus.features.onboarding.OnboardingViewModel;
import com.rebelfocus.features.onboarding.OnboardingViewModel_HiltModules;
import com.rebelfocus.features.profiles.ProfileEditViewModel;
import com.rebelfocus.features.profiles.ProfileEditViewModel_HiltModules;
import com.rebelfocus.features.profiles.ProfileListViewModel;
import com.rebelfocus.features.profiles.ProfileListViewModel_HiltModules;
import com.rebelfocus.features.stats.StatsViewModel;
import com.rebelfocus.features.stats.StatsViewModel_HiltModules;
import com.rebelfocus.services.accessibility.RebelFocusAccessibilityService;
import com.rebelfocus.services.accessibility.RebelFocusAccessibilityService_MembersInjector;
import com.rebelfocus.services.calendar.CalendarSyncWorker;
import com.rebelfocus.services.calendar.CalendarSyncWorker_AssistedFactory;
import com.rebelfocus.services.calendar.DeviceCalendarSource;
import com.rebelfocus.services.foreground.FocusSessionOrchestrator;
import com.rebelfocus.services.foreground.FocusSessionService;
import com.rebelfocus.services.foreground.FocusSessionService_MembersInjector;
import com.rebelfocus.services.foreground.NotificationHelper;
import com.rebelfocus.services.foreground.ServiceActionReceiver;
import com.rebelfocus.services.foreground.ServiceActionReceiver_MembersInjector;
import com.rebelfocus.services.overlay.OverlayController;
import com.rebelfocus.services.scheduling.RestoreSchedulesWorker;
import com.rebelfocus.services.scheduling.RestoreSchedulesWorker_AssistedFactory;
import com.rebelfocus.services.scheduling.ScheduleAlarmReceiver;
import com.rebelfocus.services.scheduling.ScheduleAlarmReceiver_MembersInjector;
import com.rebelfocus.services.scheduling.SystemAlarmScheduler;
import com.rebelfocus.services.scheduling.TimeChangeReceiver;
import com.rebelfocus.services.scheduling.TimeChangeReceiver_MembersInjector;
import dagger.hilt.android.ActivityRetainedLifecycle;
import dagger.hilt.android.ViewModelLifecycle;
import dagger.hilt.android.internal.builders.ActivityComponentBuilder;
import dagger.hilt.android.internal.builders.ActivityRetainedComponentBuilder;
import dagger.hilt.android.internal.builders.FragmentComponentBuilder;
import dagger.hilt.android.internal.builders.ServiceComponentBuilder;
import dagger.hilt.android.internal.builders.ViewComponentBuilder;
import dagger.hilt.android.internal.builders.ViewModelComponentBuilder;
import dagger.hilt.android.internal.builders.ViewWithFragmentComponentBuilder;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories;
import dagger.hilt.android.internal.lifecycle.DefaultViewModelFactories_InternalFactoryFactory_Factory;
import dagger.hilt.android.internal.managers.ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory;
import dagger.hilt.android.internal.managers.SavedStateHandleHolder;
import dagger.hilt.android.internal.modules.ApplicationContextModule;
import dagger.hilt.android.internal.modules.ApplicationContextModule_ProvideContextFactory;
import dagger.internal.DaggerGenerated;
import dagger.internal.DoubleCheck;
import dagger.internal.IdentifierNameString;
import dagger.internal.KeepFieldType;
import dagger.internal.LazyClassKeyMap;
import dagger.internal.MapBuilder;
import dagger.internal.Preconditions;
import dagger.internal.Provider;
import dagger.internal.SingleCheck;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import javax.annotation.processing.Generated;

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
public final class DaggerRebelFocusApp_HiltComponents_SingletonC {
  private DaggerRebelFocusApp_HiltComponents_SingletonC() {
  }

  public static Builder builder() {
    return new Builder();
  }

  public static final class Builder {
    private ApplicationContextModule applicationContextModule;

    private Builder() {
    }

    public Builder applicationContextModule(ApplicationContextModule applicationContextModule) {
      this.applicationContextModule = Preconditions.checkNotNull(applicationContextModule);
      return this;
    }

    public RebelFocusApp_HiltComponents.SingletonC build() {
      Preconditions.checkBuilderRequirement(applicationContextModule, ApplicationContextModule.class);
      return new SingletonCImpl(applicationContextModule);
    }
  }

  private static final class ActivityRetainedCBuilder implements RebelFocusApp_HiltComponents.ActivityRetainedC.Builder {
    private final SingletonCImpl singletonCImpl;

    private SavedStateHandleHolder savedStateHandleHolder;

    private ActivityRetainedCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ActivityRetainedCBuilder savedStateHandleHolder(
        SavedStateHandleHolder savedStateHandleHolder) {
      this.savedStateHandleHolder = Preconditions.checkNotNull(savedStateHandleHolder);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.ActivityRetainedC build() {
      Preconditions.checkBuilderRequirement(savedStateHandleHolder, SavedStateHandleHolder.class);
      return new ActivityRetainedCImpl(singletonCImpl, savedStateHandleHolder);
    }
  }

  private static final class ActivityCBuilder implements RebelFocusApp_HiltComponents.ActivityC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private Activity activity;

    private ActivityCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ActivityCBuilder activity(Activity activity) {
      this.activity = Preconditions.checkNotNull(activity);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.ActivityC build() {
      Preconditions.checkBuilderRequirement(activity, Activity.class);
      return new ActivityCImpl(singletonCImpl, activityRetainedCImpl, activity);
    }
  }

  private static final class FragmentCBuilder implements RebelFocusApp_HiltComponents.FragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private Fragment fragment;

    private FragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public FragmentCBuilder fragment(Fragment fragment) {
      this.fragment = Preconditions.checkNotNull(fragment);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.FragmentC build() {
      Preconditions.checkBuilderRequirement(fragment, Fragment.class);
      return new FragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragment);
    }
  }

  private static final class ViewWithFragmentCBuilder implements RebelFocusApp_HiltComponents.ViewWithFragmentC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private View view;

    private ViewWithFragmentCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;
    }

    @Override
    public ViewWithFragmentCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.ViewWithFragmentC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewWithFragmentCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl, view);
    }
  }

  private static final class ViewCBuilder implements RebelFocusApp_HiltComponents.ViewC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private View view;

    private ViewCBuilder(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
    }

    @Override
    public ViewCBuilder view(View view) {
      this.view = Preconditions.checkNotNull(view);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.ViewC build() {
      Preconditions.checkBuilderRequirement(view, View.class);
      return new ViewCImpl(singletonCImpl, activityRetainedCImpl, activityCImpl, view);
    }
  }

  private static final class ViewModelCBuilder implements RebelFocusApp_HiltComponents.ViewModelC.Builder {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private SavedStateHandle savedStateHandle;

    private ViewModelLifecycle viewModelLifecycle;

    private ViewModelCBuilder(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
    }

    @Override
    public ViewModelCBuilder savedStateHandle(SavedStateHandle handle) {
      this.savedStateHandle = Preconditions.checkNotNull(handle);
      return this;
    }

    @Override
    public ViewModelCBuilder viewModelLifecycle(ViewModelLifecycle viewModelLifecycle) {
      this.viewModelLifecycle = Preconditions.checkNotNull(viewModelLifecycle);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.ViewModelC build() {
      Preconditions.checkBuilderRequirement(savedStateHandle, SavedStateHandle.class);
      Preconditions.checkBuilderRequirement(viewModelLifecycle, ViewModelLifecycle.class);
      return new ViewModelCImpl(singletonCImpl, activityRetainedCImpl, savedStateHandle, viewModelLifecycle);
    }
  }

  private static final class ServiceCBuilder implements RebelFocusApp_HiltComponents.ServiceC.Builder {
    private final SingletonCImpl singletonCImpl;

    private Service service;

    private ServiceCBuilder(SingletonCImpl singletonCImpl) {
      this.singletonCImpl = singletonCImpl;
    }

    @Override
    public ServiceCBuilder service(Service service) {
      this.service = Preconditions.checkNotNull(service);
      return this;
    }

    @Override
    public RebelFocusApp_HiltComponents.ServiceC build() {
      Preconditions.checkBuilderRequirement(service, Service.class);
      return new ServiceCImpl(singletonCImpl, service);
    }
  }

  private static final class ViewWithFragmentCImpl extends RebelFocusApp_HiltComponents.ViewWithFragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl;

    private final ViewWithFragmentCImpl viewWithFragmentCImpl = this;

    private ViewWithFragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        FragmentCImpl fragmentCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;
      this.fragmentCImpl = fragmentCImpl;


    }
  }

  private static final class FragmentCImpl extends RebelFocusApp_HiltComponents.FragmentC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final FragmentCImpl fragmentCImpl = this;

    private FragmentCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, ActivityCImpl activityCImpl,
        Fragment fragmentParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return activityCImpl.getHiltInternalFactoryFactory();
    }

    @Override
    public ViewWithFragmentComponentBuilder viewWithFragmentComponentBuilder() {
      return new ViewWithFragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl, fragmentCImpl);
    }
  }

  private static final class ViewCImpl extends RebelFocusApp_HiltComponents.ViewC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl;

    private final ViewCImpl viewCImpl = this;

    private ViewCImpl(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
        ActivityCImpl activityCImpl, View viewParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.activityCImpl = activityCImpl;


    }
  }

  private static final class ActivityCImpl extends RebelFocusApp_HiltComponents.ActivityC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ActivityCImpl activityCImpl = this;

    private ActivityCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, Activity activityParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;


    }

    @Override
    public void injectMainActivity(MainActivity mainActivity) {
      injectMainActivity2(mainActivity);
    }

    @Override
    public DefaultViewModelFactories.InternalFactoryFactory getHiltInternalFactoryFactory() {
      return DefaultViewModelFactories_InternalFactoryFactory_Factory.newInstance(getViewModelKeys(), new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl));
    }

    @Override
    public Map<Class<?>, Boolean> getViewModelKeys() {
      return LazyClassKeyMap.<Boolean>of(MapBuilder.<String, Boolean>newMapBuilder(10).put(LazyClassKeyProvider.com_rebelfocus_features_apps_AppPickerViewModel, AppPickerViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_automation_AutomationEditViewModel, AutomationEditViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_automation_AutomationListViewModel, AutomationListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_backup_BackupViewModel, BackupViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_dashboard_DashboardViewModel, DashboardViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_logging_DiagnosticLogViewModel, DiagnosticLogViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_onboarding_OnboardingViewModel, OnboardingViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_profiles_ProfileEditViewModel, ProfileEditViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_profiles_ProfileListViewModel, ProfileListViewModel_HiltModules.KeyModule.provide()).put(LazyClassKeyProvider.com_rebelfocus_features_stats_StatsViewModel, StatsViewModel_HiltModules.KeyModule.provide()).build());
    }

    @Override
    public ViewModelComponentBuilder getViewModelComponentBuilder() {
      return new ViewModelCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public FragmentComponentBuilder fragmentComponentBuilder() {
      return new FragmentCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    @Override
    public ViewComponentBuilder viewComponentBuilder() {
      return new ViewCBuilder(singletonCImpl, activityRetainedCImpl, activityCImpl);
    }

    private MainActivity injectMainActivity2(MainActivity instance) {
      MainActivity_MembersInjector.injectPreferencesDataStore(instance, singletonCImpl.userPreferencesDataStoreProvider.get());
      MainActivity_MembersInjector.injectFocusSessionOrchestrator(instance, singletonCImpl.focusSessionOrchestratorProvider.get());
      MainActivity_MembersInjector.injectNotificationHelper(instance, singletonCImpl.notificationHelperProvider.get());
      return instance;
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_rebelfocus_features_automation_AutomationEditViewModel = "com.rebelfocus.features.automation.AutomationEditViewModel";

      static String com_rebelfocus_features_dashboard_DashboardViewModel = "com.rebelfocus.features.dashboard.DashboardViewModel";

      static String com_rebelfocus_features_onboarding_OnboardingViewModel = "com.rebelfocus.features.onboarding.OnboardingViewModel";

      static String com_rebelfocus_features_automation_AutomationListViewModel = "com.rebelfocus.features.automation.AutomationListViewModel";

      static String com_rebelfocus_features_backup_BackupViewModel = "com.rebelfocus.features.backup.BackupViewModel";

      static String com_rebelfocus_features_apps_AppPickerViewModel = "com.rebelfocus.features.apps.AppPickerViewModel";

      static String com_rebelfocus_features_profiles_ProfileListViewModel = "com.rebelfocus.features.profiles.ProfileListViewModel";

      static String com_rebelfocus_features_logging_DiagnosticLogViewModel = "com.rebelfocus.features.logging.DiagnosticLogViewModel";

      static String com_rebelfocus_features_profiles_ProfileEditViewModel = "com.rebelfocus.features.profiles.ProfileEditViewModel";

      static String com_rebelfocus_features_stats_StatsViewModel = "com.rebelfocus.features.stats.StatsViewModel";

      @KeepFieldType
      AutomationEditViewModel com_rebelfocus_features_automation_AutomationEditViewModel2;

      @KeepFieldType
      DashboardViewModel com_rebelfocus_features_dashboard_DashboardViewModel2;

      @KeepFieldType
      OnboardingViewModel com_rebelfocus_features_onboarding_OnboardingViewModel2;

      @KeepFieldType
      AutomationListViewModel com_rebelfocus_features_automation_AutomationListViewModel2;

      @KeepFieldType
      BackupViewModel com_rebelfocus_features_backup_BackupViewModel2;

      @KeepFieldType
      AppPickerViewModel com_rebelfocus_features_apps_AppPickerViewModel2;

      @KeepFieldType
      ProfileListViewModel com_rebelfocus_features_profiles_ProfileListViewModel2;

      @KeepFieldType
      DiagnosticLogViewModel com_rebelfocus_features_logging_DiagnosticLogViewModel2;

      @KeepFieldType
      ProfileEditViewModel com_rebelfocus_features_profiles_ProfileEditViewModel2;

      @KeepFieldType
      StatsViewModel com_rebelfocus_features_stats_StatsViewModel2;
    }
  }

  private static final class ViewModelCImpl extends RebelFocusApp_HiltComponents.ViewModelC {
    private final SavedStateHandle savedStateHandle;

    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl;

    private final ViewModelCImpl viewModelCImpl = this;

    private Provider<AppPickerViewModel> appPickerViewModelProvider;

    private Provider<AutomationEditViewModel> automationEditViewModelProvider;

    private Provider<AutomationListViewModel> automationListViewModelProvider;

    private Provider<BackupViewModel> backupViewModelProvider;

    private Provider<DashboardViewModel> dashboardViewModelProvider;

    private Provider<DiagnosticLogViewModel> diagnosticLogViewModelProvider;

    private Provider<OnboardingViewModel> onboardingViewModelProvider;

    private Provider<ProfileEditViewModel> profileEditViewModelProvider;

    private Provider<ProfileListViewModel> profileListViewModelProvider;

    private Provider<StatsViewModel> statsViewModelProvider;

    private ViewModelCImpl(SingletonCImpl singletonCImpl,
        ActivityRetainedCImpl activityRetainedCImpl, SavedStateHandle savedStateHandleParam,
        ViewModelLifecycle viewModelLifecycleParam) {
      this.singletonCImpl = singletonCImpl;
      this.activityRetainedCImpl = activityRetainedCImpl;
      this.savedStateHandle = savedStateHandleParam;
      initialize(savedStateHandleParam, viewModelLifecycleParam);

    }

    private CancelScheduledSessionUseCase cancelScheduledSessionUseCase() {
      return new CancelScheduledSessionUseCase(singletonCImpl.systemAlarmSchedulerProvider.get());
    }

    private ExportBackupUseCase exportBackupUseCase() {
      return new ExportBackupUseCase(singletonCImpl.focusProfileDao(), singletonCImpl.blockedAppDao(), singletonCImpl.automationRuleDao(), singletonCImpl.provideDatabaseProvider.get(), singletonCImpl.localBackupProvider());
    }

    private ImportBackupUseCase importBackupUseCase() {
      return new ImportBackupUseCase(singletonCImpl.restoreDao(), singletonCImpl.localBackupProvider());
    }

    private GetFocusStatsUseCase getFocusStatsUseCase() {
      return new GetFocusStatsUseCase(singletonCImpl.sessionDao());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandle savedStateHandleParam,
        final ViewModelLifecycle viewModelLifecycleParam) {
      this.appPickerViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 0);
      this.automationEditViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 1);
      this.automationListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 2);
      this.backupViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 3);
      this.dashboardViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 4);
      this.diagnosticLogViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 5);
      this.onboardingViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 6);
      this.profileEditViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 7);
      this.profileListViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 8);
      this.statsViewModelProvider = new SwitchingProvider<>(singletonCImpl, activityRetainedCImpl, viewModelCImpl, 9);
    }

    @Override
    public Map<Class<?>, javax.inject.Provider<ViewModel>> getHiltViewModelMap() {
      return LazyClassKeyMap.<javax.inject.Provider<ViewModel>>of(MapBuilder.<String, javax.inject.Provider<ViewModel>>newMapBuilder(10).put(LazyClassKeyProvider.com_rebelfocus_features_apps_AppPickerViewModel, ((Provider) appPickerViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_automation_AutomationEditViewModel, ((Provider) automationEditViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_automation_AutomationListViewModel, ((Provider) automationListViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_backup_BackupViewModel, ((Provider) backupViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_dashboard_DashboardViewModel, ((Provider) dashboardViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_logging_DiagnosticLogViewModel, ((Provider) diagnosticLogViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_onboarding_OnboardingViewModel, ((Provider) onboardingViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_profiles_ProfileEditViewModel, ((Provider) profileEditViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_profiles_ProfileListViewModel, ((Provider) profileListViewModelProvider)).put(LazyClassKeyProvider.com_rebelfocus_features_stats_StatsViewModel, ((Provider) statsViewModelProvider)).build());
    }

    @Override
    public Map<Class<?>, Object> getHiltViewModelAssistedMap() {
      return Collections.<Class<?>, Object>emptyMap();
    }

    @IdentifierNameString
    private static final class LazyClassKeyProvider {
      static String com_rebelfocus_features_logging_DiagnosticLogViewModel = "com.rebelfocus.features.logging.DiagnosticLogViewModel";

      static String com_rebelfocus_features_automation_AutomationEditViewModel = "com.rebelfocus.features.automation.AutomationEditViewModel";

      static String com_rebelfocus_features_profiles_ProfileListViewModel = "com.rebelfocus.features.profiles.ProfileListViewModel";

      static String com_rebelfocus_features_apps_AppPickerViewModel = "com.rebelfocus.features.apps.AppPickerViewModel";

      static String com_rebelfocus_features_stats_StatsViewModel = "com.rebelfocus.features.stats.StatsViewModel";

      static String com_rebelfocus_features_dashboard_DashboardViewModel = "com.rebelfocus.features.dashboard.DashboardViewModel";

      static String com_rebelfocus_features_profiles_ProfileEditViewModel = "com.rebelfocus.features.profiles.ProfileEditViewModel";

      static String com_rebelfocus_features_backup_BackupViewModel = "com.rebelfocus.features.backup.BackupViewModel";

      static String com_rebelfocus_features_automation_AutomationListViewModel = "com.rebelfocus.features.automation.AutomationListViewModel";

      static String com_rebelfocus_features_onboarding_OnboardingViewModel = "com.rebelfocus.features.onboarding.OnboardingViewModel";

      @KeepFieldType
      DiagnosticLogViewModel com_rebelfocus_features_logging_DiagnosticLogViewModel2;

      @KeepFieldType
      AutomationEditViewModel com_rebelfocus_features_automation_AutomationEditViewModel2;

      @KeepFieldType
      ProfileListViewModel com_rebelfocus_features_profiles_ProfileListViewModel2;

      @KeepFieldType
      AppPickerViewModel com_rebelfocus_features_apps_AppPickerViewModel2;

      @KeepFieldType
      StatsViewModel com_rebelfocus_features_stats_StatsViewModel2;

      @KeepFieldType
      DashboardViewModel com_rebelfocus_features_dashboard_DashboardViewModel2;

      @KeepFieldType
      ProfileEditViewModel com_rebelfocus_features_profiles_ProfileEditViewModel2;

      @KeepFieldType
      BackupViewModel com_rebelfocus_features_backup_BackupViewModel2;

      @KeepFieldType
      AutomationListViewModel com_rebelfocus_features_automation_AutomationListViewModel2;

      @KeepFieldType
      OnboardingViewModel com_rebelfocus_features_onboarding_OnboardingViewModel2;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final ViewModelCImpl viewModelCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          ViewModelCImpl viewModelCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.viewModelCImpl = viewModelCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rebelfocus.features.apps.AppPickerViewModel 
          return (T) new AppPickerViewModel(singletonCImpl.launcherAppDiscoveryProvider.get(), singletonCImpl.bindBlockedAppRepositoryProvider.get(), singletonCImpl.blockingDecisionEngineProvider.get());

          case 1: // com.rebelfocus.features.automation.AutomationEditViewModel 
          return (T) new AutomationEditViewModel(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), viewModelCImpl.savedStateHandle, singletonCImpl.bindAutomationRuleRepositoryProvider.get(), singletonCImpl.bindFocusProfileRepositoryProvider.get(), singletonCImpl.scheduleSessionUseCase(), singletonCImpl.systemAlarmSchedulerProvider.get(), singletonCImpl.deviceCalendarSource());

          case 2: // com.rebelfocus.features.automation.AutomationListViewModel 
          return (T) new AutomationListViewModel(singletonCImpl.bindAutomationRuleRepositoryProvider.get(), singletonCImpl.scheduleSessionUseCase(), viewModelCImpl.cancelScheduledSessionUseCase());

          case 3: // com.rebelfocus.features.backup.BackupViewModel 
          return (T) new BackupViewModel(viewModelCImpl.exportBackupUseCase(), viewModelCImpl.importBackupUseCase());

          case 4: // com.rebelfocus.features.dashboard.DashboardViewModel 
          return (T) new DashboardViewModel(singletonCImpl.bindFocusProfileRepositoryProvider.get(), singletonCImpl.bindSessionRepositoryProvider.get(), singletonCImpl.permissionCheckerProvider.get(), singletonCImpl.stopFocusSessionUseCase(), singletonCImpl.startFocusSessionUseCase());

          case 5: // com.rebelfocus.features.logging.DiagnosticLogViewModel 
          return (T) new DiagnosticLogViewModel(singletonCImpl.diagnosticLogDao());

          case 6: // com.rebelfocus.features.onboarding.OnboardingViewModel 
          return (T) new OnboardingViewModel(singletonCImpl.permissionCheckerProvider.get(), singletonCImpl.userPreferencesDataStoreProvider.get());

          case 7: // com.rebelfocus.features.profiles.ProfileEditViewModel 
          return (T) new ProfileEditViewModel(viewModelCImpl.savedStateHandle, singletonCImpl.bindFocusProfileRepositoryProvider.get(), singletonCImpl.bindBlockedAppRepositoryProvider.get(), singletonCImpl.blockingDecisionEngineProvider.get());

          case 8: // com.rebelfocus.features.profiles.ProfileListViewModel 
          return (T) new ProfileListViewModel(singletonCImpl.bindFocusProfileRepositoryProvider.get(), singletonCImpl.blockingDecisionEngineProvider.get());

          case 9: // com.rebelfocus.features.stats.StatsViewModel 
          return (T) new StatsViewModel(viewModelCImpl.getFocusStatsUseCase());

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ActivityRetainedCImpl extends RebelFocusApp_HiltComponents.ActivityRetainedC {
    private final SingletonCImpl singletonCImpl;

    private final ActivityRetainedCImpl activityRetainedCImpl = this;

    private Provider<ActivityRetainedLifecycle> provideActivityRetainedLifecycleProvider;

    private ActivityRetainedCImpl(SingletonCImpl singletonCImpl,
        SavedStateHandleHolder savedStateHandleHolderParam) {
      this.singletonCImpl = singletonCImpl;

      initialize(savedStateHandleHolderParam);

    }

    @SuppressWarnings("unchecked")
    private void initialize(final SavedStateHandleHolder savedStateHandleHolderParam) {
      this.provideActivityRetainedLifecycleProvider = DoubleCheck.provider(new SwitchingProvider<ActivityRetainedLifecycle>(singletonCImpl, activityRetainedCImpl, 0));
    }

    @Override
    public ActivityComponentBuilder activityComponentBuilder() {
      return new ActivityCBuilder(singletonCImpl, activityRetainedCImpl);
    }

    @Override
    public ActivityRetainedLifecycle getActivityRetainedLifecycle() {
      return provideActivityRetainedLifecycleProvider.get();
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final ActivityRetainedCImpl activityRetainedCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, ActivityRetainedCImpl activityRetainedCImpl,
          int id) {
        this.singletonCImpl = singletonCImpl;
        this.activityRetainedCImpl = activityRetainedCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // dagger.hilt.android.ActivityRetainedLifecycle 
          return (T) ActivityRetainedComponentManager_LifecycleModule_ProvideActivityRetainedLifecycleFactory.provideActivityRetainedLifecycle();

          default: throw new AssertionError(id);
        }
      }
    }
  }

  private static final class ServiceCImpl extends RebelFocusApp_HiltComponents.ServiceC {
    private final SingletonCImpl singletonCImpl;

    private final ServiceCImpl serviceCImpl = this;

    private ServiceCImpl(SingletonCImpl singletonCImpl, Service serviceParam) {
      this.singletonCImpl = singletonCImpl;


    }

    private TransitionToBreakUseCase transitionToBreakUseCase() {
      return new TransitionToBreakUseCase(singletonCImpl.sessionEngineProvider.get());
    }

    private TransitionToActiveFocusUseCase transitionToActiveFocusUseCase() {
      return new TransitionToActiveFocusUseCase(singletonCImpl.sessionEngineProvider.get());
    }

    @Override
    public void injectRebelFocusAccessibilityService(
        RebelFocusAccessibilityService rebelFocusAccessibilityService) {
      injectRebelFocusAccessibilityService2(rebelFocusAccessibilityService);
    }

    @Override
    public void injectFocusSessionService(FocusSessionService focusSessionService) {
      injectFocusSessionService2(focusSessionService);
    }

    private RebelFocusAccessibilityService injectRebelFocusAccessibilityService2(
        RebelFocusAccessibilityService instance) {
      RebelFocusAccessibilityService_MembersInjector.injectBlockingDecisionEngine(instance, singletonCImpl.blockingDecisionEngineProvider.get());
      RebelFocusAccessibilityService_MembersInjector.injectOverlayController(instance, singletonCImpl.overlayControllerProvider.get());
      RebelFocusAccessibilityService_MembersInjector.injectAuditEventRepository(instance, singletonCImpl.bindAuditEventRepositoryProvider.get());
      RebelFocusAccessibilityService_MembersInjector.injectSessionRepository(instance, singletonCImpl.bindSessionRepositoryProvider.get());
      RebelFocusAccessibilityService_MembersInjector.injectStopFocusSessionUseCase(instance, singletonCImpl.stopFocusSessionUseCase());
      RebelFocusAccessibilityService_MembersInjector.injectDiagnosticLogDao(instance, singletonCImpl.diagnosticLogDao());
      return instance;
    }

    private FocusSessionService injectFocusSessionService2(FocusSessionService instance) {
      FocusSessionService_MembersInjector.injectSessionRepository(instance, singletonCImpl.bindSessionRepositoryProvider.get());
      FocusSessionService_MembersInjector.injectNotificationHelper(instance, singletonCImpl.notificationHelperProvider.get());
      FocusSessionService_MembersInjector.injectTimingCalculator(instance, singletonCImpl.provideSessionTimingCalculatorProvider.get());
      FocusSessionService_MembersInjector.injectTransitionToBreakUseCase(instance, transitionToBreakUseCase());
      FocusSessionService_MembersInjector.injectTransitionToActiveFocusUseCase(instance, transitionToActiveFocusUseCase());
      FocusSessionService_MembersInjector.injectStopFocusSessionUseCase(instance, singletonCImpl.stopFocusSessionUseCase());
      FocusSessionService_MembersInjector.injectDiagnosticLogger(instance, singletonCImpl.diagnosticLoggerProvider.get());
      return instance;
    }
  }

  private static final class SingletonCImpl extends RebelFocusApp_HiltComponents.SingletonC {
    private final ApplicationContextModule applicationContextModule;

    private final SingletonCImpl singletonCImpl = this;

    private Provider<RebelFocusDatabase> provideDatabaseProvider;

    private Provider<AutomationRuleRepositoryImpl> automationRuleRepositoryImplProvider;

    private Provider<AutomationRuleRepository> bindAutomationRuleRepositoryProvider;

    private Provider<ScheduledTriggerRepositoryImpl> scheduledTriggerRepositoryImplProvider;

    private Provider<ScheduledTriggerRepository> bindScheduledTriggerRepositoryProvider;

    private Provider<SystemAlarmScheduler> systemAlarmSchedulerProvider;

    private Provider<CalendarSyncWorker_AssistedFactory> calendarSyncWorker_AssistedFactoryProvider;

    private Provider<RestoreSchedulesWorker_AssistedFactory> restoreSchedulesWorker_AssistedFactoryProvider;

    private Provider<TimeSource> provideTimeSourceProvider;

    private Provider<SessionRepositoryImpl> sessionRepositoryImplProvider;

    private Provider<SessionRepository> bindSessionRepositoryProvider;

    private Provider<AuditEventRepositoryImpl> auditEventRepositoryImplProvider;

    private Provider<AuditEventRepository> bindAuditEventRepositoryProvider;

    private Provider<SessionStateMachine> provideSessionStateMachineProvider;

    private Provider<SessionTimingCalculator> provideSessionTimingCalculatorProvider;

    private Provider<SessionEngine> sessionEngineProvider;

    private Provider<FocusProfileRepositoryImpl> focusProfileRepositoryImplProvider;

    private Provider<FocusProfileRepository> bindFocusProfileRepositoryProvider;

    private Provider<DiagnosticLogger> diagnosticLoggerProvider;

    private Provider<UserPreferencesDataStore> userPreferencesDataStoreProvider;

    private Provider<FocusSessionOrchestrator> focusSessionOrchestratorProvider;

    private Provider<NotificationHelper> notificationHelperProvider;

    private Provider<LauncherAppDiscovery> launcherAppDiscoveryProvider;

    private Provider<BlockedAppRepositoryImpl> blockedAppRepositoryImplProvider;

    private Provider<BlockedAppRepository> bindBlockedAppRepositoryProvider;

    private Provider<BlockingDecisionEngine> blockingDecisionEngineProvider;

    private Provider<PermissionChecker> permissionCheckerProvider;

    private Provider<OverlayController> overlayControllerProvider;

    private SingletonCImpl(ApplicationContextModule applicationContextModuleParam) {
      this.applicationContextModule = applicationContextModuleParam;
      initialize(applicationContextModuleParam);

    }

    private AutomationRuleDao automationRuleDao() {
      return DatabaseModule_ProvideAutomationRuleDaoFactory.provideAutomationRuleDao(provideDatabaseProvider.get());
    }

    private ScheduledTriggerDao scheduledTriggerDao() {
      return DatabaseModule_ProvideScheduledTriggerDaoFactory.provideScheduledTriggerDao(provideDatabaseProvider.get());
    }

    private DeviceCalendarSource deviceCalendarSource() {
      return new DeviceCalendarSource(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
    }

    private SyncCalendarEventsUseCase syncCalendarEventsUseCase() {
      return new SyncCalendarEventsUseCase(bindAutomationRuleRepositoryProvider.get(), bindScheduledTriggerRepositoryProvider.get(), deviceCalendarSource(), systemAlarmSchedulerProvider.get());
    }

    private ScheduleSessionUseCase scheduleSessionUseCase() {
      return new ScheduleSessionUseCase(new NextOccurrenceCalculator(), systemAlarmSchedulerProvider.get());
    }

    private Map<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>> mapOfStringAndProviderOfWorkerAssistedFactoryOf(
        ) {
      return MapBuilder.<String, javax.inject.Provider<WorkerAssistedFactory<? extends ListenableWorker>>>newMapBuilder(2).put("com.rebelfocus.services.calendar.CalendarSyncWorker", ((Provider) calendarSyncWorker_AssistedFactoryProvider)).put("com.rebelfocus.services.scheduling.RestoreSchedulesWorker", ((Provider) restoreSchedulesWorker_AssistedFactoryProvider)).build();
    }

    private HiltWorkerFactory hiltWorkerFactory() {
      return WorkerFactoryModule_ProvideFactoryFactory.provideFactory(mapOfStringAndProviderOfWorkerAssistedFactoryOf());
    }

    private SessionDao sessionDao() {
      return DatabaseModule_ProvideSessionDaoFactory.provideSessionDao(provideDatabaseProvider.get());
    }

    private AuditEventDao auditEventDao() {
      return DatabaseModule_ProvideAuditEventDaoFactory.provideAuditEventDao(provideDatabaseProvider.get());
    }

    private PauseSessionUseCase pauseSessionUseCase() {
      return new PauseSessionUseCase(sessionEngineProvider.get());
    }

    private ResumeSessionUseCase resumeSessionUseCase() {
      return new ResumeSessionUseCase(sessionEngineProvider.get());
    }

    private StopFocusSessionUseCase stopFocusSessionUseCase() {
      return new StopFocusSessionUseCase(sessionEngineProvider.get());
    }

    private FocusProfileDao focusProfileDao() {
      return DatabaseModule_ProvideFocusProfileDaoFactory.provideFocusProfileDao(provideDatabaseProvider.get());
    }

    private StartFocusSessionUseCase startFocusSessionUseCase() {
      return new StartFocusSessionUseCase(sessionEngineProvider.get(), bindSessionRepositoryProvider.get());
    }

    private ExecuteScheduleUseCase executeScheduleUseCase() {
      return new ExecuteScheduleUseCase(bindAutomationRuleRepositoryProvider.get(), bindFocusProfileRepositoryProvider.get(), startFocusSessionUseCase(), scheduleSessionUseCase());
    }

    private DiagnosticLogDao diagnosticLogDao() {
      return DatabaseModule_ProvideDiagnosticLogDaoFactory.provideDiagnosticLogDao(provideDatabaseProvider.get());
    }

    private ExecuteScheduledTriggerUseCase executeScheduledTriggerUseCase() {
      return new ExecuteScheduledTriggerUseCase(bindScheduledTriggerRepositoryProvider.get(), bindAutomationRuleRepositoryProvider.get(), bindFocusProfileRepositoryProvider.get(), bindSessionRepositoryProvider.get(), startFocusSessionUseCase(), diagnosticLoggerProvider.get());
    }

    private BlockedAppDao blockedAppDao() {
      return DatabaseModule_ProvideBlockedAppDaoFactory.provideBlockedAppDao(provideDatabaseProvider.get());
    }

    private LocalBackupProvider localBackupProvider() {
      return new LocalBackupProvider(ApplicationContextModule_ProvideContextFactory.provideContext(applicationContextModule));
    }

    private RestoreDao restoreDao() {
      return DatabaseModule_ProvideRestoreDaoFactory.provideRestoreDao(provideDatabaseProvider.get());
    }

    @SuppressWarnings("unchecked")
    private void initialize(final ApplicationContextModule applicationContextModuleParam) {
      this.provideDatabaseProvider = DoubleCheck.provider(new SwitchingProvider<RebelFocusDatabase>(singletonCImpl, 2));
      this.automationRuleRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 1);
      this.bindAutomationRuleRepositoryProvider = DoubleCheck.provider((Provider) automationRuleRepositoryImplProvider);
      this.scheduledTriggerRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 3);
      this.bindScheduledTriggerRepositoryProvider = DoubleCheck.provider((Provider) scheduledTriggerRepositoryImplProvider);
      this.systemAlarmSchedulerProvider = DoubleCheck.provider(new SwitchingProvider<SystemAlarmScheduler>(singletonCImpl, 4));
      this.calendarSyncWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<CalendarSyncWorker_AssistedFactory>(singletonCImpl, 0));
      this.restoreSchedulesWorker_AssistedFactoryProvider = SingleCheck.provider(new SwitchingProvider<RestoreSchedulesWorker_AssistedFactory>(singletonCImpl, 5));
      this.provideTimeSourceProvider = DoubleCheck.provider(new SwitchingProvider<TimeSource>(singletonCImpl, 7));
      this.sessionRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 8);
      this.bindSessionRepositoryProvider = DoubleCheck.provider((Provider) sessionRepositoryImplProvider);
      this.auditEventRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 9);
      this.bindAuditEventRepositoryProvider = DoubleCheck.provider((Provider) auditEventRepositoryImplProvider);
      this.provideSessionStateMachineProvider = DoubleCheck.provider(new SwitchingProvider<SessionStateMachine>(singletonCImpl, 10));
      this.provideSessionTimingCalculatorProvider = DoubleCheck.provider(new SwitchingProvider<SessionTimingCalculator>(singletonCImpl, 11));
      this.sessionEngineProvider = DoubleCheck.provider(new SwitchingProvider<SessionEngine>(singletonCImpl, 6));
      this.focusProfileRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 12);
      this.bindFocusProfileRepositoryProvider = DoubleCheck.provider((Provider) focusProfileRepositoryImplProvider);
      this.diagnosticLoggerProvider = DoubleCheck.provider(new SwitchingProvider<DiagnosticLogger>(singletonCImpl, 13));
      this.userPreferencesDataStoreProvider = DoubleCheck.provider(new SwitchingProvider<UserPreferencesDataStore>(singletonCImpl, 14));
      this.focusSessionOrchestratorProvider = DoubleCheck.provider(new SwitchingProvider<FocusSessionOrchestrator>(singletonCImpl, 15));
      this.notificationHelperProvider = DoubleCheck.provider(new SwitchingProvider<NotificationHelper>(singletonCImpl, 16));
      this.launcherAppDiscoveryProvider = DoubleCheck.provider(new SwitchingProvider<LauncherAppDiscovery>(singletonCImpl, 17));
      this.blockedAppRepositoryImplProvider = new SwitchingProvider<>(singletonCImpl, 18);
      this.bindBlockedAppRepositoryProvider = DoubleCheck.provider((Provider) blockedAppRepositoryImplProvider);
      this.blockingDecisionEngineProvider = DoubleCheck.provider(new SwitchingProvider<BlockingDecisionEngine>(singletonCImpl, 19));
      this.permissionCheckerProvider = DoubleCheck.provider(new SwitchingProvider<PermissionChecker>(singletonCImpl, 20));
      this.overlayControllerProvider = DoubleCheck.provider(new SwitchingProvider<OverlayController>(singletonCImpl, 21));
    }

    @Override
    public void injectRebelFocusApp(RebelFocusApp rebelFocusApp) {
      injectRebelFocusApp2(rebelFocusApp);
    }

    @Override
    public void injectServiceActionReceiver(ServiceActionReceiver serviceActionReceiver) {
      injectServiceActionReceiver2(serviceActionReceiver);
    }

    @Override
    public void injectScheduleAlarmReceiver(ScheduleAlarmReceiver scheduleAlarmReceiver) {
      injectScheduleAlarmReceiver2(scheduleAlarmReceiver);
    }

    @Override
    public void injectTimeChangeReceiver(TimeChangeReceiver timeChangeReceiver) {
      injectTimeChangeReceiver2(timeChangeReceiver);
    }

    @Override
    public Set<Boolean> getDisableFragmentGetContextFix() {
      return Collections.<Boolean>emptySet();
    }

    @Override
    public ActivityRetainedComponentBuilder retainedComponentBuilder() {
      return new ActivityRetainedCBuilder(singletonCImpl);
    }

    @Override
    public ServiceComponentBuilder serviceComponentBuilder() {
      return new ServiceCBuilder(singletonCImpl);
    }

    private RebelFocusApp injectRebelFocusApp2(RebelFocusApp instance) {
      RebelFocusApp_MembersInjector.injectWorkerFactory(instance, hiltWorkerFactory());
      return instance;
    }

    private ServiceActionReceiver injectServiceActionReceiver2(ServiceActionReceiver instance) {
      ServiceActionReceiver_MembersInjector.injectPauseSessionUseCase(instance, pauseSessionUseCase());
      ServiceActionReceiver_MembersInjector.injectResumeSessionUseCase(instance, resumeSessionUseCase());
      ServiceActionReceiver_MembersInjector.injectStopFocusSessionUseCase(instance, stopFocusSessionUseCase());
      return instance;
    }

    private ScheduleAlarmReceiver injectScheduleAlarmReceiver2(ScheduleAlarmReceiver instance) {
      ScheduleAlarmReceiver_MembersInjector.injectExecuteScheduleUseCase(instance, executeScheduleUseCase());
      ScheduleAlarmReceiver_MembersInjector.injectExecuteScheduledTriggerUseCase(instance, executeScheduledTriggerUseCase());
      return instance;
    }

    private TimeChangeReceiver injectTimeChangeReceiver2(TimeChangeReceiver instance) {
      TimeChangeReceiver_MembersInjector.injectLogger(instance, diagnosticLoggerProvider.get());
      TimeChangeReceiver_MembersInjector.injectRuleRepository(instance, bindAutomationRuleRepositoryProvider.get());
      TimeChangeReceiver_MembersInjector.injectScheduleSessionUseCase(instance, scheduleSessionUseCase());
      return instance;
    }

    private static final class SwitchingProvider<T> implements Provider<T> {
      private final SingletonCImpl singletonCImpl;

      private final int id;

      SwitchingProvider(SingletonCImpl singletonCImpl, int id) {
        this.singletonCImpl = singletonCImpl;
        this.id = id;
      }

      @SuppressWarnings("unchecked")
      @Override
      public T get() {
        switch (id) {
          case 0: // com.rebelfocus.services.calendar.CalendarSyncWorker_AssistedFactory 
          return (T) new CalendarSyncWorker_AssistedFactory() {
            @Override
            public CalendarSyncWorker create(Context appContext, WorkerParameters workerParams) {
              return new CalendarSyncWorker(appContext, workerParams, singletonCImpl.syncCalendarEventsUseCase());
            }
          };

          case 1: // com.rebelfocus.core.data.repository.impl.AutomationRuleRepositoryImpl 
          return (T) new AutomationRuleRepositoryImpl(singletonCImpl.automationRuleDao());

          case 2: // com.rebelfocus.core.database.RebelFocusDatabase 
          return (T) DatabaseModule_ProvideDatabaseFactory.provideDatabase(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 3: // com.rebelfocus.core.data.repository.ScheduledTriggerRepositoryImpl 
          return (T) new ScheduledTriggerRepositoryImpl(singletonCImpl.scheduledTriggerDao());

          case 4: // com.rebelfocus.services.scheduling.SystemAlarmScheduler 
          return (T) new SystemAlarmScheduler(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 5: // com.rebelfocus.services.scheduling.RestoreSchedulesWorker_AssistedFactory 
          return (T) new RestoreSchedulesWorker_AssistedFactory() {
            @Override
            public RestoreSchedulesWorker create(Context appContext2,
                WorkerParameters workerParams2) {
              return new RestoreSchedulesWorker(appContext2, workerParams2, singletonCImpl.bindAutomationRuleRepositoryProvider.get(), singletonCImpl.scheduleSessionUseCase(), singletonCImpl.syncCalendarEventsUseCase());
            }
          };

          case 6: // com.rebelfocus.core.domain.session.SessionEngine 
          return (T) new SessionEngine(singletonCImpl.provideTimeSourceProvider.get(), singletonCImpl.bindSessionRepositoryProvider.get(), singletonCImpl.bindAuditEventRepositoryProvider.get(), singletonCImpl.provideSessionStateMachineProvider.get(), singletonCImpl.provideSessionTimingCalculatorProvider.get());

          case 7: // com.rebelfocus.core.domain.session.TimeSource 
          return (T) SessionModule_ProvideTimeSourceFactory.provideTimeSource();

          case 8: // com.rebelfocus.core.data.repository.impl.SessionRepositoryImpl 
          return (T) new SessionRepositoryImpl(singletonCImpl.sessionDao());

          case 9: // com.rebelfocus.core.data.repository.impl.AuditEventRepositoryImpl 
          return (T) new AuditEventRepositoryImpl(singletonCImpl.auditEventDao());

          case 10: // com.rebelfocus.core.domain.session.SessionStateMachine 
          return (T) SessionModule_ProvideSessionStateMachineFactory.provideSessionStateMachine();

          case 11: // com.rebelfocus.core.domain.session.SessionTimingCalculator 
          return (T) SessionModule_ProvideSessionTimingCalculatorFactory.provideSessionTimingCalculator();

          case 12: // com.rebelfocus.core.data.repository.impl.FocusProfileRepositoryImpl 
          return (T) new FocusProfileRepositoryImpl(singletonCImpl.focusProfileDao());

          case 13: // com.rebelfocus.core.domain.logging.DiagnosticLogger 
          return (T) new DiagnosticLogger(singletonCImpl.diagnosticLogDao());

          case 14: // com.rebelfocus.core.data.datastore.UserPreferencesDataStore 
          return (T) new UserPreferencesDataStore(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 15: // com.rebelfocus.services.foreground.FocusSessionOrchestrator 
          return (T) new FocusSessionOrchestrator(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bindSessionRepositoryProvider.get(), singletonCImpl.diagnosticLoggerProvider.get());

          case 16: // com.rebelfocus.services.foreground.NotificationHelper 
          return (T) new NotificationHelper(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 17: // com.rebelfocus.core.common.apps.LauncherAppDiscovery 
          return (T) new LauncherAppDiscovery(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 18: // com.rebelfocus.core.data.repository.impl.BlockedAppRepositoryImpl 
          return (T) new BlockedAppRepositoryImpl(singletonCImpl.blockedAppDao());

          case 19: // com.rebelfocus.core.domain.blocking.BlockingDecisionEngine 
          return (T) new BlockingDecisionEngine(singletonCImpl.bindSessionRepositoryProvider.get(), singletonCImpl.bindFocusProfileRepositoryProvider.get());

          case 20: // com.rebelfocus.core.common.permissions.PermissionChecker 
          return (T) new PermissionChecker(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule));

          case 21: // com.rebelfocus.services.overlay.OverlayController 
          return (T) new OverlayController(ApplicationContextModule_ProvideContextFactory.provideContext(singletonCImpl.applicationContextModule), singletonCImpl.bindSessionRepositoryProvider.get(), singletonCImpl.bindFocusProfileRepositoryProvider.get());

          default: throw new AssertionError(id);
        }
      }
    }
  }
}
