# Rebel Focus - Complete Project Handoff and Chat Summary

This document consolidates the full working history, decisions, fixes, commands, release flow, feature set, QA notes, and next-step context for the Rebel Focus Android project.

It is written as a project handoff document, not as a raw chat transcript. It preserves the important information from the conversation so the project can be resumed later without losing context.

---

## 1. Project Summary

**Project name:** Rebel Focus

**Platform:** Android

**Purpose:** A focus and productivity app built around Pomodoro sessions, app blocking, strict focus modes, premium blocking overlay UX, weekly goals, analytics, backup/restore, and session transparency.

**Current repo:**

```text
https://github.com/Arthur160814/VC---RebelFocus.git
```

**Current release branch:**

```text
release/1.1.1
```

**Current release label:**

```text
release 1.1.1
```

**Recommended versioning convention:** semantic versioning.

```text
MAJOR.MINOR.PATCH
1.1.0
1.1.1
1.1.2
```

Use `1.1.1`, not `1.1.01`.

---

## 2. Current High-Level Feature Set

Rebel Focus currently includes:

- Pomodoro focus sessions.
- Break timer.
- Normal focus mode.
- Extreme Focus Mode.
- Ultimate Extreme Mode.
- Always-on blocking overlay for strict modes.
- Overlay covers Rebel Focus itself during strict modes.
- Overlay stays through Focus and Break.
- Emergency Exit available only where intended.
- Ultimate Mode hides visible exit controls.
- Premium dark minimal overlay UI.
- Focus phase red accent.
- Break phase blue accent.
- Completion surface with Well Done summary.
- DONE button to dismiss completion surface.
- Focus session analytics.
- Weekly goal tracker.
- Range selector for analytics: 7D, 30D, All.
- Mode Usage analytics: Normal, Extreme, Ultimate.
- Recent Sessions history.
- Backup and restore.
- Diagnostic logs.
- System health / permission guidance.
- Time and timezone hardening.
- Database migrations for evolving schema.
- GitHub release branch workflow.
- ADB install support for real device testing.

---

## 3. Major Development Timeline

### 3.1 Stabilization / Hardening Pass

After multiple rapid phases, the recommendation was to stabilize before adding larger features.

Main reasons:

1. Address technical debt.
2. Improve Android platform resilience.
3. Review foreground service decisions.
4. Harden materialized state such as calendar sync and alarm schedules.

Implemented:

- Added `createdAt` and `updatedAt` to `BlockedAppEntity`.
- Backup/restore merge logic updated for Last Write Wins.
- Added diagnostic logging infrastructure.
- Added `TimeChangeReceiver` for `TIME_SET` and `TIMEZONE_CHANGED`.
- Added system health guidance for battery optimization.
- Added database migration from version 2 to 3.
- Added diagnostic logs table.
- Hardened scheduling and calendar sync behavior.

Verification:

```text
assembleDebug: BUILD SUCCESSFUL
testDebugUnitTest: BUILD SUCCESSFUL
```

---

### 3.2 Phase 9A - Lightweight Statistics

Implemented initial statistics based on `SessionEntity`.

Included:

- Total focus time.
- Session count.
- Current streak.
- Longest streak.
- Weekly chart.

Deferred:

- Achievements.
- Badges.
- Rewards.

Important calculation decisions:

- Only `Completed` sessions count toward focus stats.
- Cancelled, EmergencyExit, Failed, Paused, Break, ActiveFocus, and Idle are excluded from completed focus metrics.
- Focus duration uses `elapsedAtPauseMillis`, not raw timestamp delta.
- Streak groups multiple sessions on the same day as one focus day.
- Current streak allows today/yesterday grace.

---

### 3.3 Trust and Transparency Polish

Goal: make advanced logic feel clear and trustworthy to users.

Implemented:

- Setup guidance card on Dashboard.
- Permission clarity for Accessibility, Exact Alarms, Battery Optimization.
- Lifecycle auto-refresh when returning from settings.
- Confirmation dialog for Cancel Session and Emergency Exit.
- Empty states for Profiles, Automations, and Statistics.
- Schedule validation.
- Centralized notification channel creation.

---

### 3.4 Release Candidate QA and Fixes

Issues found and fixed:

- Backup format version confusion.
- WorkManager initialization lint failure.
- Release build blocker due to WorkManagerInitializer conflict.

Fix:

- Removed default WorkManager initializer when using custom Hilt-enabled `Configuration.Provider`.

