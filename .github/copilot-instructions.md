# RoutineFlow Copilot Skills

Use these repository-specific skills whenever you modify RoutineFlow. Prefer small, verifiable changes over speculative architecture.

## 1. Native Android architecture skill

- Use Kotlin, Jetpack Compose, Material 3, Navigation Compose, Room, Coroutines/Flow, Hilt, DataStore, and WorkManager.
- Keep UI, domain, and data responsibilities separate.
- Use immutable UI state and unidirectional data flow.
- Keep database and serialization work off the main thread.
- Prefer existing repositories, DAOs, and design-system components before adding new abstractions.
- Do not add React Native, Flutter, web views, networking, account systems, ads, subscriptions, or payment SDKs unless explicitly requested.

## 2. Compose performance skill

- Collect flows with `collectAsStateWithLifecycle`.
- Keep state as close as possible to the composable that owns it.
- Use stable keys for lazy-list items.
- Use `remember` and `derivedStateOf` only where they prevent actual repeated work.
- Avoid database calls, expensive calculations, or side effects directly inside composables.
- Avoid full-screen blur, stacked real-time effects, continuous animations, and unnecessary nested layouts.
- Prefer `AnimatedVisibility`, `AnimatedContent`, and spring motion for meaningful state changes only.
- Respect reduced-motion preferences when adding or changing animation.

## 3. Liquid Glass UI/UX skill

- Reuse `GlassSurface` for translucent surfaces rather than creating one-off cards.
- Keep surfaces legible in both light and dark themes.
- Use restrained opacity, borders, highlights, corner radii, and shadows.
- Prefer overlays and subtle gradients over expensive backdrop blur.
- Maintain accessible contrast and minimum touch targets.
- Use meaningful content descriptions; decorative icons and emoji should not be announced redundantly.
- Avoid copying Apple icons, proprietary assets, exact layouts, or branding.

## 4. Habit and scheduling correctness skill

- A completion is unique per `(habitId, date)` and repeated taps must never award duplicate XP.
- Never delete historical completions when editing, archiving, or recovering a habit.
- Unscheduled weekdays must not count as misses or failures.
- Recovery logic belongs in domain code, not UI code.
- Treat dates and time zones explicitly; use `java.time` and avoid device-local string assumptions in new persistence logic.
- Persist recovery-plan status and avoid repeatedly resurfacing dismissed suggestions.
- Add unit tests for every scheduling or recovery edge case.

## 5. Room/data skill

- Add indexes for foreign keys and frequent date-range queries.
- Use explicit migrations when changing the schema; do not destructively recreate user data.
- Use conflict strategies intentionally and test them.
- Expose `Flow` for observable data and suspend functions for one-shot writes.
- Reuse the singleton database instance and avoid creating executors or databases per event.
- Keep query projections small for widgets and summaries.

## 6. Widget skill

- Widgets must use `RemoteViews`-compatible layouts and remain useful when data is temporarily unavailable.
- Keep widget queries small and execute them away from the main thread.
- Refresh widgets after relevant data changes and on the provider’s periodic schedule.
- Do not create a new executor for every refresh; reuse a bounded executor or use WorkManager when periodic work needs durable scheduling.
- Log refresh failures with enough context to diagnose them without exposing user data.
- Provide widget descriptions, previews, resize metadata, and accessible text.

## 7. Gamification skill

- XP and levels must be derived from persisted, non-duplicated completion records.
- Never penalize missed days or award XP for unscheduled days.
- Keep feedback encouraging and avoid manipulative streak-loss language.
- Test duplicate taps, process restarts, archive/recovery transitions, and date boundaries.

## 8. Accessibility and compatibility skill

- Support system insets and edge-to-edge layouts.
- Preserve usability with large font scales and different screen sizes.
- Keep controls at least 48dp where practical.
- Add semantic labels for meaningful actions and state.
- Respect system dark mode and reduced motion.
- Avoid relying on color alone to communicate completion or intensity.
- Verify Back behavior for dialogs and navigation.

## 9. Testing skill

- Run the smallest relevant test task first, then broaden only when needed.
- Add pure unit tests for recovery, scheduling, XP, and date logic.
- Add Room tests for uniqueness, persistence, indexes, and migrations.
- Add Compose UI tests for create, complete, duplicate-tap, archive, navigation, and dialog flows.
- Do not claim APK, emulator, or instrumentation success unless the Android SDK and device/emulator are available.
- When tooling is unavailable, state the exact blocked command and reason.

## 10. Reliability and security skill

- Validate user input at the boundary and show explicit errors instead of silent fallbacks.
- Do not log habit names, schedules, or completion history unnecessarily.
- Keep local backup/export user-controlled and avoid implicit network uploads.
- Request notification permission only when needed and explain its value.
- Do not catch broad exceptions without logging or a user-visible recovery path.
- Review exported components, widget receivers, file access, and backup behavior when changing app boundaries.

## 11. Change-management skill

- Inspect related code before editing.
- Preserve unrelated worktree changes.
- Keep changes surgical and update directly related documentation.
- Follow existing naming, formatting, and package conventions.
- After meaningful changes, run Gradle configuration, relevant tests, lint, or build checks that the environment supports.
