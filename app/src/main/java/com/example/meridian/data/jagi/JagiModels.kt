package com.example.meridian.data.jagi

import androidx.compose.ui.graphics.Color
import java.time.LocalDate
import java.time.temporal.ChronoUnit

data class CoupleData(
    val userName: String = "You",
    val partnerName: String = "Partner",
    val userCity: String = "London",
    val partnerCity: String = "New York",
    val userTimezone: String = "GMT",
    val partnerTimezone: String = "EST",
    val distanceKm: Int = 5570,
    val anniversaryDate: LocalDate = LocalDate.now().minusDays(120),
    val nextVisitDate: LocalDate? = LocalDate.now().plusDays(24),
    val companionName: String = "Sol",
    val companionSpecies: String = "Cosmic Cat",
    val companionLevel: Int = 1,
    val companionXp: Int = 15,
    val companionMaxXp: Int = 30,
    val hatchedDate: String = "Day 1",
    val userMood: String = "Thinking of you",
    val partnerMood: String = "Counting down the days",
    val userBatteryPercent: Int = 88,
    val partnerBatteryPercent: Int = 74,
    val isPartnerAsleep: Boolean = false,
    val questionStreakDays: Int = 7,
    val todayQuestion: String = "What is a small, quiet moment with me that you still think about?",
    val userAnswer: String? = null,
    val partnerAnswer: String? = "That rainy Tuesday morning when we made coffee and neither of us wanted to leave bed.",
    val isUserAnswerSealed: Boolean = false,
    val isPartnerAnswerSealed: Boolean = false,
    val lastTouchFromPartner: String? = null,
    val lastTouchSentToPartner: String? = null,
    val touchCount: Int = 24,
    val distanceUnit: String = "KM",
    val temperatureUnit: String = "°C",
    val selectedAppIcon: String = "Midnight",
    val pairingCode: String = "EOS-7842",
    val userTime: String = "23:18",
    val partnerTime: String = "18:18",
    val bondRank: String = "#284",
    val leaderboardRankWeek: String = "#116"
) {
    val daysTogether: Long
        get() = ChronoUnit.DAYS.between(anniversaryDate, LocalDate.now()).coerceAtLeast(1)

    val daysUntilNextVisit: Long?
        get() = nextVisitDate?.let { ChronoUnit.DAYS.between(LocalDate.now(), it).coerceAtLeast(0) }
}

data class SharedListItem(
    val id: String,
    val title: String,
    val description: String,
    val emoji: String,
    val items: List<String> = emptyList()
)

data class MemoryItem(
    val id: String,
    val title: String,
    val timestamp: String,
    val isVoiceNote: Boolean = false,
    val voiceDurationSeconds: Int = 0,
    val note: String = ""
)

data class PlayDeck(
    val id: String,
    val title: String,
    val subtitle: String,
    val emoji: String,
    val decksCount: Int,
    val category: String
)

data class SecretLetter(
    val id: String,
    val title: String,
    val author: String,
    val body: String,
    val date: String,
    val isSpicy: Boolean = false
)

data class RepairProtocolState(
    val isUnderStress: Boolean = false,
    val currentFeeling: String? = null,
    val neededAffirmation: String? = null,
    val breathingDone: Boolean = false,
    val repairNote: String = ""
)