Release build passed afterward.

---

### 3.5 Focus Initiation UX Fix

Original issue:

- User could not intuitively start a Pomodoro.
- Tapping `Start Focusing` from stats took user back to dashboard, but did not clearly start a profile.

Fix:

- Dashboard profile cards gained explicit Play icon.
- Only Play starts session.
- Edit icon separately edits profile.
- Later, full-card tap was disabled because it caused accidental session starts.

---

### 3.6 Countdown Timer UX

Implemented visible countdown timer across:

- Dashboard active session card.
- Blocking overlay.
- Foreground notification.

Timer source:

- Uses monotonic `SystemClock.elapsedRealtime()` aligned with session engine.

Added:

- Phase label.
- Pomodoro progress.
- Break countdown.
- Notification updates.

---

### 3.7 Extreme Focus Mode Evolution

Initial intended behavior:

- Overlay should block selected apps.
- Later redefined to block any external app.
- Later finalized as an always-on session surface when Extreme/Ultimate starts.

Important final behavior:

- Extreme overlay appears immediately when session starts.
- It does not depend on switching apps.
- It remains during Focus and Break.
- It covers Rebel Focus itself.
- It dismisses only when session ends or Emergency Exit is used.

Major bugs and fixes:

1. **Launcher whitelist bypass**
   - Launcher packages were whitelisted.
   - Extreme logic was not reached for launcher.
   - Fix: remove launcher from static whitelist for Extreme path.

2. **Break transition missing**
   - Engine had Break state but service was not triggering transition.
   - Fix: FocusSessionService ticker transitions Focus -> Break -> Focus or Complete.

3. **Overlay Window token error**
   - Error: `Unable to add window -- token null is not valid`.
   - Cause: WindowManager from ApplicationContext lacked proper accessibility token.
   - Fix: OverlayController updates context from `RebelFocusAccessibilityService`.

4. **Overlay not touch-blocking / pass-through concerns**
   - Window flags and pointer input were hardened.
   - Fullscreen MATCH_PARENT overlay was verified.

5. **Overlay flickering**
   - Cause: overlay belonged to `com.rebelfocus`, which triggered an Allow event and hid itself.
   - Fix: service tracks enforced package and ignores Allow for Rebel Focus while enforcement is active.

6. **Overlay not appearing over Rebel Focus immediately**
   - Cause: overlay was only AccessibilityEvent-driven.
   - Fix: service observes active session state and shows overlay immediately for Extreme/Ultimate sessions.

7. **Theme crash in service context**
   - Crash: `RebelFocusApp cannot be cast to android.app.Activity`.
   - Cause: theme cast `view.context as Activity`.
   - Fix: safe cast `as? Activity` and guard window-specific logic.

---

### 3.8 Premium Overlay UI

Replaced debug red screen with premium dark minimal overlay.

Design:

- Near-black background.
- Large central timer.
- Elegant circular progress ring.
- Focus = deep red.
- Break = premium blue.
- Paused = neutral gray.
- Minimal text.
- Subtle motion.
- Emergency Exit displayed only in Extreme.
- Hidden in Ultimate.

Added:

- Session start ritual: fade and scale intro.
- Smooth phase crossfade.
- Smooth red-to-blue transition.
- Session intention/profile label.
- Well Done completion surface.
- DONE button.
- Haptics on phase transitions.

---

### 3.9 Ultimate Extreme Mode

Final behavior:

- Same always-on overlay behavior as Extreme.
- No visible Emergency Exit button.
- User remains in minimal overlay until session naturally completes.
- System UI remains exempt for stability.

Data changes:

- `isUltimateMode` added to FocusProfile and Session.
- Room migration 4 -> 5.
- Backup format eventually updated to preserve Ultimate state.

---

### 3.10 Completion Flow Fixes

Original issues:

- Haptics did not work.
- Well Done completion surface did not appear.
- DONE button did not work.
- App crashed on vibration.

Root causes and fixes:

1. **Haptics unreliable in Compose overlay context**
   - Switched from Compose `LocalHapticFeedback` to direct Android Vibrator.

2. **Crash due to missing VIBRATE permission**
   - Crash:

```text
java.lang.SecurityException: vibrate: Neither user nor current process has android.permission.VIBRATE
```

   - Fix:

```xml
<uses-permission android:name="android.permission.VIBRATE" />
```

   - Added `hasVibrator()` and try/catch safety guard.

