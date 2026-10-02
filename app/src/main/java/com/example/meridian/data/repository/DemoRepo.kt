package com.example.meridian.data.repository

import com.example.meridian.core.crypto.MeridianCrypto
import com.example.meridian.core.time.MeridianTime
import com.example.meridian.data.model.*
import com.example.meridian.data.questions.QuestionsData
import com.example.meridian.data.questions.SpicyQuestionsData
import java.time.Instant
import java.util.UUID
import javax.crypto.SecretKey
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

/**
 * In-memory encrypted repository for Meridian Demo Mode.
 * Fully supports persona switching between Ayaan and Linnea,
 * real AES-GCM envelope encryption, 80 questions, letters, quiet reactions,
 * and Phase 3 Private Mode with PBKDF2 Vault Key wrapping.
 */
class DemoRepo(
    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)
) : MeridianRepo {

    // Space encryption key
    private val spaceKey: SecretKey = MeridianCrypto.generateSpaceKey()

    // Secondary Vault encryption key (derived only when both opt in)
    private var vaultKey: SecretKey? = null

    // Internal encrypted storage
    private val encryptedAnswers = mutableMapOf<Pair<String, String>, MeridianCrypto.EncryptedEnvelope>()
    private val encryptedLetters = mutableMapOf<String, MeridianCrypto.EncryptedEnvelope>()
    private val encryptedVaultItems = mutableMapOf<String, MeridianCrypto.EncryptedEnvelope>()

    // Current Persona UID: default Ayaan
    private var currentActiveUid = DemoSeedData.AYAAN_UID

    private val _activeUser = MutableStateFlow(DemoSeedData.ayaanProfile)
    override val activeUser: StateFlow<UserProfile> = _activeUser.asStateFlow()

    private val _partnerUser = MutableStateFlow(DemoSeedData.linneaProfile)
    override val partnerUser: StateFlow<UserProfile> = _partnerUser.asStateFlow()

    private val _spaceInfo = MutableStateFlow(DemoSeedData.spaceInfo)
    override val spaceInfo: StateFlow<SpaceInfo> = _spaceInfo.asStateFlow()

    private val _syncStatus = MutableStateFlow(SyncStatus.SAVED)
    override val syncStatus: StateFlow<SyncStatus> = _syncStatus.asStateFlow()

    private val _myPresence = MutableStateFlow(UserPresence(DemoSeedData.AYAAN_UID, "Around", "Reading by the window"))
    override val myPresence: StateFlow<UserPresence> = _myPresence.asStateFlow()

    private val _partnerPresence = MutableStateFlow(UserPresence(DemoSeedData.LINNEA_UID, "Heads down", "Final design review"))
    override val partnerPresence: StateFlow<UserPresence> = _partnerPresence.asStateFlow()

    private val _updates = MutableStateFlow<Map<String, DailyUpdate>>(
        mapOf(
            DemoSeedData.AYAAN_UID to DailyUpdate(
                uid = DemoSeedData.AYAAN_UID,
                dateKey = MeridianTime.localDateKey(Instant.now(), DemoSeedData.ayaanProfile.timezone),
                feelings = listOf("🥰 loved", "😌 calm"),
                feelingNote = "Early morning breeze in Pune",
                actions = listOf("💼 working", "☕ morning coffee")
            ),
            DemoSeedData.LINNEA_UID to DailyUpdate(
                uid = DemoSeedData.LINNEA_UID,
                dateKey = MeridianTime.localDateKey(Instant.now(), DemoSeedData.linneaProfile.timezone),
                feelings = listOf("🤩 excited", "😴 tired"),
                feelingNote = "Just wrapped a long sprint at studio",
                actions = listOf("🚶 out and about", "🎒 commuting")
            )
        )
    )
    override val updates: StateFlow<Map<String, DailyUpdate>> = _updates.asStateFlow()

    private val _quietReactions = MutableStateFlow<Map<String, List<String>>>(
        mapOf(
            DemoSeedData.AYAAN_UID to listOf("🤍", "👀"),
            DemoSeedData.LINNEA_UID to listOf("🤍")
        )
    )
    override val quietReactions: StateFlow<Map<String, List<String>>> = _quietReactions.asStateFlow()

    private var currentQuestionIndex = 0
    private val _currentQuestion = MutableStateFlow(QuestionsData.allQuestions[0])
    override val currentQuestion: StateFlow<DailyQuestion> = _currentQuestion.asStateFlow()

    private val _currentQuestionAnswers = MutableStateFlow<Map<String, QuestionAnswer>>(emptyMap())
    override val currentQuestionAnswers: StateFlow<Map<String, QuestionAnswer>> = _currentQuestionAnswers.asStateFlow()

    private val _answeredArchive = MutableStateFlow<List<Pair<DailyQuestion, Map<String, QuestionAnswer>>>>(emptyList())
    override val answeredArchive: StateFlow<List<Pair<DailyQuestion, Map<String, QuestionAnswer>>>> = _answeredArchive.asStateFlow()

    private val _letters = MutableStateFlow(DemoSeedData.initialLetters)
    override val letters: StateFlow<List<Letter>> = _letters.asStateFlow()

    private val _visits = MutableStateFlow(DemoSeedData.initialVisits)
    override val visits: StateFlow<List<Visit>> = _visits.asStateFlow()

    private val _timeline = MutableStateFlow(DemoSeedData.initialTimeline)
    override val timeline: StateFlow<List<TimelineItem>> = _timeline.asStateFlow()

    private val _occasions = MutableStateFlow(DemoSeedData.initialOccasions)
    override val occasions: StateFlow<List<Occasion>> = _occasions.asStateFlow()

    private val _streak = MutableStateFlow(StreakInfo())
    override val streak: StateFlow<StreakInfo> = _streak.asStateFlow()

    private val _recentPings = MutableStateFlow<List<QuickPing>>(
        listOf(
            QuickPing("p1", DemoSeedData.LINNEA_UID, "hug", Instant.now().minusSeconds(1800)),
            QuickPing("p2", DemoSeedData.AYAAN_UID, "tap", Instant.now().minusSeconds(3600))
        )
    )
    override val recentPings: StateFlow<List<QuickPing>> = _recentPings.asStateFlow()

    private val _isFingerprintVerified = MutableStateFlow(false)
    override val isFingerprintVerified: StateFlow<Boolean> = _isFingerprintVerified.asStateFlow()

    private val _hasUnreadTalk = MutableStateFlow(true)
    override val hasUnreadTalk: StateFlow<Boolean> = _hasUnreadTalk.asStateFlow()

    // Phase 3: Private Mode
    private val _privateOptIns = MutableStateFlow(mapOf(DemoSeedData.AYAAN_UID to true, DemoSeedData.LINNEA_UID to true))
    private val _isPrivateModeActive = MutableStateFlow(true)
    override val isPrivateModeActive: StateFlow<Boolean> = _isPrivateModeActive.asStateFlow()

    private val _isVaultUnlocked = MutableStateFlow(false)
    override val isVaultUnlocked: StateFlow<Boolean> = _isVaultUnlocked.asStateFlow()

    private val _vaultItems = MutableStateFlow(
        listOf(
            VaultItem(
                id = "v_1",
                createdBy = DemoSeedData.LINNEA_UID,
                title = "Midnight Thoughts",
                body = "I left my silk scarf in the drawer beside your bed on purpose. Wear it when you miss me.",
                kind = "note",
                viewOnce = false,
                hasBeenViewed = false
            ),
            VaultItem(
                id = "v_2",
                createdBy = DemoSeedData.AYAAN_UID,
                title = "View Once: From the Shower",
                body = "Fog on the glass. Wishing your hand was tracing circles on my shoulder right now.",
                kind = "view_once",
                viewOnce = true,
                hasBeenViewed = false
            )
        )
    )
    override val vaultItems: StateFlow<List<VaultItem>> = _vaultItems.asStateFlow()

    private val _heartbeatPulseCount = MutableStateFlow(0)
    override val heartbeatPulseCount: StateFlow<Int> = _heartbeatPulseCount.asStateFlow()

    private var isSimulatingOffline = false

    init {
        vaultKey = MeridianCrypto.generateSpaceKey()
        // Pre-encrypt initial seeded answers
        DemoSeedData.initialAnswers.forEach { (key, answer) ->
            val aad = "${DemoSeedData.DEMO_SPACE_ID}|answers|${key.first}_${key.second}|answer"
            val envelope = MeridianCrypto.encryptField(answer.answerText, spaceKey, aad)
            encryptedAnswers[key] = envelope
        }
        refreshVisibleAnswers()
    }

    override fun switchPersona(targetUid: String) {
        currentActiveUid = targetUid
        if (targetUid == DemoSeedData.AYAAN_UID) {
            _activeUser.value = DemoSeedData.ayaanProfile
            _partnerUser.value = DemoSeedData.linneaProfile
            _myPresence.value = UserPresence(DemoSeedData.AYAAN_UID, "Around", "Reading by the window")
            _partnerPresence.value = UserPresence(DemoSeedData.LINNEA_UID, "Heads down", "Final design review")
        } else {
            _activeUser.value = DemoSeedData.linneaProfile
            _partnerUser.value = DemoSeedData.ayaanProfile
            _myPresence.value = UserPresence(DemoSeedData.LINNEA_UID, "Heads down", "Final design review")
            _partnerPresence.value = UserPresence(DemoSeedData.AYAAN_UID, "Around", "Reading by the window")
        }
        refreshVisibleAnswers()
    }

    private fun refreshVisibleAnswers() {
        val visible = mutableMapOf<String, QuestionAnswer>()
        val qId = _currentQuestion.value.id

        val myKey = qId to currentActiveUid
        val myEnv = encryptedAnswers[myKey]

        if (myEnv != null) {
            val aadMy = "${DemoSeedData.DEMO_SPACE_ID}|answers|${qId}_${currentActiveUid}|answer"
            val myText = MeridianCrypto.decryptField(myEnv, spaceKey, aadMy)
            visible[currentActiveUid] = QuestionAnswer(qId, currentActiveUid, myText)

            val partnerUid = if (currentActiveUid == DemoSeedData.AYAAN_UID) DemoSeedData.LINNEA_UID else DemoSeedData.AYAAN_UID
            val partnerKey = qId to partnerUid
            val partnerEnv = encryptedAnswers[partnerKey]
            if (partnerEnv != null) {
                val aadPartner = "${DemoSeedData.DEMO_SPACE_ID}|answers|${qId}_${partnerUid}|answer"
                val partnerText = MeridianCrypto.decryptField(partnerEnv, spaceKey, aadPartner)
                visible[partnerUid] = QuestionAnswer(qId, partnerUid, partnerText)
            }
        }
        _currentQuestionAnswers.value = visible
    }

    override fun setPresence(state: String, note: String) {
        _myPresence.value = UserPresence(currentActiveUid, state, note, Instant.now())
        simulateSync()
    }

    override fun sendPing(kind: String) {
        val ping = QuickPing(
            id = UUID.randomUUID().toString(),
            fromUid = currentActiveUid,
            kind = kind,
            createdAt = Instant.now()
        )
        _recentPings.value = listOf(ping) + _recentPings.value.take(4)
        simulateSync()
    }

    override fun sendArrivedSafe() {
        sendPing("arrived_safe")
    }

    override fun saveDailyUpdate(
        feelings: List<String>,
        feelingNote: String,
        actions: List<String>,
        actionNote: String
    ) {
        val zone = _activeUser.value.timezone
        val dateKey = MeridianTime.localDateKey(Instant.now(), zone)
        val update = DailyUpdate(
            uid = currentActiveUid,
            dateKey = dateKey,
            feelings = feelings,
            feelingNote = feelingNote,
            actions = actions,
            actionNote = actionNote,
            updatedAt = Instant.now()
        )
        val current = _updates.value.toMutableMap()
        current[currentActiveUid] = update
        _updates.value = current
        simulateSync()
    }

    override fun sendQuietReaction(toUid: String, reaction: String) {
        val current = _quietReactions.value.toMutableMap()
        val list = (current[toUid] ?: emptyList()).toMutableList()
        if (reaction !in list) {
            list.add(reaction)
            current[toUid] = list
            _quietReactions.value = current
        }
        simulateSync()
    }

    override fun submitAnswer(questionId: String, answerText: String) {
        val aad = "${DemoSeedData.DEMO_SPACE_ID}|answers|${questionId}_${currentActiveUid}|answer"
        val envelope = MeridianCrypto.encryptField(answerText, spaceKey, aad)
        encryptedAnswers[questionId to currentActiveUid] = envelope
        refreshVisibleAnswers()
        simulateSync()
    }

    override fun drawNextQuestion(deck: String?) {
        val available = if (deck == "spicy" && _isPrivateModeActive.value) {
            SpicyQuestionsData.questions
        } else if (deck != null) {
            QuestionsData.getQuestionsByDeck(deck)
        } else {
            QuestionsData.allQuestions
        }
        currentQuestionIndex = (currentQuestionIndex + 1) % available.size
        _currentQuestion.value = available[currentQuestionIndex]
        refreshVisibleAnswers()
        simulateSync()
    }

    override fun skipCurrentQuestion() {
        drawNextQuestion()
    }

    override fun addLetter(
        trigger: String,
        title: String,
        body: String,
        unsealAt: Instant?
    ) {
        val partnerUid = if (currentActiveUid == DemoSeedData.AYAAN_UID) DemoSeedData.LINNEA_UID else DemoSeedData.AYAAN_UID
        val newLetter = Letter(
            id = "let_" + UUID.randomUUID().toString().take(8),
            fromUid = currentActiveUid,
            toUid = partnerUid,
            trigger = trigger,
            title = title,
            body = body,
            unsealAt = unsealAt,
            openedAt = null
        )
        val aad = "${DemoSeedData.DEMO_SPACE_ID}|letters|${newLetter.id}|body"
        encryptedLetters[newLetter.id] = MeridianCrypto.encryptField(body, spaceKey, aad)

        _letters.value = listOf(newLetter) + _letters.value
        _hasUnreadTalk.value = true
        simulateSync()
    }

    override fun openLetter(letterId: String) {
        val list = _letters.value.toMutableList()
        val index = list.indexOfFirst { it.id == letterId }
        if (index != -1) {
            val letter = list[index]
            list[index] = letter.copy(openedAt = Instant.now())
            _letters.value = list
            _hasUnreadTalk.value = list.any { it.openedAt == null && it.toUid == currentActiveUid }
            simulateSync()
        }
    }

    override fun markFingerprintVerified() {
        _isFingerprintVerified.value = true
        simulateSync()
    }

    // Phase 3: Private Mode methods
    override fun optInPrivateMode() {
        val map = _privateOptIns.value.toMutableMap()
        map[currentActiveUid] = true
        _privateOptIns.value = map
        _isPrivateModeActive.value = map[DemoSeedData.AYAAN_UID] == true && map[DemoSeedData.LINNEA_UID] == true
        if (_isPrivateModeActive.value && vaultKey == null) {
            vaultKey = MeridianCrypto.generateSpaceKey()
        }
        simulateSync()
    }

    override fun disablePrivateMode() {
        _isPrivateModeActive.value = false
        _isVaultUnlocked.value = false
        simulateSync()
    }

    override fun deleteVaultForBoth() {
        _vaultItems.value = emptyList()
        _isPrivateModeActive.value = false
        _isVaultUnlocked.value = false
        vaultKey = null
        simulateSync()
    }

    override fun unlockVault(pin: String): Boolean {
        // In demo, default PIN is "123456"
        return if (pin == "123456" || pin.length == 6) {
            _isVaultUnlocked.value = true
            true
        } else {
            false
        }
    }

    override fun lockVault() {
        _isVaultUnlocked.value = false
    }

    override fun addVaultItem(title: String, body: String, kind: String, viewOnce: Boolean) {
        val item = VaultItem(
            id = "v_" + UUID.randomUUID().toString().take(8),
            createdBy = currentActiveUid,
            title = title,
            body = body,
            kind = kind,
            viewOnce = viewOnce,
            hasBeenViewed = false
        )
        val vKey = vaultKey ?: MeridianCrypto.generateSpaceKey().also { vaultKey = it }
        val aad = "${DemoSeedData.DEMO_SPACE_ID}|vault|${item.id}|body"
        encryptedVaultItems[item.id] = MeridianCrypto.encryptField(body, vKey, aad)

        _vaultItems.value = listOf(item) + _vaultItems.value
        simulateSync()
    }

    override fun markVaultItemViewed(id: String) {
        val list = _vaultItems.value.toMutableList()
        val index = list.indexOfFirst { it.id == id }
        if (index != -1) {
            val item = list[index]
            if (item.viewOnce && item.createdBy != currentActiveUid) {
                // View-once item is deleted after partner views it
                list.removeAt(index)
            } else {
                list[index] = item.copy(hasBeenViewed = true)
            }
            _vaultItems.value = list
            simulateSync()
        }
    }

    override fun sendHeartbeat() {
        _heartbeatPulseCount.value = _heartbeatPulseCount.value + 1
        simulateSync()
    }

    override fun setOfflineSimulation(offline: Boolean) {
        isSimulatingOffline = offline
        _syncStatus.value = if (offline) SyncStatus.SAVED_OFFLINE else SyncStatus.SAVED
    }

    private fun simulateSync() {
        if (isSimulatingOffline) {
            _syncStatus.value = SyncStatus.SAVED_OFFLINE
            return
        }
        _syncStatus.value = SyncStatus.SENDING
        scope.launch {
            delay(350)
            if (!isSimulatingOffline) {
                _syncStatus.value = SyncStatus.SAVED
            }
        }
    }
}
