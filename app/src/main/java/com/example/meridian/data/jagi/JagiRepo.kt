package com.example.meridian.data.jagi

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import java.time.LocalDate

class JagiRepo {

    private val _coupleData = MutableStateFlow(CoupleData())
    val coupleData: StateFlow<CoupleData> = _coupleData.asStateFlow()

    private val _sharedLists = MutableStateFlow(
        listOf(
            SharedListItem(
                id = "bucket_list",
                title = "Bucket list",
                description = "the someday places & experiences",
                emoji = "🌍",
                items = listOf(
                    "Watch the sunrise from a seaside cabin",
                    "Take a spontaneous train to somewhere we've never been",
                    "Cook a full 4-course dinner together from scratch"
                )
            ),
            SharedListItem(
                id = "when_together",
                title = "When we're together",
                description = "the little plans for our next visit",
                emoji = "🏡",
                items = listOf(
                    "Get our favorite morning coffee and walk with no destination",
                    "Long grocery run buying silly snacks",
                    "Fall asleep with a movie playing in the background"
                )
            )
        )
    )
    val sharedLists: StateFlow<List<SharedListItem>> = _sharedLists.asStateFlow()

    private val _memories = MutableStateFlow(
        listOf(
            MemoryItem(
                id = "mem_1",
                title = "Airport arrival hug",
                timestamp = "Last visit",
                isVoiceNote = false,
                note = "That moment coming through the sliding doors and seeing you waiting."
            ),
            MemoryItem(
                id = "mem_2",
                title = "Goodnight voicemail",
                timestamp = "3 days ago",
                isVoiceNote = true,
                voiceDurationSeconds = 34,
                note = "Whispered goodnight note when the time difference made it late."
            )
        )
    )
    val memories: StateFlow<List<MemoryItem>> = _memories.asStateFlow()

    // Encrypted Vault
    private val _vaultLetters = MutableStateFlow(
        listOf(
            SecretLetter(
                id = "vl_1",
                title = "Open when you miss my voice",
                author = "Partner",
                body = "Distance is temporary, but the way you hold my heart across thousands of miles is permanent. Take a deep breath, close your eyes, and imagine me right beside you.",
                date = "Oct 1, 2026"
            ),
            SecretLetter(
                id = "vl_2",
                title = "A confession for tonight",
                author = "Partner",
                body = "Every single night before I fall asleep, the last thing on my mind is our next morning together.",
                date = "Sep 28, 2026",
                isSpicy = true
            )
        )
    )
    val vaultLetters: StateFlow<List<SecretLetter>> = _vaultLetters.asStateFlow()

    // Ambient Sound Levels (0 to 100)
    private val _ambientLevels = MutableStateFlow(mapOf("Rain" to 75, "Campfire" to 40, "Cafe" to 0, "Night Wind" to 55))
    val ambientLevels: StateFlow<Map<String, Int>> = _ambientLevels.asStateFlow()

    // Repair Protocol
    private val _repairState = MutableStateFlow(RepairProtocolState())
    val repairState: StateFlow<RepairProtocolState> = _repairState.asStateFlow()

    // Game stats
    private val _duelScore = MutableStateFlow(Pair(0, 0))
    val duelScore: StateFlow<Pair<Int, Int>> = _duelScore.asStateFlow()

    fun updateProfile(
        userName: String,
        partnerName: String,
        userCity: String,
        partnerCity: String,
        anniversary: LocalDate,
        nextVisit: LocalDate?
    ) {
        _coupleData.update { current ->
            current.copy(
                userName = userName.ifBlank { current.userName },
                partnerName = partnerName.ifBlank { current.partnerName },
                userCity = userCity.ifBlank { current.userCity },
                partnerCity = partnerCity.ifBlank { current.partnerCity },
                anniversaryDate = anniversary,
                nextVisitDate = nextVisit
            )
        }
    }

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

    fun petCompanion() {
        _coupleData.update { current ->
            val newXp = current.companionXp + 3
            val (level, xp) = if (newXp >= current.companionMaxXp) {
                Pair(current.companionLevel + 1, newXp - current.companionMaxXp)
            } else {
                Pair(current.companionLevel, newXp)
            }
            current.copy(
                companionLevel = level,
                companionXp = xp
            )
        }
    }

    fun renameCompanion(name: String, species: String) {
        _coupleData.update {
            it.copy(
                companionName = name.ifBlank { it.companionName },
                companionSpecies = species.ifBlank { it.companionSpecies }
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
                isPartnerAnswerSealed = false,
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
        if (text.isBlank()) return
        _sharedLists.update { lists ->
            lists.map { list ->
                if (list.id == listId) list.copy(items = list.items + text.trim()) else list
            }
        }
    }

    fun removeListItem(listId: String, index: Int) {
        _sharedLists.update { lists ->
            lists.map { list ->
                if (list.id == listId) {
                    val updated = list.items.toMutableList().apply { if (index in indices) removeAt(index) }
                    list.copy(items = updated)
                } else list
            }
        }
    }

    fun addSecretLetter(title: String, body: String, isSpicy: Boolean = false) {
        val letter = SecretLetter(
            id = "vl_${System.currentTimeMillis()}",
            title = title,
            author = "You",
            body = body,
            date = "Today",
            isSpicy = isSpicy
        )
        _vaultLetters.update { listOf(letter) + it }
    }

    fun setAmbientLevel(sound: String, volume: Int) {
        _ambientLevels.update { current ->
            current.toMutableMap().apply { put(sound, volume) }
        }
    }

    fun setRepairFeeling(feeling: String) {
        _repairState.update { it.copy(currentFeeling = feeling, isUnderStress = true) }
    }

    fun finishBreathing() {
        _repairState.update { it.copy(breathingDone = true) }
    }

    fun resetRepair() {
        _repairState.value = RepairProtocolState()
    }

    fun recordDuelWin(playerIndex: Int) {
        _duelScore.update { (p1, p2) ->
            if (playerIndex == 1) Pair(p1 + 1, p2) else Pair(p1, p2 + 1)
        }
    }

    fun resetDuelScore() {
        _duelScore.value = Pair(0, 0)
    }

    fun setDistanceUnit(unit: String) {
        _coupleData.update { it.copy(distanceUnit = unit) }
    }

    fun toggleDistanceUnit() {
        _coupleData.update { it.copy(distanceUnit = if (it.distanceUnit == "KM") "MI" else "KM") }
    }

    fun setTemperatureUnit(unit: String) {
        _coupleData.update { it.copy(temperatureUnit = unit) }
    }

    fun toggleTemperatureUnit() {
        _coupleData.update { it.copy(temperatureUnit = if (it.temperatureUnit == "°C") "°F" else "°C") }
    }

    fun createBlankList(title: String, description: String = "shared list", emoji: String = "📝") {
        val newList = SharedListItem(
            id = "list_${System.currentTimeMillis()}",
            title = title.ifBlank { "New List" },
            description = description.ifBlank { "shared list" },
            emoji = emoji.ifBlank { "📝" },
            items = emptyList()
        )
        _sharedLists.update { it + newList }
    }

    fun selectAppIcon(iconName: String) {
        _coupleData.update { it.copy(selectedAppIcon = iconName) }
    }
}