3. **Completion surface never appeared**
   - Cause: `observeActiveSession()` emitted only ActiveFocus, Break, Paused.
   - When session became Completed, flow emitted null and overlay was dismissed.
   - Fix: added enforcement observer that includes Completed state.

4. **DONE button logic wrong**
   - It attempted Emergency Exit on an already Completed session.
   - Fix: if Completed, DONE only hides overlay and clears enforcement state.

---

### 3.11 Interval and Completion Summary Fixes

Issues:

1. Overlay showed:

```text
Interval 0 of 2
```

Fix:

- UI-only display mapping.
- During Focus: `pomodoroCount + 1`.
- During Break: `pomodoroCount`.
- Clamp min 1 and max target.

2. Well Done screen showed:

```text
Intervals: 1 / 2
```

Fix:

- In Completed branch, show `target / target`.
- Hide active interval label on completion screen.

3. Well Done screen showed:

```text
Focus: 1m
```

for 2 intervals of 1 minute.

Fix:

- Completion summary uses total focus:

```text
plannedDurationMillis * pomodoroTarget
```

- Example:

```text
1m x 2 -> Focus: 2m
25m x 4 -> Focus: 1h 40m
```

---

## 4. Analytics Roadmap and Implemented Analytics

### 4.1 Analytics 1 - Useful Focus Insights

Implemented:

- Weekly chart changed from sessions per day to focus minutes per day.
- Daily Average.
- Completion Rate.
- Interrupted Sessions.
- Best Focus Day.

Rules:

- Focus minutes use Completed sessions only.
- Break time is not included.
- Completion Rate = Completed / terminal sessions.
- Terminal sessions = Completed, Cancelled, EmergencyExit, Failed.
- Active sessions excluded.
- Interrupted = Cancelled + EmergencyExit + Failed.

Formatting fix:

- If total focus > 0 but average < 1 minute/day, show:

```text
<1 min/day
```

not:

```text
0 min/day
```

---

### 4.2 Analytics 2 - Range Selector

Implemented selector:

```text
7D | 30D | All
```

Default:

```text
7D
```

Range behavior:

- 7D: last 7 calendar days including today.
- 30D: last 30 calendar days including today.
- All: all history for metrics, recent 30 days for chart.

Metrics affected by selected range:

- Total Focus.
- Daily Average.
- Sessions.
- Completion Rate.
- Interrupted.
- Best Day.
- Chart.

Streaks remain global.

Chart behavior:

- 7D: 7 daily bars.
- 30D: scrollable compact daily bars.
- All: recent 30 days chart titled `Recent Focus Minutes`.

---

### 4.3 Analytics 3 - Mode Usage

Implemented Mode Usage section for:

- Normal Focus.
- Extreme Focus.
- Ultimate Focus.

Metrics per mode:

- Completed focus time.
- Completed sessions.
- Completion rate.
- Proportional horizontal bar.
- Most Effective badge.

Mode classification:

```text
Normal: !isExtremeMode && !isUltimateMode
Extreme: isExtremeMode && !isUltimateMode
Ultimate: isUltimateMode
```

Most Effective mode:

1. Highest completion rate.
2. Tie-breaker: highest completed focus time.
3. Ignore modes with no terminal sessions.

No data handling:

- If denominator is 0, show `No data`, not `0%`.

---

### 4.4 Analytics 4 - Compact Weekly Goal

Implemented compact Weekly Goal card inside Statistics.

Placement:

- Below range selector.
- Above Overview.

Behavior:

- Current calendar week Monday to Sunday.
- Counts completed focus time only.
- Does not include break time.
- Does not include interrupted/cancelled sessions.
- Independent of selected range.

Default target:

```text
120 min
```

Preset goal options:

```text
60 min
120 min
180 min
300 min
```

Display examples:

```text
2 / 60 min
3%
```

If reached:

```text
Goal reached
```

Progress bar caps visually at 100%, but text can show actual overachievement.

Storage:

- UserPreferences DataStore.
- Not Room.

---

### 4.5 Analytics 5 - Recent Session History

Implemented Recent Sessions section.

Shows latest 5 terminal sessions for selected range.

Terminal states:

- Completed.
- Cancelled.
- Emergency Exit.
- Failed.

Excluded:

- ActiveFocus.
- Break.
- Paused.
- Idle.

Row format:

```text
Today - Extreme Focus - 2 min - Completed
Today - Ultimate Focus - <1 min - Emergency Exit
```

Uses:

- terminal timestamp descending.
- completedAt or updatedAt preferred.
- createdAt fallback.

