package com.example.meridian

import com.example.meridian.data.repository.DemoRepo
import com.example.meridian.data.timeline.TimelineTemplates
import org.junit.Assert.*
import org.junit.Test

class Phase2Test {

    @Test
    fun `timeline templates library has at least 20 calibrated templates with variants`() {
        assertTrue(TimelineTemplates.templates.size >= 20)
        TimelineTemplates.templates.forEach { template ->
            assertTrue(template.variants.isNotEmpty())
            assertTrue(template.variants.all { it.isNotBlank() })
            assertNotNull(template.emoji)
        }
    }

    @Test
    fun `streak model protects cumulative days and handles rest buffer`() {
        val repo = DemoRepo()
        val streak = repo.streak.value

        assertEquals(312, streak.cumulativeDaysConnected)
        assertEquals(18, streak.currentStreak)
        assertEquals(2, streak.restDaysAvailable)
        assertTrue(streak.isOptedIn)
    }

    @Test
    fun `visits log tracks both past and upcoming reunions`() {
        val repo = DemoRepo()
        val visits = repo.visits.value

        val pastVisits = visits.filter { it.isPast }
        val upcomingVisits = visits.filter { !it.isPast }

        assertEquals(3, pastVisits.size)
        assertEquals(1, upcomingVisits.size)
        assertEquals(27, pastVisits.sumOf { it.durationDays })
        assertEquals("Pune", upcomingVisits.first().place)
    }
}
