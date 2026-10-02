package com.example.meridian.data.jagi

import androidx.compose.ui.graphics.Color
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class JagiRepo {

    private val _coupleData = MutableStateFlow(CoupleData())
    val coupleData: StateFlow<CoupleData> = _coupleData.asStateFlow()

    private val _sharedLists = MutableStateFlow(
        listOf(
            SharedListItem(
                id = "bucket_list",
                title = "Bucket list",
                description = "the someday places",
                emoji = "🌍",
                items = listOf("See Northern Lights in Tromsø", "Ride the cable car in San Francisco")
            ),
            SharedListItem(
                id = "when_together",
                title = "When we're together",
                description = "the little plans for next time",
                emoji = "🏡",
                items = listOf("Cook homemade gnocchi from scratch", "Morning coffee walk in the park")
            )
        )
    )
    val sharedLists: StateFlow<List<SharedListItem>> = _sharedLists.asStateFlow()

    private val _memories = MutableStateFlow<List<MemoryItem>>(emptyList())
    val memories: StateFlow<List<MemoryItem>> = _memories.asStateFlow()

    val playDecks = listOf(
        PlayDeck("guess_her", "Guess her answer", "How well you know her", "🎯", 13, "Guess"),
        PlayDeck("in_sync", "In sync", "You both answer", "🔗", 13, "Sync"),
        PlayDeck("more_likely", "Who's more likely?", "Agree and score", "🎲", 11, "Likely"),
        PlayDeck("this_or_that", "This or That", "Quick picks", "⚡", 14, "Picks"),
        PlayDeck("deep_dive", "Deep dive", "Three questions, real answers", "🌊", 6, "Deep")
    )

    fun sendTouch(message: String) {
        _coupleData.update { current ->
            val newXp = current.companionXp + 2
            val (level, xp) = if (newXp >= current.companionMaxXp) {
                Pair(current.companionLevel + 1, newXp - current.companionMaxXp)
            } else {
                Pair(current.companionLevel, newXp)
            }
            current.copy(
                lastTouchSentToPartner = message,
                touchCount = current.touchCount + 1,
                companionLevel = level,
                companionXp = xp
            )
        }
    }

    fun setUserMood(mood: String) {
        _coupleData.update { it.copy(userMood = mood) }
    }

    fun sealUserAnswer(answer: String) {
        _coupleData.update { current ->
            val newXp = current.companionXp + 10
            val (level, xp) = if (newXp >= current.companionMaxXp) {
                Pair(current.companionLevel + 1, newXp - current.companionMaxXp)
            } else {
                Pair(current.companionLevel, newXp)
            }
            current.copy(
                userAnswer = answer,
                isUserAnswerSealed = true,
                isPartnerAnswerSealed = false, // Unseals partner's answer!
                questionStreakDays = current.questionStreakDays + 1,
                companionLevel = level,
                companionXp = xp
            )
        }
    }

    fun addMemory(title: String, isVoice: Boolean = false, duration: Int = 10, note: String = "") {
        val newMemory = MemoryItem(
            id = "mem_${System.currentTimeMillis()}",
            title = title,
            timestamp = "Just now",
            isVoiceNote = isVoice,
            voiceDurationSeconds = duration,
            note = note
        )
        _memories.update { listOf(newMemory) + it }
        _coupleData.update { current ->
            current.copy(companionXp = minOf(current.companionMaxXp, current.companionXp + 5))
        }
    }

    fun addListItem(listId: String, text: String) {
        _sharedLists.update { list ->
            list.map { item ->
                if (item.id == listId) {
                    item.copy(items = item.items + text)
                } else item
            }
        }
    }

    fun createBlankList(title: String, description: String = "our shared thoughts", emoji: String = "✨") {
        val newList = SharedListItem(
            id = "list_${System.currentTimeMillis()}",
            title = title,
            description = description,
            emoji = emoji,
            items = emptyList()
        )
        _sharedLists.update { it + newList }
    }

    fun setSelectedAppIcon(iconName: String) {
        _coupleData.update { it.copy(selectedAppIcon = iconName) }
    }

    fun toggleDistanceUnit() {
        _coupleData.update {
            it.copy(distanceUnit = if (it.distanceUnit == "KM") "MI" else "KM")
        }
    }

    fun toggleTemperatureUnit() {
        _coupleData.update {
            it.copy(temperatureUnit = if (it.temperatureUnit == "°C") "°F" else "°C")
        }
    }
}
