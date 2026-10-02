package com.example.meridian

import com.example.meridian.data.questions.SpicyQuestionsData
import com.example.meridian.data.repository.DemoRepo
import com.example.meridian.data.vault.FlirtPromptsData
import org.junit.Assert.*
import org.junit.Test

class Phase3Test {

    @Test
    fun `flirt prompts bank contains 40 tasteful prompts`() {
        assertEquals(40, FlirtPromptsData.prompts.size)
        assertTrue(FlirtPromptsData.prompts.all { it.isNotBlank() })
    }

    @Test
    fun `spicy deck contains 20 intimate questions`() {
        assertEquals(20, SpicyQuestionsData.questions.size)
        assertTrue(SpicyQuestionsData.questions.all { it.deck == "spicy" && it.promptText.isNotBlank() })
    }

    @Test
    fun `vault unlocks with correct 6-digit pin and locks properly`() {
        val repo = DemoRepo()
        assertFalse(repo.isVaultUnlocked.value)

        val unlocked = repo.unlockVault("123456")
        assertTrue(unlocked)
        assertTrue(repo.isVaultUnlocked.value)

        repo.lockVault()
        assertFalse(repo.isVaultUnlocked.value)
    }

    @Test
    fun `view-once items auto-destruct when partner views them`() {
        val repo = DemoRepo()
        val initialCount = repo.vaultItems.value.size

        // Add a view-once note created by Ayaan
        repo.addVaultItem(
            title = "View Once Secret",
            body = "This message will self-destruct once read.",
            kind = "view_once",
            viewOnce = true
        )

        assertEquals(initialCount + 1, repo.vaultItems.value.size)
        val added = repo.vaultItems.value.first()

        // Switch to Linnea (the partner)
        repo.switchPersona("user_linnea_gothenburg")
        repo.markVaultItemViewed(added.id)

        // View-once item must be removed from vaultItems!
        val remaining = repo.vaultItems.value.filter { it.id == added.id }
        assertTrue("View-once item must auto-destruct after partner reads it", remaining.isEmpty())
    }

    @Test
    fun `delete vault for both purges all private items`() {
        val repo = DemoRepo()
        assertTrue(repo.vaultItems.value.isNotEmpty())

        repo.deleteVaultForBoth()

        assertTrue(repo.vaultItems.value.isEmpty())
        assertFalse(repo.isPrivateModeActive.value)
        assertFalse(repo.isVaultUnlocked.value)
    }
}
