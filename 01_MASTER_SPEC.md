# Rebel Focus — Master Technical Specification

## 1. Project context

Build an open-source Android application named **Rebel Focus**.
It is a **productivity and distraction-blocking app** distributed as a **sideloaded APK**.
It will **not** be released on the Play Store.
It will **not** use enterprise device management.
It will **not** depend on a custom backend.

The app must maximize distraction containment **within the real limits of standard Android**.
It must not pretend to have kiosk-grade control.

## 2. Hard constraints

These decisions are already made and must not be re-litigated unless a hard technical blocker appears.

1. Distribution model: **APK sideload only**.
2. Open source project.
3. No Play Store assumptions.
4. No enterprise mode.
5. No device owner, kiosk mode, lock task mode, work profile, MDM, or EMM.
6. Local-first architecture.
7. Main persistence layer: **Room**.
8. UI: **Jetpack Compose**.
9. Dependency injection: **Hilt**.
10. Concurrency: **Coroutines + Flow / StateFlow**.
11. Detection layer: **AccessibilityService**.
12. Primary blocking overlay: **TYPE_ACCESSIBILITY_OVERLAY** when the overlay is owned by the accessibility service.
13. Optional fallback overlay: **TYPE_APPLICATION_OVERLAY** only as a fallback path, not as the main design.
14. Focus timer source of truth: **monotonic time (`elapsedRealtime`) + persisted state**, not raw `CountDownTimer`.
15. Exact, user-visible scheduling: **AlarmManager**.
16. Persistent, deferrable background work: **WorkManager**.
17. Optional backup/sync with no backend: **Google Drive `appDataFolder`**.
18. Calendar integration must support a local device-first adapter using **CalendarContract / Calendar Provider**, plus an optional Google Calendar adapter.
19. The app must not claim to be unbreakable.
20. The user and the system will always retain exit paths on standard Android.

## 3. Product goal

Rebel Focus should help a user enter focus sessions, block selected apps, and automate focus behavior through schedules and calendars.

It should be:
- fast,
- local-first,
- explicit about permissions,
- resilient to rotation and process recreation,
- modular enough to grow into automation, gamification, and advanced statistics later.

## 4. What the app must do

### Core user capabilities
- Start and stop focus sessions.
- Support Pomodoro sessions and manual free-form focus sessions.
- Let the user select apps to block.
- Detect when a blocked app becomes active.
- Show a blocking screen quickly and consistently over blocked apps.
- Keep a focus session alive across rotation, app backgrounding, and reasonable process recreation.
- Support scheduled focus rules.
- Activate focus profiles from calendar events.
- Persist everything locally.
- Optionally export, import, backup, and restore data.
- Maintain an audit trail of important internal events for debugging and analytics.

### UX requirements
- Clear onboarding.
- Explicit permission education before requesting special access.
- Friction-reduced setup for blocked app selection and profiles.
- Fast recovery when required permissions are revoked.
- A visible emergency exit / break-glass path.

## 5. What the app must not do

- Do not implement enterprise-only control models.
- Do not claim reliable prevention of uninstall, force-stop, ADB intervention, safe mode, or permission revocation.
- Do not build the system around Device Admin.
- Do not make the UI Activity the source of truth for active sessions.
- Do not use accessibility as a generic polling tool.
- Do not subscribe to every accessibility event type without justification.
- Do not default to `QUERY_ALL_PACKAGES` unless there is a clearly documented, unavoidable technical need.
- Do not create scheduling assumptions that depend on exact alarms always being granted.
- Do not make Google services mandatory for core app operation.

## 6. Recommended tech stack and tools

### Language and build
- Kotlin
- Gradle Kotlin DSL
- Android Gradle Plugin
- Version Catalog (`libs.versions.toml`)
- KSP

### Android UI
- Jetpack Compose
- Material 3
- Navigation Compose
- Lifecycle ViewModel
- SavedStateHandle
- ComposeView for overlays mounted from services

