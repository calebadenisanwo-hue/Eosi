package com.example.meridian.data.repository

import com.example.meridian.data.model.*
import java.time.Instant
import kotlinx.coroutines.flow.StateFlow

/**
 * Common Repository Interface for Meridian.
 * Supports both Live (Firestore) and Demo (In-Memory/Encrypted) modes.
 */
interface MeridianRepo {
    val activeUser: StateFlow<UserProfile>
    val partnerUser: StateFlow<UserProfile>
    val spaceInfo: StateFlow<SpaceInfo>
    val syncStatus: StateFlow<SyncStatus>
    val myPresence: StateFlow<UserPresence>
    val partnerPresence: StateFlow<UserPresence>
    val updates: StateFlow<Map<String, DailyUpdate>>
    val quietReactions: StateFlow<Map<String, List<String>>>
    val currentQuestion: StateFlow<DailyQuestion>
    val currentQuestionAnswers: StateFlow<Map<String, QuestionAnswer>>
    val answeredArchive: StateFlow<List<Pair<DailyQuestion, Map<String, QuestionAnswer>>>>
    val letters: StateFlow<List<Letter>>
    val visits: StateFlow<List<Visit>>
    val timeline: StateFlow<List<TimelineItem>>
    val occasions: StateFlow<List<Occasion>>
    val streak: StateFlow<StreakInfo>
    val recentPings: StateFlow<List<QuickPing>>
    val isFingerprintVerified: StateFlow<Boolean>
    val hasUnreadTalk: StateFlow<Boolean>

    // Phase 3: Private Mode (18+)
    val isPrivateModeActive: StateFlow<Boolean>
    val isVaultUnlocked: StateFlow<Boolean>
    val vaultItems: StateFlow<List<VaultItem>>
    val heartbeatPulseCount: StateFlow<Int>

    fun switchPersona(targetUid: String)
    fun setPresence(state: String, note: String = "")
    fun sendPing(kind: String)
    fun sendArrivedSafe()
    fun saveDailyUpdate(feelings: List<String>, feelingNote: String, actions: List<String>, actionNote: String)
    fun sendQuietReaction(toUid: String, reaction: String)
    fun submitAnswer(questionId: String, answerText: String)
    fun drawNextQuestion(deck: String? = null)
    fun skipCurrentQuestion()
    fun addLetter(trigger: String, title: String, body: String, unsealAt: Instant? = null)
    fun openLetter(letterId: String)
    fun markFingerprintVerified()
    fun setOfflineSimulation(offline: Boolean)

    // Private Mode operations
    fun optInPrivateMode()
    fun disablePrivateMode()
    fun deleteVaultForBoth()
    fun unlockVault(pin: String): Boolean
    fun lockVault()
    fun addVaultItem(title: String, body: String, kind: String, viewOnce: Boolean)
    fun markVaultItemViewed(id: String)
    fun sendHeartbeat()
}
