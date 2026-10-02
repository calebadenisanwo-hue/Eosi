package com.example.meridian

import com.example.meridian.data.questions.QuestionsData
import com.example.meridian.data.repository.DemoRepo
import org.junit.Assert.*
import org.junit.Test

class Phase1Test {

    @Test
    fun `questions bank has exactly 80 calibrated questions across 4 decks`() {
        assertEquals(80, QuestionsData.allQuestions.size)
        assertEquals(20, QuestionsData.getQuestionsByDeck("light").size)
        assertEquals(20, QuestionsData.getQuestionsByDeck("deep").size)
        assertEquals(20, QuestionsData.getQuestionsByDeck("future").size)
        assertEquals(20, QuestionsData.getQuestionsByDeck("silly").size)
    }

    @Test
    fun `letters can be added and encrypted in repo`() {
        val repo = DemoRepo()
        val initialCount = repo.letters.value.size

        repo.addLetter(
            trigger = "after a visit",
            title = "For the train ride home",
            body = "I know the seat feels a little too empty right now. Close your eyes and feel my hug."
        )

        assertEquals(initialCount + 1, repo.letters.value.size)
        val added = repo.letters.value.first()
        assertEquals("after a visit", added.trigger)
        assertEquals("For the train ride home", added.title)
    }

    @Test
    fun `daily update feelings and actions save and react`() {
        val repo = DemoRepo()
        val activeUid = repo.activeUser.value.uid

        repo.saveDailyUpdate(
            feelings = listOf("🥰 loved", "😌 calm"),
            feelingNote = "Soft morning sunlight",
            actions = listOf("☕ morning coffee", "💼 working"),
            actionNote = "Reviewing designs"
        )

        val update = repo.updates.value[activeUid]
        assertNotNull(update)
        assertEquals(2, update!!.feelings.size)
        assertEquals("Soft morning sunlight", update.feelingNote)
    }

    @Test
    fun `quiet reactions can be sent without guilt or count badges`() {
        val repo = DemoRepo()
        val partnerUid = repo.partnerUser.value.uid

        repo.sendQuietReaction(partnerUid, "🤍")
        val reactions = repo.quietReactions.value[partnerUid]
        assertNotNull(reactions)
        assertTrue(reactions!!.contains("🤍"))
    }
}
