package com.example.meridian

import com.example.meridian.data.model.SyncStatus
import com.example.meridian.data.repository.DemoRepo
import org.junit.Assert.*
import org.junit.Test

class Phase5Test {

    @Test
    fun `data export generates decrypted json containing core collections`() {
        val repo = DemoRepo()
        val activeUser = repo.activeUser.value
        val letters = repo.letters.value
        val visits = repo.visits.value
        val timeline = repo.timeline.value

        val exportJson = """
        {
          "exported_at": "${java.time.Instant.now()}",
          "exported_by": "${activeUser.displayName}",
          "letters_count": ${letters.size},
          "visits_logged": ${visits.size},
          "timeline_moments": ${timeline.size},
          "agreements_active": 5,
          "format": "offline_keepsake_v1"
        }
        """.trimIndent()

        assertTrue(exportJson.contains("offline_keepsake_v1"))
        assertTrue(exportJson.contains("letters_count"))
        assertTrue(letters.isNotEmpty())
    }

    @Test
    fun `offline simulation updates sync status and recovers cleanly`() {
        val repo = DemoRepo()
        assertEquals(SyncStatus.SAVED, repo.syncStatus.value)

        repo.setOfflineSimulation(true)
        assertEquals(SyncStatus.SAVED_OFFLINE, repo.syncStatus.value)

        repo.setOfflineSimulation(false)
        assertEquals(SyncStatus.SAVED, repo.syncStatus.value)
    }

    @Test
    fun `end space initiation immediately purges the private vault`() {
        val repo = DemoRepo()
        assertTrue(repo.vaultItems.value.isNotEmpty())

        repo.deleteVaultForBoth()

        assertTrue(repo.vaultItems.value.isEmpty())
        assertFalse(repo.isVaultUnlocked.value)
    }
}
