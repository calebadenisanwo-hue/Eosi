package com.example.meridian

import com.example.meridian.data.dates.DateIdeasData
import com.example.meridian.data.plan.PlanData
import org.junit.Assert.*
import org.junit.Test

class Phase4Test {

    @Test
    fun `date generator bank contains exactly 40 ideas`() {
        assertEquals(40, DateIdeasData.ideas.size)
        assertTrue(DateIdeasData.ideas.all { it.title.isNotBlank() && it.description.isNotBlank() })
    }

    @Test
    fun `roadmap stages cover all 4 stages from exploring to closing distance`() {
        val stages = PlanData.initialStages
        assertEquals(4, stages.size)
        assertEquals(1, stages[0].stageNumber)
        assertEquals("Exploring & Research", stages[0].name)
        assertEquals("COMPLETED", stages[0].status)

        assertEquals(2, stages[1].stageNumber)
        assertEquals("Decided & Committed", stages[1].name)
        assertEquals("IN_PROGRESS", stages[1].status)

        assertEquals(4, stages[3].stageNumber)
        assertEquals("Closing the Distance", stages[3].name)
    }

    @Test
    fun `shared goal fund computes correct progress percentage`() {
        val target = PlanData.sharedGoalTarget
        val current = PlanData.sharedGoalCurrent
        val pct = (current / target) * 100.0

        assertEquals(12000.0, target, 0.01)
        assertEquals(6850.0, current, 0.01)
        assertEquals(57.08, pct, 0.1)
    }

    @Test
    fun `visit planner packing list and itinerary are initialized`() {
        val packing = PlanData.packingItems
        val itinerary = PlanData.itineraryItems

        assertTrue(packing.isNotEmpty())
        assertTrue(packing.any { it.assignedUid == "Ayaan" })
        assertTrue(packing.any { it.assignedUid == "Linnea" })

        assertTrue(itinerary.isNotEmpty())
        assertTrue(itinerary.any { it.category == "Must Do" })
        assertTrue(itinerary.any { it.category == "Rain Backup" })
    }
}
