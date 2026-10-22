# RoutineFlow 2.0 integration contracts

This worktree currently owns the pure `PlannerEngine` domain slice. It does not change the
Room schema, navigation shell, or shared Compose screens.

## Planner contract

`PlannerEngine.plan(PlannerRequest)` is deterministic and offline-only. The caller supplies
tasks with stable IDs, estimated minutes, priority, optional `LocalDateTime` deadlines, and
optional existing calendar/time blocks. The result contains approved-proposal candidates in
`scheduled`, explicit `unscheduled` reasons, available minutes, scheduled minutes, and an
`isOverloaded` guardrail. The engine never mutates tasks or silently changes deadlines.

The future Planner screen should:

1. Convert persisted epoch timestamps into the user's current `LocalDateTime`.
2. Display `scheduled` blocks as a proposal requiring user confirmation.
3. Explain `unscheduled` reasons and offer Recovery Mode or a smaller plan.
4. Persist accepted time blocks through a separate `DailyPlan`/schedule contract.
5. Keep timezone conversion at the UI/data boundary; the engine itself is timezone-neutral.

## Ownership boundary

Do not duplicate scheduling logic in Compose or a ViewModel. Extend this engine or add a
separate adapter around it. Room migrations and navigation remain shared integration work.
