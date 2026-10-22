package com.routineflow.ui

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OnboardingTemplatesTest {
    @Test
    fun selectedCategoriesProduceOnlyRelevantDeduplicatedSuggestions() {
        val suggestions = onboardingTaskTemplates(setOf("study", "health"))

        assertEquals(suggestions.size, suggestions.map { it.title }.distinct().size)
        assertTrue(suggestions.any { it.title == "Study a topic for 30 minutes." })
        assertTrue(suggestions.any { it.title == "Take a 20-minute walk." })
        assertTrue(suggestions.none { it.title == "Fix one outstanding bug." })
    }
}
