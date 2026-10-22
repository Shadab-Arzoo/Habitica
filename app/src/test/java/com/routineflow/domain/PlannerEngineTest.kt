package com.routineflow.domain

import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class PlannerEngineTest {
    private val date = LocalDate.of(2026, 10, 19)

    @Test
    fun schedulesUrgentDeadlineBeforeLowerPriorityWork() {
        val result = PlannerEngine.plan(
            PlannerRequest(
                date = date,
                workingStart = LocalTime.of(9, 0),
                workingEnd = LocalTime.of(12, 0),
                breakMinutes = 0,
                tasks = listOf(
                    PlannableTask(1, "Long project", 60, priority = 1),
                    PlannableTask(
                        2,
                        "Exam revision",
                        30,
                        priority = 3,
                        dueAt = LocalDateTime.of(date, LocalTime.of(10, 0))
                    )
                )
            )
        )

        assertEquals("Exam revision", result.scheduled.first().task.title)
        assertEquals(LocalTime.of(9, 0), result.scheduled.first().start.toLocalTime())
        assertEquals(2, result.scheduled.size)
    }

    @Test
    fun respectsExistingBlocksAndReportsOverload() {
        val result = PlannerEngine.plan(
            PlannerRequest(
                date = date,
                workingStart = LocalTime.of(9, 0),
                workingEnd = LocalTime.of(11, 45),
                breakMinutes = 0,
                existingBlocks = listOf(
                    ExistingTimeBlock(
                        LocalDateTime.of(date, LocalTime.of(9, 30)),
                        LocalDateTime.of(date, LocalTime.of(10, 30)),
                        "Class"
                    )
                ),
                tasks = listOf(
                    PlannableTask(1, "Study", 45),
                    PlannableTask(2, "Walk", 45)
                )
            )
        )

        assertEquals(1, result.scheduled.size)
        assertEquals(1, result.unscheduled.size)
        assertTrue(result.isOverloaded)
        assertEquals(LocalTime.of(10, 30), result.scheduled.first().start.toLocalTime())
    }

    @Test
    fun doesNotScheduleTaskAfterItsDeadline() {
        val result = PlannerEngine.plan(
            PlannerRequest(
                date = date,
                workingStart = LocalTime.of(9, 0),
                workingEnd = LocalTime.of(17, 0),
                tasks = listOf(
                    PlannableTask(
                        1,
                        "Late task",
                        30,
                        dueAt = LocalDateTime.of(date, LocalTime.of(9, 15))
                    )
                )
            )
        )

        assertTrue(result.scheduled.isEmpty())
        assertEquals(UnscheduledReason.DoesNotFitBeforeDeadline, result.unscheduled.single().reason)
    }

    @Test
    fun duplicateTaskIdsAreNotRewardedWithDuplicateBlocks() {
        val result = PlannerEngine.plan(
            PlannerRequest(
                date = date,
                tasks = listOf(
                    PlannableTask(1, "Same task", 20),
                    PlannableTask(1, "Same task copy", 20)
                )
            )
        )

        assertEquals(1, result.scheduled.size)
    }
}
