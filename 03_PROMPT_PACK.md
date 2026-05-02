# Rebel Focus — Antigravity Prompt Pack

Use these prompts in order.
Do **not** dump all of them into the agent at once.

Replace placeholders like `[PASTE SPEC HERE]` or `[CURRENT REPO STATE]` if needed.

---

## Prompt 1 — Planning pass

```text
You are implementing an Android app called Rebel Focus.

In this step, do not write production code yet.
Your job is to read the provided spec and produce an implementation plan.

Tasks:
1. Read the spec completely.
2. Extract the architecture and hard constraints.
3. Identify ambiguities, technical risks, and fragile areas.
4. Propose a phased implementation plan.
5. Propose a module, package, and file structure.
6. List tools, libraries, plugins, and Android capabilities required.
7. Define acceptance criteria per phase.
8. Explicitly call out anything that is unrealistic on standard Android.

Constraints:
- Android native app
- Kotlin + Compose + Room + Hilt + Coroutines/Flow
- sideloaded APK
- no Play Store assumptions
- no backend
- no enterprise features
- no impossible claims about anti-uninstall or anti-force-stop behavior

Output format:
- Executive summary
- Architecture summary
- Risks and ambiguities
- Phased implementation plan
- Proposed project structure
- Required tools and dependencies
- Acceptance criteria by phase
- Blockers only if truly blocking

Use this as the source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
```

---

## Prompt 2 — Bootstrap only

```text
Use the provided master spec and implement only Phase 0.

Goal:
Create the Android project bootstrap and keep the build green.

Required scope:
- project structure
- Gradle Kotlin DSL setup
- Version Catalog setup
- Hilt setup
- Compose setup
- Navigation skeleton
- base package structure
- placeholder feature screens
- Room placeholders only

Do not implement yet:
- real accessibility logic
- overlay logic
- scheduling
- calendar
- Drive sync
- advanced session engine

Before coding:
1. List the files you will create or modify.
2. Explain why each group of files is needed.

After coding:
1. Show the files changed.
2. Explain the key decisions.
3. Run the relevant build command.
4. Report build result.
5. Report what remains for the next phase.

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 3 — Core data and domain foundation

```text
Implement only Phase 1 from the implementation plan.

Scope:
- Room entities
- DAOs
- database module
- repository interfaces
- local repository implementations
- domain models
- DataStore setup for lightweight settings
- initial use case contracts

Rules:
- keep architecture aligned with the master spec
- do not jump ahead to accessibility or overlay code
- prefer small, testable abstractions
- add TODOs only when they map to future phases

Before coding:
- list files to touch
- explain intended changes

After coding:
- run build
- run relevant tests if added
- summarize completed work
- summarize remaining work
- note any mismatch with the spec

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 4 — Session engine and timer model

```text
Implement only Phase 2.

Scope:
- session state machine
- start / stop / pause / resume use cases
- monotonic-time-based remaining-time logic
- persisted session restoration logic
- ViewModel-facing state exposure

Important rule:
Do not make CountDownTimer the source of truth.
If you use it at all, it must be a UI convenience layer only.

Before coding:
- list files to create or modify
- explain the timing model you will use

After coding:
- run build
- run relevant tests
- show how session restoration works
- list what is intentionally deferred

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 5 — Onboarding, permissions, and app selection

```text
Implement only Phase 3.

Scope:
- onboarding screens
- permission education screens
- permission state monitor
- blocked app selection flow
- profile basics if required for this phase

Rules:
- explain permissions before redirecting the user
- make skipped permissions visible in settings
- do not implement enforcement logic in this phase
- keep app selection package visibility as narrow as practical

Before coding:
- list files to touch
- explain the onboarding flow

After coding:
- run build
- summarize permission flows
- summarize app selection strategy
- report known limitations

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 6 — Accessibility detection and blocking overlay

```text
Implement only Phase 4.

Scope:
- AccessibilityService
- event filtering and dispatch
- blocked app evaluation use case wiring
- overlay controller
- Compose-based blocking overlay
- audit events for detection and overlay actions

Rules:
- use the minimum required accessibility events
- do not put all business logic inside the service
- do not depend on a foreground Activity
- use TYPE_ACCESSIBILITY_OVERLAY as the primary implementation path
- include an emergency exit path

Before coding:
- list files to touch
- explain the event flow from accessibility event to overlay decision

After coding:
- run build
- explain how blocking decisions are made
- explain how duplicate overlays are prevented
- report OEM / platform limitations that still remain

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 7 — Foreground service and notification actions

```text
Implement only Phase 5.

Scope:
- focus foreground service
- session notification
- pause / resume / stop actions
- service start / stop rules
- service integration with active session state

