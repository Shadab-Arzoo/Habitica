# RoutineFlow

RoutineFlow is a native, offline-first Android habit app built with Kotlin, Jetpack Compose, Material 3, Room, DataStore, Coroutines/Flow, and Hilt.

## Current foundation

- Liquid Glass-inspired `GlassSurface` with restrained translucency, borders, and no full-screen blur.
- Room schema for habits, unique daily completions, and persisted recovery plans.
- Duplicate completion protection through a `(habitId, date)` primary key and conflict-ignore insert.
- Flow-driven Today state with progress, XP, habit completion, archive, and add-habit flows.
- Recovery rules that count only scheduled weekdays and trigger after two consecutive scheduled misses.
- Progress and Settings surfaces ready for the remaining preference and analytics flows, with persisted light/dark theme selection.
- Two native home-screen widgets: Today momentum and weekly check-ins.
- A task-powered Gaming garden with persistent daily water totals, five distinct low-poly tree growth stages, a 2.5D view, an interactive 3D-style perspective, and selectable historical days.
- Persistent productivity tasks with title, notes, category, priority, estimate, energy-ready fields, due timestamp, archive state, and completion history.
- Atomic task completion with a unique `(taskId, date)` record to prevent duplicate completion rewards.
- Task creation and completion controls on Today, including an offline-friendly empty state and priority cues.
- Planner destination with deterministic, approval-only daily scheduling proposals, deadline awareness, workload indicators, and explicit unscheduled-task explanations.
- First-run onboarding with animated welcome slides, selectable focus areas, editable starter task suggestions, a reviewable plan, and a final confirmation before Room persistence.
- Native Liquid Glass visual system with adaptive content widths, Apple-inspired silver-white/blue light styling, readable dark mode, selective glossy surfaces, continuous dock drag highlighting, directional screen transitions, and a user-controlled reduced-motion mode.
- Task completion history is stored per task and date with duplicate-safe Room writes; toggling a task off removes only the current day's completion record so tree growth updates immediately without duplicate water rewards.

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
proprietary Apple assets, fonts, or layouts. The shared design system uses a cool
silver-white light background, deep navy dark mode, electric blue primary actions,
violet secondary accents, rose tertiary feedback, rounded 24dp cards, and 16dp controls.
Typography uses the platform sans-serif family with a stronger display-to-body hierarchy.
`GlassSurface` provides reusable translucent fills, glossy top-to-bottom sheen, thin light
borders, soft ambient elevation, and a simplified rendering mode for lower-power or
reduced-motion configurations. Broad backdrop blur and continuous shader effects are
intentionally avoided. Theme selection is persisted in DataStore and falls back to the
system theme only before a user preference has been saved.

The bottom dock supports ordinary taps and a held horizontal swipe: its single highlight
follows the finger continuously and the tab under the release point opens. The Settings
screen exposes **Reduce Motion**, which minimizes dock and screen-transition animation while
preserving navigation and accessibility semantics. Primary list screens use centered,
bounded content on larger windows and full-width content on compact phones.

## ClearPDF dock reference

The dock motion pass was implemented after inspecting ClearPDF's MIT-licensed
`LiquidBottomTabs`, `LiquidBottomTab`, `DampedDragAnimation`, `GlassMotion`, and
`InteractiveHighlight` sources. RoutineFlow does not import ClearPDF or its Kyant backdrop
module; instead, `ClearPdfDockMotion` is a native Compose adaptation of the reference's
fractional tab tracking, spring-damped release, press scaling, and no-ripple interaction model.
The ClearPDF repository and its license/notice files remain the authoritative reference for
that behavior.

## Implementation roadmap

The current release foundation is implemented and verified through Kotlin compilation and unit tests.
The next milestones are task editing/details, subtasks UI, recurrence generation, calendar/weekly planning,
Recovery Mode for task backlogs, Minimum Viable Day, focus sessions, achievements, backup/restore, and
Compose/instrumentation coverage. These should be added incrementally without replacing the existing habit,
reminder, widget, graph, and recovery behavior.