### Data and persistence
- Room
- DataStore
- kotlinx.serialization

### Concurrency and state
- Kotlin Coroutines
- Flow / StateFlow / SharedFlow
- Injected dispatchers

### System and enforcement
- AccessibilityService
- WindowManager
- TYPE_ACCESSIBILITY_OVERLAY
- TYPE_APPLICATION_OVERLAY as optional fallback
- Foreground Service used only while a focus session is active
- AlarmManager
- WorkManager
- BroadcastReceiver for boot and auxiliary events
- NotificationManager
- PackageManager and launcher queries for app selection

### Optional external integrations
- Credential Manager
- AuthorizationClient or the currently recommended Google authorization layer
- Google Calendar API adapter
- Google Drive API `appDataFolder` adapter

### Quality and reliability
- JUnit
- kotlinx-coroutines-test
- Turbine
- Hilt testing
- Compose UI testing
- UI Automator
- Macrobenchmark
- Baseline Profile
- Detekt
- Ktlint
- LeakCanary

### Development workflow
- Android Studio
- ADB
- Emulator and physical devices
- GitHub Actions
- GitHub Releases

## 7. Architecture style

Use **lightweight Clean Architecture + MVVM + unidirectional data flow**.

### Architectural principles
- Single source of truth: Room for durable domain state.
- UI consumes state and emits events, but does not own the focus session engine.
- Services should be thin orchestration adapters around domain use cases.
- State transitions must be explicit and testable.
- System integrations must be wrapped behind interfaces.
- Feature growth must not require rewriting the session engine.

## 8. Suggested module layout

```text
rebel-focus/
  app/
  core/common/
  core/model/
  core/database/
  core/data/
  core/domain/
  core/designsystem/
  core/testing/
  feature/onboarding/
  feature/dashboard/
  feature/session/
  feature/profiles/
  feature/apps/
  feature/automation/
  feature/settings/
  feature/stats/
  service/accessibility/
  service/focus/
  service/overlay/
  sync/calendar-local/
  sync/calendar-google/
  sync/drive/
  benchmark/
```

### Module intent
- `app/`: app shell, navigation root, DI wiring, manifest-level integration.
- `core/model/`: pure domain models and enums.
- `core/database/`: Room entities, DAOs, migrations.
- `core/data/`: repository implementations and data source orchestration.
- `core/domain/`: use cases, business rules, state machine logic.
- `feature/*`: screen-level UI, view models, feature interactions.
- `service/*`: Android system adapters and always-on components.
- `sync/*`: external adapters and synchronization providers.
- `benchmark/`: macrobenchmark and startup measurement.

## 9. High-level logical flow

```text
AccessibilityService detects active window
        -> package name is extracted
        -> blocked app evaluation use case runs
        -> if blocked and session active
             -> overlay controller shows block UI
             -> audit event is persisted
             -> UI state stream updates if needed

User starts or stops a session from Compose UI
        -> ViewModel dispatches intent
        -> domain use case updates Room state
        -> focus foreground service reacts to active session state
        -> scheduler / alarm layer is updated
        -> notification state is updated
```

## 10. Session engine and state model

The session engine must not rely on an Activity lifecycle.

### Session state machine
At minimum support:
- `Idle`
- `Scheduled`
- `ActiveFocus`
- `Break`
- `Paused`
- `Completed`
- `Cancelled`
- `EmergencyExit`

### Timing model
Do not make `CountDownTimer` the source of truth.
Instead:
- store session start time,
- store intended duration,
- store session state,
- compute remaining time from monotonic clock,
- reconstruct state after configuration changes and process recreation.

A `CountDownTimer` or ticker may exist only as a UI convenience layer.

## 11. Accessibility and blocking design

### Accessibility service responsibilities
- Listen to the minimum required event set.
- Detect when the active app changes.
- Extract package and optional class information.
- Forward the event into a debounced, testable evaluation pipeline.
- Avoid heavy work on the callback thread.

