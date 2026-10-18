package com.routineflow.domain

import com.routineflow.data.local.CompletionEntity
import java.time.DayOfWeek
import java.time.LocalDate

object RecoveryRules {
    fun consecutiveMisses(
        scheduledWeekdays: Set<DayOfWeek>,
        completions: List<CompletionEntity>,
        today: LocalDate = LocalDate.now()
    ): Int {
        if (scheduledWeekdays.isEmpty()) return 0
        val completed = completions.map { LocalDate.parse(it.date) }.toSet()
        var cursor = today
        var misses = 0
        while (cursor >= today.minusDays(30)) {
            if (cursor.dayOfWeek in scheduledWeekdays) {
                if (cursor in completed) {
                    break
                } else {
                    misses++
                }
            }
            cursor = cursor.minusDays(1)
        }
        return misses
    }

    fun shouldSuggestRecovery(misses: Int): Boolean = misses >= 2
}
