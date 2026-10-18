# RoutineFlow

RoutineFlow is a native, offline-first Android habit app built with Kotlin, Jetpack Compose, Material 3, Room, DataStore, Coroutines/Flow, and Hilt.

## Current foundation

- Liquid Glass-inspired `GlassSurface` with restrained translucency, borders, and no full-screen blur.
- Room schema for habits, unique daily completions, and persisted recovery plans.
- Duplicate completion protection through a `(habitId, date)` primary key and conflict-ignore insert.
- Flow-driven Today state with progress, XP, habit completion, archive, and add-habit flows.
- Recovery rules that count only scheduled weekdays and trigger after two consecutive scheduled misses.
- Progress and Settings surfaces ready for the remaining preference and analytics flows.
- Two native home-screen widgets: Today momentum and weekly check-ins.
- A 16-week GitHub-style contribution heatmap in Progress, using persisted Room completion records.

## Using the widgets

After installing the debug APK, long-press the Android launcher and choose **Widgets**. Add either:

- **RoutineFlow** — today’s completed habits versus active habits.
- **RoutineFlow Week** — the last seven days of check-ins.

Both widgets refresh periodically and are refreshed immediately when a habit is added, completed, or archived.

## Habit reminders

When creating a habit, enter an optional daily time in `HH:mm` format, such as `08:30`.
RoutineFlow schedules an offline Android reminder with a rotating encouraging quote. On
Android 13 and newer, allow notification permission when the app requests it. Reminders
use the device's local time and are scheduled daily.

## Build

```bash
./gradlew tasks
./gradlew testDebugUnitTest
```

The project is configured for Android SDK 35 and Java 17 toolchains. An Android SDK is required for compilation and tests; emulator/device verification is not available in the current workspace.