### Blocking decision path
The service must not embed all business logic.
Instead it should call a use case such as:
- `EvaluateBlockedAppUseCase`

Inputs:
- active package name
- active session state
- active profile rules
- emergency bypass state
- optional cooldown state

Output:
- allow
- block
- allow temporarily
- ignore because no active session

### Overlay behavior
Primary path:
- overlay owned by accessibility service
- `TYPE_ACCESSIBILITY_OVERLAY`
- block screen rendered with ComposeView

Fallback path:
- `TYPE_APPLICATION_OVERLAY` with special access granted
- only if needed and only as a secondary implementation

Overlay requirements:
- must appear fast,
- must not depend on a foreground Activity,
- must support dismiss logic only through approved paths,
- must support emergency exit,
- must record audit events.

## 12. Foreground service design

Use a foreground service only when a focus session is active.

### Responsibilities
- maintain durable session presence while active,
- host a persistent notification,
- respond to stop / pause / resume actions,
- coordinate with alarm and recovery logic,
- stay thin and delegate real business logic.

### Important rules
- do not keep a foreground service alive when no focus session is active,
- use valid service types for modern Android,
- make notification actions deterministic and testable.

## 13. Persistence model

### Core Room entities
At minimum include:
- `SessionEntity`
- `BlockedAppEntity`
- `FocusProfileEntity`
- `ProfileBlockedAppCrossRef`
- `AutomationRuleEntity`
- `UserProgressEntity`
- `AuditEventEntity`
- `SyncStateEntity`

### Small-config persistence
Use DataStore for:
- onboarding completed flags,
- temporary settings,
- emergency-exit counters,
- last-known permission state snapshots,
- lightweight app preferences.

### Audit events
Persist important events such as:
- session started,
- session paused,
- session resumed,
- session completed,
- blocked app detected,
- overlay shown,
- overlay dismissed,
- permission missing,
- scheduled trigger fired,
- calendar trigger matched,
- sync success / sync failure.

## 14. Scheduling and automation

### AlarmManager use cases
Use AlarmManager only for:
- exact session start,
- exact session end if strictly needed,
- user-visible time-sensitive automation.

### WorkManager use cases
Use WorkManager for:
- sync retries,
- backup jobs,
- stale-state cleanup,
- analytics compaction,
- non-exact automation maintenance.

### Automation rules
Support rules such as:
- every weekday 09:00–11:00,
- profile-based recurring schedules,
- event-triggered activation from calendar,
- one-time scheduled sessions.

## 15. Calendar integration

Use an abstraction:
- `CalendarSource`

Implementations:
1. `DeviceCalendarSource` using `CalendarContract`
2. `GoogleCalendarSource` as optional adapter

### Matching rules
Support matching by:
- time window,
- calendar id,
- title keyword,
- availability / busy state,
- all-day exclusion or inclusion,
- user-selected profile mapping.

### Important constraint
Calendar integration must be optional.
The app must still be fully functional without any Google account.

## 16. Backup and sync

Use a local-first model.

### Primary behavior
- the app works fully offline,
- sync is optional,
- Room remains the source of truth,
- exported snapshots are serialized from domain-safe models.

### Drive sync model
Use Google Drive `appDataFolder` for:
- encrypted or plain structured backup payloads,
- settings backup,
- profile backup,
- session history backup,
- sync metadata.

### Conflict strategy
Start simple:
- last-write-wins on top-level documents,
- stable record ids,
- version field,
- timestamp field,
- import validation,
- corruption-safe restore fallback.

## 17. App selection and package visibility

The user needs to choose apps to block.

Preferred strategy:
- use launcher-resolved apps,
- use targeted queries,
- keep package visibility narrow.

Do not default to broad package visibility unless there is a documented technical blocker.

## 18. Permissions and onboarding

The onboarding flow must explain every sensitive permission before sending the user to grant it.

