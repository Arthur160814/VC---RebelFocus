# Rebel Focus — Phased Implementation Plan

This file defines the recommended build order for the AI coding agent.

The goal is to reduce drift, keep the project compiling, and avoid introducing complex Android integrations too early.

## General rules for every phase

For every phase, the agent must:

1. list the files it plans to change,
2. explain the purpose of those changes,
3. implement only the requested scope,
4. run the relevant build and tests,
5. summarize what is done,
6. summarize what is still missing,
7. avoid silently implementing future phases.

---

## Phase 0 — Project bootstrap

### Goal
Create a clean, compiling Android project skeleton with the agreed architecture.

### Deliverables
- Android app module created
- Version Catalog configured
- Hilt configured
- Compose configured
- Navigation skeleton created
- Room placeholders created
- base packages created
- CI-friendly build commands documented

### Scope
- Gradle setup
- module layout
- dependency management
- app theme and basic navigation shell
- placeholder screens
- DI scaffolding

### Do not include yet
- real accessibility logic
- overlay implementation
- calendar integration
- Drive sync
- advanced timers

### Acceptance criteria
- debug build succeeds
- app launches
- Hilt works
- navigation works
- package structure matches the master spec

---

## Phase 1 — Core data and domain foundation

### Goal
Create the durable data model and the domain contracts that the rest of the app will use.

### Deliverables
- Room entities
- DAOs
- database setup
- repository interfaces
- repository implementations for local persistence
- domain models
- use case interfaces / initial implementations
- DataStore for lightweight settings

### Required entities
- SessionEntity
- BlockedAppEntity
- FocusProfileEntity
- ProfileBlockedAppCrossRef
- AutomationRuleEntity
- UserProgressEntity
- AuditEventEntity
- SyncStateEntity

### Acceptance criteria
- database compiles
- repositories compile
- simple repository tests pass
- migrations strategy is defined
- seed / debug data approach is documented if used

---

## Phase 2 — Session engine and timer model

### Goal
Implement a robust session engine that survives rotation and process recreation.

### Deliverables
- session state machine
- `StartFocusSessionUseCase`
- `StopFocusSessionUseCase`
- `PauseSessionUseCase`
- `ResumeSessionUseCase`
- remaining time computation based on monotonic time
- active session restoration logic
- notification model contract

### Rules
- the source of truth must be persisted state
- UI timers must be derived, not authoritative
- do not make `CountDownTimer` the core engine

### Acceptance criteria
- session lifecycle logic is testable
- remaining time is correctly restored after recreation
- pause / resume logic works
- state transitions are unit tested

---

## Phase 3 — Onboarding, permissions, and app selection

### Goal
Build the user setup flow needed before enforcement starts.

### Deliverables
- onboarding screens
- permission explanation screens
- permission state monitor
- app picker for blocked apps
- profile creation basics
- settings entry points

### Permissions / setup flows
- accessibility enablement education
- notification permission where required
- fallback overlay permission flow if fallback overlay is implemented
- battery optimization education
- exact alarm education only if scheduling is implemented in the same phase or earlier

### Acceptance criteria
- user can complete onboarding
- user can grant or skip non-critical permissions
- blocked apps can be selected and saved
- permission status is reflected in the UI

---

## Phase 4 — Accessibility detection and blocking overlay

### Goal
Implement the actual detection of blocked apps and the primary overlay path.

### Deliverables
- AccessibilityService implementation
- event filtering and debouncing
- blocked app evaluation use case
- overlay controller
- Compose-based block UI mounted from the service
- audit events for block / allow paths

### Rules
- subscribe only to required accessibility events
- do not embed all business logic inside the service
- do not depend on a foreground Activity to block apps
- use `TYPE_ACCESSIBILITY_OVERLAY` as the primary path

### Acceptance criteria
- active blocked app detection works
- overlay appears while session is active
- overlay does not appear when no session is active
- emergency exit path exists
- service logic remains thin and testable

---

## Phase 5 — Foreground focus service and notification actions

### Goal
Add durable session presence, notification actions, and service-backed recovery.

### Deliverables
- focus foreground service
- persistent notification for active sessions
- pause / resume / stop actions from the notification
- service integration with active session state
- service start / stop rules

### Rules
- foreground service only during active focus states where justified
- no always-on idle foreground service
- notification actions must map to domain use cases

### Acceptance criteria
- session notification stays in sync with session state
- pause / resume / stop actions work
- service lifecycle does not create duplicate sessions

---

## Phase 6 — Scheduling and automation

### Goal
Add recurring and one-time focus automation.

### Deliverables
- AlarmManager integration for exact user-visible triggers
- WorkManager integration for persistent / deferred automation support
- rule evaluation engine
- schedule creation UI
- boot recovery for alarms where appropriate

### Supported rules
- one-time scheduled focus session
- recurring weekday schedule
- profile-based schedule assignment

### Acceptance criteria
- scheduled sessions start correctly
- alarms restore correctly after reboot where supported
- schedule state is visible in the UI
- the app degrades gracefully if exact alarms are unavailable

---

## Phase 7 — Calendar integration

### Goal
Map calendar events to focus profile activation.

### Deliverables
- `CalendarSource` abstraction
- `DeviceCalendarSource` implementation
- optional `GoogleCalendarSource` interface or adapter stub
- event-to-profile matching rules
- calendar settings UI

### Rules
- local device calendar path first
- Google calendar optional
- core app must work without any Google account

### Acceptance criteria
- calendar matching logic is testable
- local calendar event mapping works
- user can enable / disable calendar-driven automation

---

## Phase 8 — Drive backup and restore

### Goal
Add optional backup / restore with no custom backend.

### Deliverables
- export format
- import validation
- Drive sync adapter
- local restore flow
- conflict resolution strategy
- user-facing sync settings

### Rules
- Room remains the local source of truth
- sync is optional
- corrupt backups must fail safely

### Acceptance criteria
- local export works
- local import works
- Drive backup can be triggered manually
- sync metadata is persisted

---

## Phase 9 — Statistics and gamification foundation

### Goal
Add non-core features without rewriting the core engine.

### Deliverables
- stats repository and aggregation queries
- streak tracking
- session summary UI
- achievement hooks or placeholder architecture

### Acceptance criteria
- stats derive from existing persisted data
- no core engine rewrite is required
- feature remains optional and modular

---

## Phase 10 — Hardening, QA, and release prep

### Goal
Stabilize the application for real sideload use.

### Deliverables
- test coverage improvements
- battery / permission edge-case handling
- migration validation
- debug diagnostics screen
- baseline profile / performance improvements
- release signing guidance
- GitHub Actions setup
- release notes template

### Acceptance criteria
- project builds reproducibly
- critical flows are tested
- major permission revocation paths are handled
- reboot and recovery behavior is documented
- APK release process is documented

---

## Recommended stop points for human review

Stop and review after:
- Phase 0
- Phase 2
- Phase 4
- Phase 6
- Phase 8
- Phase 10

These are the best points to decide whether to continue, refactor, or tighten requirements.

## Risk notes

### Highest risk areas
- accessibility timing and event quality
- overlay behavior across OEMs
- battery restrictions and process recovery
- exact alarm availability
- permission revocation after onboarding
- over-coupling service code to UI code

### Rule for the agent
If a phase reveals a platform blocker or major architectural mismatch, stop and produce:
1. the blocker,
2. why it matters,
3. at least two feasible alternatives,
4. the recommended option.
