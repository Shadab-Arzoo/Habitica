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
- Persistent productivity tasks with title, notes, category, priority, estimate, energy-ready fields, due timestamp, archive state, and completion history.
- Atomic task completion with a unique `(taskId, date)` record to prevent duplicate completion rewards.
- Task creation and completion controls on Today, including an offline-friendly empty state and priority cues.
- Planner destination with deterministic, approval-only daily scheduling proposals, deadline awareness, workload indicators, and explicit unscheduled-task explanations.
- First-run onboarding with animated welcome slides, selectable focus areas, editable starter task suggestions, a reviewable plan, and a final confirmation before Room persistence.
- Native Liquid Glass visual system with adaptive content widths, Apple-inspired silver-white/blue light styling, readable dark mode, selective glossy surfaces, continuous dock drag highlighting, directional screen transitions, and a user-controlled reduced-motion mode.

## Using the widgets

After installing the debug APK, long-press the Android launcher and choose **Widgets**. Add either:

- **RoutineFlow** — today’s completed habits versus active habits.
- **RoutineFlow Week** — the last seven days of check-ins.

Both widgets refresh periodically and are refreshed immediately when a habit is added, completed, or archived.

## Habit reminders

When creating a habit, enter an optional daily time in `HH:mm` format, such as `08:30`.
RoutineFlow schedules an offline Android reminder with a rotating encouraging quote. Reminders
use the device's local time and are scheduled daily.

## First-run onboarding

New users see a short welcome journey before the main dashboard. Intro slides can be skipped,
focus areas are optional, and suggested tasks remain temporary until the user explicitly confirms
the starter plan. Confirmed tasks are inserted through the Room repository with active
title/category deduplication so a retry cannot create duplicate starter tasks. Existing users with
persisted habits or tasks bypass onboarding automatically; onboarding preferences are stored in
DataStore. Notification permission is not requested during onboarding or immediately at launch.

## Build

```bash
./gradlew tasks
./gradlew testDebugUnitTest
```

The project is configured for Android SDK 35 and Java 17 toolchains. An Android SDK is required for compilation and tests; emulator/device verification is not available in the current workspace.

## Visual system and motion

RoutineFlow uses an Android-native interpretation of Liquid Glass rather than copying
proprietary Apple assets, fonts, or layouts. The shared design direction follows the
provided MockFlow skill: a silver-white `#F2F2F7` background, white glass slabs, blue
`#007AFF` accents, deep-gray body text, 20dp cards, and 12dp controls. `GlassSurface`
provides reusable translucent fills, glossy top-to-bottom sheen, thin light borders,
soft ambient elevation, and a simplified rendering mode for lower-power or reduced-motion
configurations. Broad backdrop blur and continuous shader effects are intentionally avoided.

The bottom dock supports ordinary taps and a held horizontal swipe: its single highlight
follows the finger continuously and the tab under the release point opens. The Settings
screen exposes **Reduce Motion**, which minimizes dock and screen-transition animation while
preserving navigation and accessibility semantics. Primary list screens use centered,
bounded content on larger windows and full-width content on compact phones.

## Implementation roadmap

The current release foundation is implemented and verified through Kotlin compilation and unit tests.
The next milestones are task editing/details, subtasks UI, recurrence generation, calendar/weekly planning,
Recovery Mode for task backlogs, Minimum Viable Day, focus sessions, achievements, backup/restore, and
Compose/instrumentation coverage. These should be added incrementally without replacing the existing habit,
reminder, widget, graph, and recovery behavior.