Duration:

- completed/accumulated focus duration only.
- no break time.
- interrupted sessions show accumulated focus, or 0 / <1 min if relevant.

Empty state:

```text
No sessions yet
```

---

## 5. Statistics UI Current Structure

Recommended/current layout:

```text
Statistics

[7D | 30D | All]

Weekly Goal
X / target min
[progress bar] Edit

Overview
Total Focus | Daily Average
Sessions    | Completion

Insights
Current Streak | Longest Streak
Interrupted    | Best Day

Focus Minutes - Last 7 Days / 30D / Recent Focus Minutes
[chart]

Mode Usage
Normal Focus
Extreme Focus
Ultimate Focus

Recent Sessions
latest terminal sessions
```

---

## 6. Backup and Restore

Important backup changes:

### Backup format versions

- v1: original backup DTO format.
- v2: added Extreme Mode.
- v3: added Ultimate Mode.

Current format:

```text
Backup format version 3
```

Rules:

- v1/v2 backups must remain importable.
- `isUltimateMode` defaults to false for old backups.
- On import, enforce consistency:

```text
if isExtremeMode == false, then isUltimateMode must be false
```

Backup/restore uses:

- DTO separation.
- Last Write Wins merge.
- Transactional restore.
- Non-destructive import.

Critical fix:

- Ultimate Mode state is now preserved during export/import.

---

## 7. Database and Migrations

Known migration milestones:

### Version 2 -> 3

- Added `created_at` and `updated_at` to `BlockedAppEntity`.
- Added diagnostic logs table.
- Preserved existing data.

### Version 3 -> 4

- Added `is_extreme_mode` to focus profiles.
- Added `is_extreme_mode` to sessions.
- Default false.

### Version 4 -> 5

- Added `is_ultimate_mode` to focus profiles.
- Added `is_ultimate_mode` to sessions.
- Default false.

Principle:

- Use explicit migrations.
- Preserve existing installs.
- Avoid destructive migration.

---

## 8. Permissions and Android System Integration

Important permissions and system integration:

- Accessibility permission for app detection and overlay enforcement.
- Exact Alarm permission for schedule precision.
- Notification permission / notification channels.
- Battery optimization exemption prompt for reliability.
- Vibration permission:

```xml
<uses-permission android:name="android.permission.VIBRATE" />
```

- WorkManager custom initialization with Hilt.
- Default WorkManagerInitializer must be removed when using on-demand/custom initialization.
- Foreground service configuration reviewed.
- System UI remains exempt from blocking for stability.

---

## 9. Major Bugs and Root Causes

### 9.1 WorkManager release lint failure

Error:

```text
Remove androidx.work.WorkManagerInitializer from your AndroidManifest.xml when using on-demand initialization
```

Fix:

- Removed WorkManagerInitializer via manifest tools removal.

---

### 9.2 Extreme Mode did not block Home/Chrome

Several root causes discovered over time:

- Launcher whitelisted.
- Overlay token null due to ApplicationContext WindowManager.
- Overlay dismissed itself on Rebel Focus accessibility event.
- Overlay was event-driven instead of session-driven.

Final fix:

- Session-driven overlay lifecycle.
- Service context used for accessibility overlay.
- Enforcement gate prevents self-dismissal.
- Overlay shows immediately on session start.

---

### 9.3 Break timer missing

Cause:

- Session state machine supported Break but service did not auto-transition.

Fix:

- FocusSessionService ticker triggers transition to Break and back to Focus or Completed.

---

### 9.4 ClassCastException in overlay theme

Crash:

```text
RebelFocusApp cannot be cast to android.app.Activity
```

Fix:

- Use safe Activity cast in Theme.

---

### 9.5 Vibration crash

Crash:

```text
SecurityException: android.permission.VIBRATE missing
```

Fix:

- Added VIBRATE permission.
- Guarded vibrator call.

---

### 9.6 Completion surface did not appear

Cause:

- Active session observer filtered out Completed and emitted null.

Fix:

- Added enforcement observer including Completed state.

---

### 9.7 Interval display off by one

Cause:

- UI used 0-based `pomodoroCount` directly.

Fix:

- Display mapping only.
- Domain logic unchanged.

---

### 9.8 Daily Average showed 0 min/day despite activity

Cause:

- Integer rounding below 1 minute/day.

Fix:

- Show `<1 min/day` for non-zero average under 1 minute/day.

---

## 10. Git and Release Workflow

