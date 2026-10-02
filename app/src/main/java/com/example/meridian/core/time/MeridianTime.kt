package com.example.meridian.core.time

import java.time.Duration
import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit
import java.util.Locale

/**
 * Meridian Time Architecture.
 * Standardizes all time calculations:
 * - Developer Time Machine override for Instant
 * - Partner local conversions
 * - 48-hour awake time overlap window computation
 * - 14-day DST shift detection and difference calculation
 * - Local date keys and scheduled unseals
 */
object MeridianTime {

    // Overridable clock for Dev Time Machine
    @Volatile
    var clockOverride: Instant? = null

    /**
     * Current instant according to real clock or Dev Time Machine.
     */
    fun now(): Instant {
        return clockOverride ?: Instant.now()
    }

    /**
     * Reset the time machine override back to system clock.
     */
    fun resetClock() {
        clockOverride = null
    }

    /**
     * Set a specific instant for developer testing.
     */
    fun setOverrideInstant(instant: Instant) {
        clockOverride = instant
    }

    data class AwakeHours(
        val start: LocalTime = LocalTime.of(7, 30),
        val end: LocalTime = LocalTime.of(23, 0)
    )

    data class OverlapWindow(
        val startInstant: Instant,
        val endInstant: Instant,
        val durationMinutes: Long
    )

    data class DstShift(
        val transitionInstant: Instant,
        val zoneAffected: ZoneId,
        val previousOffsetDiff: Duration,
        val newOffsetDiff: Duration,
        val daysUntil: Long,
        val headsUpMessage: String
    )

    data class FormattedTime(
        val timeStr: String,
        val period: String,
        val dateStr: String,
        val hour24: Int,
        val minute: Int
    )

    /**
     * Format an instant for display in a specific zone.
     */
    fun formatInZone(instant: Instant, zoneId: ZoneId): FormattedTime {
        val zdt = instant.atZone(zoneId)
        val timeFmt = DateTimeFormatter.ofPattern("h:mm", Locale.getDefault())
        val periodFmt = DateTimeFormatter.ofPattern("a", Locale.getDefault())
        val dateFmt = DateTimeFormatter.ofPattern("EEE, d MMM", Locale.getDefault())
        return FormattedTime(
            timeStr = zdt.format(timeFmt),
            period = zdt.format(periodFmt).lowercase(),
            dateStr = zdt.format(dateFmt),
            hour24 = zdt.hour,
            minute = zdt.minute
        )
    }

    /**
     * Produce local date key (YYYY-MM-DD) for grouping and streak tracking in author's zone.
     */
    fun localDateKey(instant: Instant, zoneId: ZoneId): String {
        val zdt = instant.atZone(zoneId)
        return zdt.toLocalDate().toString()
    }

    /**
     * Compute current time difference between user zone and partner zone.
     */
    fun offsetDifference(myZone: ZoneId, partnerZone: ZoneId, instant: Instant = now()): Duration {
        val myOffset = myZone.rules.getOffset(instant)
        val partnerOffset = partnerZone.rules.getOffset(instant)
        val diffSeconds = Math.abs(myOffset.totalSeconds - partnerOffset.totalSeconds)
        return Duration.ofSeconds(diffSeconds.toLong())
    }

    /**
     * Format a duration as a calm string: "4h 30m apart" or "Same time zone"
     */
    fun formatDifference(diff: Duration): String {
        val hours = diff.toHours()
        val minutes = (diff.toMinutes() % 60)
        return when {
            hours == 0L && minutes == 0L -> "Same time zone"
            minutes == 0L -> "${hours}h apart"
            else -> "${hours}h ${minutes}m apart"
        }
    }

    /**
     * Compute overlap windows across the next 48 hours between two awake schedules.
     */
    fun computeOverlapWindows(
        myZone: ZoneId,
        myAwake: AwakeHours,
        partnerZone: ZoneId,
        partnerAwake: AwakeHours,
        referenceInstant: Instant = now()
    ): List<OverlapWindow> {
        val windows = mutableListOf<OverlapWindow>()
        val startOfToday = referenceInstant.atZone(myZone).truncatedTo(ChronoUnit.DAYS).toInstant()
        val stepMinutes = 15L
        val totalSteps = (48 * 60) / stepMinutes

        var inOverlap = false
        var windowStart: Instant? = null

        for (i in 0 until totalSteps) {
            val currentInstant = startOfToday.plus(Duration.ofMinutes(i * stepMinutes))
            if (currentInstant.isBefore(referenceInstant)) continue

            val myLocal = currentInstant.atZone(myZone).toLocalTime()
            val partnerLocal = currentInstant.atZone(partnerZone).toLocalTime()

            val isMyAwake = isWithinAwake(myLocal, myAwake)
            val isPartnerAwake = isWithinAwake(partnerLocal, partnerAwake)

            if (isMyAwake && isPartnerAwake) {
                if (!inOverlap) {
                    inOverlap = true
                    windowStart = currentInstant
                }
            } else {
                if (inOverlap && windowStart != null) {
                    val duration = Duration.between(windowStart, currentInstant).toMinutes()
                    if (duration >= 30) {
                        windows.add(OverlapWindow(windowStart, currentInstant, duration))
                    }
                    inOverlap = false
                    windowStart = null
                }
            }
        }

        if (inOverlap && windowStart != null) {
            val end = startOfToday.plus(Duration.ofHours(48))
            val duration = Duration.between(windowStart, end).toMinutes()
            if (duration >= 30) {
                windows.add(OverlapWindow(windowStart, end, duration))
            }
        }

        return windows
    }

