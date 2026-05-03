# Rebel Focus

Rebel Focus is a focus and productivity Android application designed to help users regain control over their time through structured Pomodoro sessions, rigorous app blocking, and deep focus analytics.

## Features

- **Pomodoro Sessions**: Customizable focus and break intervals to maintain peak productivity.
- **App Blocking**: Restrict access to distracting applications during active sessions.
- **Focus Modes**:
    - **Normal Mode**: Standard focus tracking and app blocking.
    - **Extreme Mode**: Hardened blocking with restricted exit options.
    - **Ultimate Extreme Mode**: The highest level of enforcement with zero emergency exits.
- **Weekly Goals**: Set and track focus targets to build consistent habits.
- **Statistics & Insights**: Comprehensive analytics including total focus time, daily averages, and completion rates.
- **Session History**: Detailed logs of recent sessions and their outcomes.
- **Backup & Restore**: Securely export and import your profiles and settings.

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Database**: Room (SQLite)
- **Data Persistence**: DataStore
- **Background Tasks**: WorkManager
- **Dependency Injection**: Hilt
- **Build System**: Gradle

## Getting Started

### Prerequisites

- Android Studio Jellyfish or newer
- JDK 17

### Setup

1. Clone the repository:
   ```bash
   git clone https://github.com/Arthur160814/VC---RebelFocus.git
   ```
2. Open the project in **Android Studio**.
3. Sync project with Gradle files.
4. Run the `app` module on a physical device or emulator (API 26+).

## Build Commands

Use the following commands for development and testing:

```bash
# Build debug APK
./gradlew assembleDebug

# Run unit tests
./gradlew testDebugUnitTest

# Build production release APK
./gradlew assembleRelease
```

## Permissions Note

Rebel Focus requires several system-level permissions to provide full functionality:
- **Accessibility Service**: Required for app blocking and session enforcement.
- **Notifications**: Required for timer updates and phase transitions.
- **Exact Alarms**: Required for precise Pomodoro timing.
- **Battery Optimization Exemption**: Required to ensure focus sessions are not interrupted by system power management.

## Project Status

This project is under active development.

---
*This project was vibecoded.*