Rules:
- use foreground service only while justified by active focus state
- keep service logic thin
- map actions to use cases
- avoid duplicate active sessions

Before coding:
- list files to touch
- explain service lifecycle rules

After coding:
- run build
- explain notification action flow
- report anything deferred for later hardening

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 8 — Scheduling and automation

```text
Implement only Phase 6.

Scope:
- AlarmManager integration for exact user-visible triggers
- WorkManager integration for durable background work
- automation rule persistence and evaluation
- schedule creation UI
- boot recovery for schedules where appropriate

Rules:
- use AlarmManager only where exact timing is justified
- use WorkManager for deferred or retryable work
- degrade safely if exact alarms are unavailable

Before coding:
- list files to touch
- explain which responsibilities belong to AlarmManager vs WorkManager

After coding:
- run build
- explain how exact-alarm-unavailable scenarios behave
- summarize schedule persistence and recovery behavior

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 9 — Calendar integration

```text
Implement only Phase 7.

Scope:
- CalendarSource abstraction
- DeviceCalendarSource implementation
- event-to-profile matching logic
- settings / mapping UI
- optional GoogleCalendarSource stub or adapter boundary if appropriate

Rules:
- local device calendar path comes first
- core app must work with no Google account
- keep Google-specific code optional and isolated

Before coding:
- list files to touch
- explain local calendar integration design

After coding:
- run build
- explain matching rules
- report permission requirements and user-visible fallbacks

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 10 — Drive backup and restore

```text
Implement only Phase 8.

Scope:
- export/import format
- backup metadata
- Drive sync adapter
- manual backup / restore entry points
- conflict resolution baseline

Rules:
- local database remains the source of truth
- sync is optional
- restore must validate input and fail safely

Before coding:
- list files to touch
- explain serialization and conflict strategy

After coding:
- run build
- explain backup format and restore validation
- list what remains for future sync hardening

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 11 — Stats and gamification foundation

```text
Implement only Phase 9.

Scope:
- statistics aggregation foundation
- streaks or progress metrics
- summary UI
- architecture hooks for future achievements or gamification

Rules:
- use existing persisted data
- do not rewrite the core engine
- keep this phase modular and optional

Before coding:
- list files to touch
- explain where aggregates will be computed

After coding:
- run build
- explain what metrics are now available
- explain how future gamification can extend this safely

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 12 — Hardening and release prep

```text
Implement only Phase 10.

Scope:
- test expansion
- diagnostics screen
- migration checks
- battery / permission edge-case handling
- performance baseline work
- CI improvements
- release prep documentation

Rules:
- prioritize stability over new features
- fix architectural drift if necessary
- document all major edge cases

Before coding:
- list files to touch
- explain the hardening priorities

After coding:
- run build
- run relevant tests
- summarize release readiness
- list remaining risks honestly

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[PASTE 02_PHASED_IMPLEMENTATION_PLAN.md HERE]
```

---

## Prompt 13 — Architecture audit

```text
Audit the current repository against the master spec.
Do not implement new features in this step.

Tasks:
1. Compare the repository to the master spec.
2. Identify architectural drift.
3. Identify fragile Android-specific behavior.
4. Identify hidden coupling between UI, services, and domain logic.
5. Identify missing tests.
6. Identify likely bugs in process recovery, permissions, and scheduling.
7. Produce a prioritized fix list.

Output format:
- What matches the spec
- What deviates from the spec
- Technical debt
- Probable bugs
- Missing tests
- Priority 1 fixes
- Priority 2 fixes
- Recommendation for the next 10 tasks

Use these as the source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
[OPTIONALLY PASTE CURRENT PHASE PLAN OR CURRENT STATUS HERE]
```

---

## Prompt 14 — Recovery prompt when the agent starts drifting

```text
Stop implementation and re-anchor to the source of truth.

You are drifting from the agreed architecture.
Do not write new code yet.

Tasks:
1. Re-read the master spec.
2. Identify where your current implementation diverges.
3. Explain the divergence.
4. Propose the minimal correction path.
5. Show which files should be rolled back, refactored, or left intact.
6. Wait for the next implementation instruction after presenting the correction plan.

Source of truth:
[PASTE 01_MASTER_SPEC.md HERE]
```

---

## Prompt 15 — Compact daily operator prompt

Use this when you do not want to paste the full long prompt every time.

```text
Use the master spec as the source of truth.
Implement only the requested phase.
Before coding, list the files you will touch and why.
After coding, run the relevant build/tests, summarize what changed, what remains, and any deviations from the spec.
Do not jump ahead to future phases.
```