    private fun isWithinAwake(time: LocalTime, awake: AwakeHours): Boolean {
        return if (awake.start <= awake.end) {
            time >= awake.start && time <= awake.end
        } else {
            // Spans midnight
            time >= awake.start || time <= awake.end
        }
    }

    /**
     * Check if both partners are currently awake.
     */
    fun areBothAwakeNow(
        myZone: ZoneId,
        myAwake: AwakeHours,
        partnerZone: ZoneId,
        partnerAwake: AwakeHours,
        instant: Instant = now()
    ): Boolean {
        val myLocal = instant.atZone(myZone).toLocalTime()
        val partnerLocal = instant.atZone(partnerZone).toLocalTime()
        return isWithinAwake(myLocal, myAwake) && isWithinAwake(partnerLocal, partnerAwake)
    }

    /**
     * Scan forward up to 60 days to detect the next daylight saving time shift in either zone.
     * Generates the 14-day advance heads-up banner notice.
     */
    fun nextDstShift(
        myZone: ZoneId,
        partnerZone: ZoneId,
        referenceInstant: Instant = now(),
        lookaheadDays: Long = 60
    ): DstShift? {
        val currentDiff = offsetDifference(myZone, partnerZone, referenceInstant)
        var cursor = referenceInstant

        for (day in 1..lookaheadDays) {
            val nextCursor = cursor.plus(Duration.ofDays(1))
            val myOffsetCurr = myZone.rules.getOffset(cursor)
            val myOffsetNext = myZone.rules.getOffset(nextCursor)
            val pOffsetCurr = partnerZone.rules.getOffset(cursor)
            val pOffsetNext = partnerZone.rules.getOffset(nextCursor)

            val myChanged = myOffsetCurr != myOffsetNext
            val partnerChanged = pOffsetCurr != pOffsetNext

            if (myChanged || partnerChanged) {
                // Find transition boundary
                val affectedZone = if (partnerChanged) partnerZone else myZone
                val transition = affectedZone.rules.nextTransition(cursor)
                val transInstant = transition?.instant ?: nextCursor

                val newDiff = offsetDifference(myZone, partnerZone, transInstant.plusSeconds(3600))
                val daysUntil = ChronoUnit.DAYS.between(referenceInstant, transInstant).coerceAtLeast(0)

                val dateFmt = DateTimeFormatter.ofPattern("d MMMM", Locale.ENGLISH)
                val dateStr = transInstant.atZone(myZone).format(dateFmt)

                val oldDiffStr = formatDifference(currentDiff)
                val newDiffStr = formatDifference(newDiff)

                val msg = "Heads up: from $dateStr you're $newDiffStr instead of $oldDiffStr. Your usual call time may move."

                return DstShift(
                    transitionInstant = transInstant,
                    zoneAffected = affectedZone,
                    previousOffsetDiff = currentDiff,
                    newOffsetDiff = newDiff,
                    daysUntil = daysUntil,
                    headsUpMessage = msg
                )
            }
            cursor = nextCursor
        }
        return null
    }

    /**
     * Check if a letter or birthday is unsealed.
     */
    fun isUnsealed(unsealAt: Instant?, currentInstant: Instant = now()): Boolean {
        if (unsealAt == null) return true
        return !currentInstant.isBefore(unsealAt)
    }

    /**
     * Compute the next midnight instant in the recipient's zone for birthday or scheduled letter unseal.
     */
    fun nextMidnightInZone(zoneId: ZoneId, referenceInstant: Instant = now()): Instant {
        val zdt = referenceInstant.atZone(zoneId)
        val nextDayMidnight = zdt.toLocalDate().plusDays(1).atStartOfDay(zoneId)
        return nextDayMidnight.toInstant()
    }
}