### Repo

```text
https://github.com/Arthur160814/VC---RebelFocus.git
```

### Branches used

```text
main
release/1.1.0
release/1.1.1
```

### Current recommended flow

Create release branch from main:

```powershell
git checkout main
git pull origin main
git checkout -b release/1.1.1
git push -u origin release/1.1.1
```

Commit changes:

```powershell
git status
git add .
git commit -m "Your commit message"
git push
```

Merge release to main:

```powershell
git checkout main
git pull origin main
git merge release/1.1.1
git push origin main
```

Tag release:

```powershell
git tag v1.1.1
git push origin v1.1.1
```

Optional delete old release branch:

```powershell
git branch -d release/1.1.0
git push origin --delete release/1.1.0
```

### Fetch all branches

```powershell
git fetch --all --prune
git branch -a
```

If untracked README blocks pull:

```powershell
ren README.md README.local.md
git pull origin release/1.1.1
```

---

## 11. Build and Test Commands

Run all major checks:

```powershell
cd "C:\Users\MI PC\Desktop\focus_app"
.\gradlew.bat testDebugUnitTest assembleDebug assembleRelease
```

Individual commands:

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleRelease
```

APK paths:

```text
Debug APK:
app\build\outputs\apk\debug\app-debug.apk

Release APK:
app\build\outputs\apk\release\app-release.apk
```

Release APK may require proper signing. For device testing, debug APK is easiest.

---

## 12. ADB Device Installation

### Check devices

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" devices
```

Example output:

```text
RFCX80P4KCT     device
emulator-5554   device
```

### Install on physical device

```powershell
cd "C:\Users\MI PC\Desktop\focus_app"
.\gradlew.bat assembleDebug
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" -s RFCX80P4KCT install -r "app\build\outputs\apk\debug\app-debug.apk"
```

### Open app

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" -s RFCX80P4KCT shell monkey -p com.rebelfocus 1
```

### Clean install

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" -s RFCX80P4KCT uninstall com.rebelfocus
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" -s RFCX80P4KCT install "app\build\outputs\apk\debug\app-debug.apk"
```

### If Android blocks APK install for security

Recommended solution:

- Install via ADB.

Other option:

- Enable Install unknown apps for Files, Chrome, Drive, etc.
- Play Protect may need Install anyway.

---

## 13. Manual QA Checklist

### Session flows

- Normal session starts.
- Normal mode blocks only selected blocked apps.
- Extreme starts overlay immediately.
- Extreme has visible Emergency Exit.
- Ultimate starts overlay immediately.
- Ultimate hides visible Emergency Exit.
- Focus phase red.
- Break phase blue.
- Break timer works.
- Overlay remains through Focus and Break.
- Overlay covers Rebel Focus itself during strict modes.
- Emergency Exit works where available.
- Completion surface appears after natural completion.
- DONE closes completion surface.

### Overlay stability

- No flicker.
- No self-dismissal on Rebel Focus event.
- No background touch pass-through.
- No crash on overlay attach.
- No crash on phase change/haptics.
- System UI remains usable.

### Statistics

- 7D selected by default.
- 30D and All work.
- Total Time accurate.
- Daily Average accurate.
- `<1 min/day` formatting works.
- Completion Rate accurate.
- Interrupted count accurate.
- Best Day correct or No data.
- Streak singular/plural correct.
- Weekly chart shows focus minutes.
- 30D chart scrollable and readable.
- Weekly Goal updates and is range-independent.
- Mode Usage rows classify correctly.
- Most Effective badge correct.
- Recent Sessions shows last 5 terminal sessions.

### Backup

- Export backup with Ultimate profile.
- Reset app data.
- Import backup.
- Verify Extreme and Ultimate settings preserved.
- Older backups import without crash.
- Ultimate false by default for old backups.

### Permissions

- Accessibility prompt works.
- Exact alarm prompt works.
- Battery optimization warning works.
- Notification channels exist.
- Vibration no longer crashes.

### Build

```powershell
.\gradlew.bat testDebugUnitTest assembleDebug assembleRelease
```

All should pass.

---

## 14. README Request

A general README was requested for the project.

Requirements for README:

- Project name: Rebel Focus.
- Short description.
- Main features at high level.
- Tech stack.
- Getting started.
- Build commands.
- Permissions note.
- Project status.
- Professional concise tone.
- No phase history, no debug notes, no long internal walkthrough.

---

## 15. Suggested Future Roadmap

