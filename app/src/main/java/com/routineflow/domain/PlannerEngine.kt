package com.routineflow.domain

import java.time.Duration
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

data class PlannableTask(
    val id: Long,
    val title: String,
    val estimatedMinutes: Int,
    val category: String = "General",
    val priority: Int = 1,
    val dueAt: LocalDateTime? = null,
    val energy: Int = 1
) {
    init {
        require(title.isNotBlank()) { "Task title must not be blank" }
        require(estimatedMinutes > 0) { "Task duration must be positive" }
        require(priority in 0..3) { "Task priority must be between 0 and 3" }
        require(energy in 0..2) { "Task energy must be between 0 and 2" }
    }
}

data class ExistingTimeBlock(
    val start: LocalDateTime,
    val end: LocalDateTime,
    val label: String = "Busy"
) {
    init {
        require(end.isAfter(start)) { "Time block end must be after start" }
    }
}

data class PlannerRequest(
    val date: LocalDate,
    val workingStart: LocalTime = LocalTime.of(9, 0),
    val workingEnd: LocalTime = LocalTime.of(17, 0),
    val breakMinutes: Int = 10,
    val existingBlocks: List<ExistingTimeBlock> = emptyList(),
    val tasks: List<PlannableTask>
) {
    init {
        require(workingEnd.isAfter(workingStart)) { "Working hours must be valid" }
        require(breakMinutes >= 0) { "Break duration cannot be negative" }
        require(existingBlocks.zipWithNext().none { it.first.end.isAfter(it.second.start) }) {
            "Existing time blocks must not overlap"
        }
    }
}

data class PlannedBlock(
    val task: PlannableTask,
    val start: LocalDateTime,
    val end: LocalDateTime
)

sealed interface UnscheduledReason {
    data object NoTimeRemaining : UnscheduledReason
    data object DeadlinePassed : UnscheduledReason
    data object DoesNotFitBeforeDeadline : UnscheduledReason
}

data class UnscheduledTask(
    val task: PlannableTask,
    val reason: UnscheduledReason
)

data class PlannerResult(
    val scheduled: List<PlannedBlock>,
    val unscheduled: List<UnscheduledTask>,
    val availableMinutes: Int,
    val scheduledMinutes: Int
) {
    val workloadRatio: Float
        get() = if (availableMinutes == 0) 0f else scheduledMinutes.toFloat() / availableMinutes

    val isOverloaded: Boolean
        get() = unscheduled.isNotEmpty() || workloadRatio > 0.9f
}

/**
 * Deterministic, approval-only day planning.
 *
 * Tasks are ordered by urgency (deadline, priority, then shorter duration) and placed
 * into the earliest available gap. This engine never mutates tasks, deadlines, or
 * existing blocks; callers present the result as a proposal.
 */
object PlannerEngine {
    fun plan(request: PlannerRequest): PlannerResult {
        val dayStart = LocalDateTime.of(request.date, request.workingStart)
        val dayEnd = LocalDateTime.of(request.date, request.workingEnd)
        val occupied = request.existingBlocks
            .filter { it.end.isAfter(dayStart) && it.start.isBefore(dayEnd) }
            .map { it.copy(start = maxOf(it.start, dayStart), end = minOf(it.end, dayEnd)) }
            .sortedBy { it.start }
            .toMutableList()

        val availableMinutes = freeMinutes(dayStart, dayEnd, occupied)
        val scheduled = mutableListOf<PlannedBlock>()
        val unscheduled = mutableListOf<UnscheduledTask>()
        val sortedTasks = request.tasks
            .distinctBy { it.id }
            .sortedWith(
                compareBy<PlannableTask> { it.dueAt == null }
                    .thenBy { it.dueAt }
                    .thenByDescending { it.priority }
                    .thenBy { it.estimatedMinutes }
            )

        sortedTasks.forEach { task ->
            val deadline = task.dueAt
            if (deadline != null && !deadline.isAfter(dayStart)) {
                unscheduled += UnscheduledTask(task, UnscheduledReason.DeadlinePassed)
                return@forEach
            }
            val latestEnd = minOf(dayEnd, deadline ?: dayEnd)
            val start = findGap(dayStart, latestEnd, task.estimatedMinutes, occupied, request.breakMinutes)
            if (start == null) {
                val reason = if (deadline != null && deadline.isBefore(dayEnd)) {
                    UnscheduledReason.DoesNotFitBeforeDeadline
                } else {
                    UnscheduledReason.NoTimeRemaining
                }
                unscheduled += UnscheduledTask(task, reason)
                return@forEach
            }
            val end = start.plusMinutes(task.estimatedMinutes.toLong())
            scheduled += PlannedBlock(task, start, end)
            occupied += ExistingTimeBlock(start, end, task.title)
            occupied.sortBy { it.start }
        }

        return PlannerResult(
            scheduled = scheduled,
            unscheduled = unscheduled,
            availableMinutes = availableMinutes,
            scheduledMinutes = scheduled.sumOf { Duration.between(it.start, it.end).toMinutes().toInt() }
        )
    }

    private fun findGap(
        start: LocalDateTime,
        latestEnd: LocalDateTime,
        durationMinutes: Int,
        occupied: List<ExistingTimeBlock>,
        breakMinutes: Int
    ): LocalDateTime? {
        var cursor = start
        occupied.sortedBy { it.start }.forEach { block ->
            if (!block.end.isAfter(cursor)) return@forEach
            val gapEnd = minOf(block.start, latestEnd)
            if (gapEnd.isAfter(cursor) &&
                Duration.between(cursor, gapEnd).toMinutes() >= durationMinutes
            ) {
                return cursor
            }
            cursor = maxOf(cursor, block.end.plusMinutes(breakMinutes.toLong()))
            if (!cursor.isBefore(latestEnd)) return null
        }
        return if (Duration.between(cursor, latestEnd).toMinutes() >= durationMinutes) cursor else null
    }

    private fun freeMinutes(
        dayStart: LocalDateTime,
        dayEnd: LocalDateTime,
        occupied: List<ExistingTimeBlock>
    ): Int {
        var cursor = dayStart
        var free = 0L
        occupied.forEach { block ->
            if (block.start.isAfter(cursor)) {
                free += Duration.between(cursor, block.start).toMinutes()
            }
            cursor = maxOf(cursor, block.end)
        }
        if (cursor.isBefore(dayEnd)) free += Duration.between(cursor, dayEnd).toMinutes()
        return free.toInt()
    }
}
