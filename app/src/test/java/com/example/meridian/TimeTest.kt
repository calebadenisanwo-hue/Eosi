package com.example.meridian

import com.example.meridian.core.time.MeridianTime
import org.junit.Assert.*
import org.junit.Test
import java.time.Duration
import java.time.Instant
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime

class TimeTest {

    private val puneZone = ZoneId.of("Asia/Kolkata")
    private val gothenburgZone = ZoneId.of("Europe/Stockholm")

    @Test
    fun `pune and gothenburg offset difference in summer is 3h 30m`() {
        // Summer instant: 15 July 2026 12:00 UTC
        val summerInstant = ZonedDateTime.of(2026, 7, 15, 12, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        val diff = MeridianTime.offsetDifference(puneZone, gothenburgZone, summerInstant)
        assertEquals(3, diff.toHours())
        assertEquals(30, (diff.toMinutes() % 60))
        assertEquals("3h 30m apart", MeridianTime.formatDifference(diff))
    }

    @Test
    fun `pune and gothenburg offset difference in winter is 4h 30m`() {
        // Winter instant: 15 December 2026 12:00 UTC
        val winterInstant = ZonedDateTime.of(2026, 12, 15, 12, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        val diff = MeridianTime.offsetDifference(puneZone, gothenburgZone, winterInstant)
        assertEquals(4, diff.toHours())
        assertEquals(30, (diff.toMinutes() % 60))
        assertEquals("4h 30m apart", MeridianTime.formatDifference(diff))
    }

    @Test
    fun `nextDstShift detects 25 October 2026 European clock change from 14 days out`() {
        // Reference: 15 October 2026 (10 days before transition)
        val refInstant = ZonedDateTime.of(2026, 10, 15, 12, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        val shift = MeridianTime.nextDstShift(puneZone, gothenburgZone, refInstant, lookaheadDays = 20)

        assertNotNull(shift)
        assertEquals(gothenburgZone, shift!!.zoneAffected)
        assertEquals(Duration.ofMinutes(210), shift.previousOffsetDiff) // 3h 30m
        assertEquals(Duration.ofMinutes(270), shift.newOffsetDiff) // 4h 30m
        assertTrue(shift.daysUntil in 9..11)
        assertTrue(shift.headsUpMessage.contains("4h 30m apart instead of 3h 30m"))
    }

    @Test
    fun `computeOverlapWindows intersects awake schedules across 48 hours`() {
        val refInstant = ZonedDateTime.of(2026, 7, 15, 8, 0, 0, 0, ZoneId.of("UTC")).toInstant()
        val puneAwake = MeridianTime.AwakeHours(LocalTime.of(7, 30), LocalTime.of(23, 0))
        val gothAwake = MeridianTime.AwakeHours(LocalTime.of(7, 0), LocalTime.of(23, 0))

        val windows = MeridianTime.computeOverlapWindows(
            myZone = puneZone,
            myAwake = puneAwake,
            partnerZone = gothenburgZone,
            partnerAwake = gothAwake,
            referenceInstant = refInstant
        )

        assertFalse("Should find awake overlap windows", windows.isEmpty())
        assertTrue("Windows should be at least 30 minutes", windows.all { it.durationMinutes >= 30 })
    }

    @Test
    fun `localDateKey reflects local calendar date of author`() {
        // Instant at 2026-10-02 21:00 UTC
        // In Pune (UTC+5:30), this is 2026-10-03 02:30 AM
        // In Gothenburg (UTC+2:00 in Oct), this is 2026-10-02 23:00 PM
        val instant = ZonedDateTime.of(2026, 10, 2, 21, 0, 0, 0, ZoneId.of("UTC")).toInstant()

        val puneKey = MeridianTime.localDateKey(instant, puneZone)
        val gothKey = MeridianTime.localDateKey(instant, gothenburgZone)

        assertEquals("2026-10-03", puneKey)
        assertEquals("2026-10-02", gothKey)
    }

    @Test
    fun `isUnsealed evaluates unsealInstant correctly`() {
        val now = Instant.parse("2026-10-02T12:00:00Z")
        val future = Instant.parse("2026-10-03T00:00:00Z")
        val past = Instant.parse("2026-10-01T00:00:00Z")

        assertTrue(MeridianTime.isUnsealed(null, now))
        assertTrue(MeridianTime.isUnsealed(past, now))
        assertFalse(MeridianTime.isUnsealed(future, now))
    }
}