No more features were recommended immediately before release. Current recommendation is to stabilize and polish.

Possible future phases:

### Future 1 - Full Session History Screen

- View all sessions.
- Filter by mode.
- Filter by state.
- Sort by date.
- Helps transparency and analytics debugging.

### Future 2 - 30-Day Heatmap

- GitHub-like visual activity map.
- Each square = one day.
- Color intensity = focus minutes.

### Future 3 - Focus Recommendation

Examples:

```text
Your most effective mode is Extreme.
Try a 25 min Extreme session today.
```

### Future 4 - Session Intention

Before starting:

```text
What are you focusing on?
```

Then show intention in overlay and history.

### Future 5 - Goals Advanced

- Daily goals.
- Monthly goals.
- Goal history.
- Weekly goal streak.

### Future 6 - Recovery After Failed Session

After Emergency Exit:

```text
What happened?
Too hard / wrong app / urgent access
```

This can improve insight quality.

### Future 7 - Onboarding Upgrade

Better explain:

- Normal.
- Extreme.
- Ultimate.
- Android system limits.
- Required permissions.

---

## 16. Important Product Decisions

### Extreme Mode

- Always-on overlay.
- Covers Rebel Focus itself.
- Has visible Emergency Exit.
- Runs during Focus and Break.

### Ultimate Mode

- Always-on overlay.
- Covers Rebel Focus itself.
- No visible Emergency Exit.
- User waits until session finishes.

### Completion Screen

- Appears only after natural completion.
- Not after Emergency Exit.
- Shows Well Done.
- Shows total focus time.
- Shows completed intervals.
- DONE dismisses summary.

### Statistics

- Focus time never includes break time.
- Completed sessions count for completed focus.
- Interrupted sessions affect completion rate.
- Active sessions excluded from terminal analytics.
- Weekly Goal is independent of selected range.
- Streaks remain global.

### Versioning

- Use semantic versioning.
- Current line is 1.1.x.
- Next patch after 1.1.1 is 1.1.2.
- Next feature set could be 1.2.0.

---

## 17. Known Current State

As of the end of this chat:

- Project is pushed to GitHub.
- A release branch `release/1.1.1` exists locally and remotely.
- The app can be installed on a physical phone via ADB.
- Physical device detected as:

```text
RFCX80P4KCT
```

- Emulator detected as:

```text
emulator-5554
```

- ADB install to the physical phone succeeded.
- The app is usable on device.
- Security install issue was bypassed by ADB.

---

## 18. Common Commands Reference

### Git status

```powershell
git status
git branch
git branch -a
```

### Pull branch

```powershell
git checkout release/1.1.1
git pull origin release/1.1.1
```

### Commit

```powershell
git add .
git commit -m "Message"
git push
```

### Build

```powershell
.\gradlew.bat assembleDebug
.\gradlew.bat testDebugUnitTest
.\gradlew.bat assembleRelease
```

### ADB devices

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" devices
```

### ADB install physical phone

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" -s RFCX80P4KCT install -r "app\build\outputs\apk\debug\app-debug.apk"
```

### ADB open app

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" -s RFCX80P4KCT shell monkey -p com.rebelfocus 1
```

### Logcat crash filter

```powershell
& "C:\Users\MI PC\AppData\Local\Android\Sdk\platform-tools\adb.exe" logcat | Select-String "FATAL EXCEPTION|AndroidRuntime|Process: com.rebelfocus|Caused by:"
```

---

## 19. Final Recommendation

The project is now at a strong release candidate state. The next best step is not to add new product features immediately. The recommended next actions are:

1. Run one full manual QA pass on device.
2. Ensure `release 1.1.1` footer is correct.
3. Ensure backup/restore preserves Ultimate Mode.
4. Ensure ADB install works on phone.
5. Merge `release/1.1.1` to `main` when satisfied.
6. Tag `v1.1.1`.
7. Only then consider future roadmap items such as full session history or heatmap.

---

## 20. Final Release Commands

When ready to finalize:

```powershell
git checkout main
git pull origin main
git merge release/1.1.1
git push origin main
git tag v1.1.1
git push origin v1.1.1
```

Optional APK copy:

```powershell
copy "app\build\outputs\apk\debug\app-debug.apk" "RebelFocus-v1.1.1-debug.apk"
```

For release APK, proper signing may be needed:

```powershell
copy "app\build\outputs\apk\release\app-release.apk" "RebelFocus-v1.1.1-release.apk"
```

---

End of handoff.
