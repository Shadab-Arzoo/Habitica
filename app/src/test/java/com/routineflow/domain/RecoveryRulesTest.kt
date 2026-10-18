package com.routineflow.domain

import com.routineflow.data.local.CompletionEntity
import java.time.DayOfWeek
import java.time.LocalDate
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class RecoveryRulesTest {
    private val monday = LocalDate.of(2026, 10, 12)

    @Test
    fun unscheduledDaysDoNotCountAsMisses() {
        val today = monday.plusDays(6)
        val misses = RecoveryRules.consecutiveMisses(
            scheduledWeekdays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            completions = listOf(CompletionEntity(1, monday.plusDays(2).toString())),
            today = today
        )
        assertEquals(1, misses)
    }

    @Test
    fun twoScheduledMissesTriggerRecovery() {
        val today = monday.plusDays(4)
        val misses = RecoveryRules.consecutiveMisses(
            scheduledWeekdays = setOf(DayOfWeek.MONDAY, DayOfWeek.WEDNESDAY, DayOfWeek.FRIDAY),
            completions = listOf(CompletionEntity(1, monday.toString())),
            today = today
        )
        assertTrue(RecoveryRules.shouldSuggestRecovery(misses))
    }
}
