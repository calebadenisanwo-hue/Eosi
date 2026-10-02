package com.example.meridian.data.jagi

import androidx.compose.ui.graphics.Color
import java.time.LocalDate

data class CoupleData(
    val userName: String = "Kola",
    val partnerName: String = "Joy",
    val daysTogether: Int = 153,
    val anniversaryDate: LocalDate = LocalDate.of(2026, 5, 2),
    val userTime: String = "23:18",
    val partnerTime: String = "23:18",
    val companionName: String = "Sol line",
    val companionSpecies: String = "Spark",
    val companionLevel: Int = 1,
    val companionXp: Int = 10,
    val companionMaxXp: Int = 30,
    val hatchedDate: String = "2 Oct",
    val userMood: String? = null,
    val partnerMood: String? = null,
    val isPartnerAsleep: Boolean = true,
    val questionStreakDays: Int = 0,
    val todayQuestion: String = "What is a question you asked yourself today?",
    val userAnswer: String? = null,
    val partnerAnswer: String? = "How can I make more time for the things that quietly bring me peace?",
    val isUserAnswerSealed: Boolean = false,
    val isPartnerAnswerSealed: Boolean = true,
    val lastTouchFromPartner: String? = null,
    val lastTouchSentToPartner: String? = null,
    val touchCount: Int = 0,
    val bondRank: String = "#284",
    val leaderboardRankWeek: String = "#116",
    val distanceUnit: String = "KM",
    val temperatureUnit: String = "°C",
    val selectedAppIcon: String = "Default",
    val favoriteColor: Color = Color(0xFF749B74)
)

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