### Potential permissions / special access
- Accessibility service enablement
- Notifications
- Overlay permission if fallback overlay is used
- Battery optimization exemption request where justified
- Exact alarm access where justified
- Boot completed receiver
- Calendar read permission when using local calendar integration

### Onboarding requirements
- explain why the permission is needed,
- explain what feature breaks without it,
- provide a continue / skip path when possible,
- continuously detect revoked permissions and degrade safely.

## 19. Battery, process death, and recovery

The architecture must explicitly handle:
- configuration changes,
- process recreation,
- app backgrounding,
- device reboot,
- permission revocation,
- OEM battery restrictions.

### Recovery requirements
- restore active session state from persisted data,
- reschedule alarms when appropriate,
- rebuild notification state,
- avoid duplicate overlays or duplicate active sessions,
- audit recovery paths.

## 20. Suggested package structure

```text
app/src/main/java/.../
  app/
    navigation/
    di/
    MainActivity.kt
    RebelFocusApp.kt
  core/
    common/
    model/
    database/
    data/
    domain/
    designsystem/
  features/
    onboarding/
    dashboard/
    session/
    profiles/
    apps/
    automation/
    settings/
    stats/
  services/
    accessibility/
    focus/
    overlay/
  sync/
    calendar/
    drive/
  receivers/
  notifications/
```

## 21. Minimum Android components to implement

- Main Activity
- Accessibility Service
- Foreground Focus Service
- Overlay controller
- Boot receiver
- Alarm receiver
- Notification action receiver if needed
- Room database
- DataStore module
- Hilt setup

## 22. Manifest expectations

The implementation should include a realistic AndroidManifest with:
- accessibility service declaration,
- foreground service declaration,
- boot receiver,
- notification permission when required by API level,
- overlay permission handling if fallback overlay is implemented,
- exact alarm handling only when justified,
- calendar permission only when calendar features are enabled.

The agent must add comments in the manifest or surrounding docs explaining why each sensitive capability exists.

## 23. Testing strategy

### Unit tests
Cover:
- session state machine,
- blocked app evaluation,
- automation rule matching,
- remaining-time calculations,
- sync conflict logic,
- permission-state mapping.

### Instrumentation tests
Cover:
- onboarding flow,
- session start and stop,
- database migrations,
- alarm recovery,
- process recreation scenarios where practical.

### UI tests
Cover:
- Compose screens,
- settings flow,
- blocked app list flow,
- profile editing,
- basic session interaction.

### Device-level testing
Cover:
- accessibility enabled and disabled,
- reboot recovery,
- battery-restricted state,
- permissions revoked after setup,
- blocked app launch behavior,
- overlay rendering on different API levels.

## 24. Observability and diagnostics

Include:
- structured local logging,
- audit event persistence,
- debug screen for permission and service state,
- optional export of diagnostic bundle for local troubleshooting.

## 25. Scalability path

The architecture must allow future addition of:
- gamification,
- streaks,
- achievements,
- advanced statistics,
- rule engine expansion,
- CSV / JSON export,
- richer backup and restore,
- widget support,
- wearable integrations later if desired.

These must be added without rewriting the focus session engine.

## 26. Definition of done for the first usable release

The first usable release is complete when:
- the app compiles and runs on modern Android,
- onboarding is functional,
- the user can select blocked apps,
- the user can start a focus session,
- blocked apps trigger the overlay while the session is active,
- session state survives rotation and reasonable process recreation,
- scheduled sessions work,
- local persistence is stable,
- audit events are recorded,
- the project structure is maintainable,
- there is basic test coverage,
- the app does not claim impossible control over the device.

## 27. Instruction to the coding agent

Use this document as the source of truth.

When implementing:
- prefer incremental delivery,
- keep the build green,
- avoid speculative features,
- do not jump to future phases without explicit instruction,
- explain any deviation from this spec,
- document blockers clearly,
- do not silently add enterprise concepts,
- do not overengineer the first milestone.
