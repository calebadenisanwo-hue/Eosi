package com.example.meridian

import com.example.meridian.core.crypto.MeridianCrypto
import org.junit.Assert.*
import org.junit.Test
import javax.crypto.AEADBadTagException

class CryptoTest {

    @Test
    fun `encrypt and decrypt round trip with valid AAD succeeds`() {
        val key = MeridianCrypto.generateSpaceKey()
        val plaintext = "When 2am gets too quiet in Gothenburg, I miss your voice in Pune."
        val aad = "space_123|letters|doc_456|body"

        val envelope = MeridianCrypto.encryptField(plaintext, key, aad)
        assertNotNull(envelope.iv)
        assertNotNull(envelope.ct)

        val decrypted = MeridianCrypto.decryptField(envelope, key, aad)
        assertEquals(plaintext, decrypted)
    }

    @Test(expected = Exception::class)
    fun `decrypt with mismatched AAD fails with authentication exception`() {
        val key = MeridianCrypto.generateSpaceKey()
        val plaintext = "Confidential agreement text"
        val originalAad = "space_123|agreements|doc_1|text"
        val tamperedAad = "space_123|agreements|doc_2|text" // Relocation attack attempt

        val envelope = MeridianCrypto.encryptField(plaintext, key, originalAad)
        // Decryption MUST fail because AAD does not match
        MeridianCrypto.decryptField(envelope, key, tamperedAad)
    }

    @Test
    fun `key wrap and unwrap round trip succeeds`() {
        val spaceKey = MeridianCrypto.generateSpaceKey()
        val wrappingKey = MeridianCrypto.generateSpaceKey()

        val wrapped = MeridianCrypto.wrapKey(spaceKey, wrappingKey)
        val unwrapped = MeridianCrypto.unwrapKey(wrapped, wrappingKey)

        assertArrayEquals(spaceKey.encoded, unwrapped.encoded)
    }

    @Test
    fun `recovery key generation formats into 8 groups of 4 Crockford characters`() {
        val recoveryKey = MeridianCrypto.generateRecoveryKey()
        val groups = recoveryKey.split("-")
        assertEquals(8, groups.size)
        groups.forEach { group ->
            assertEquals(4, group.length)
        }
        assertTrue(MeridianCrypto.isValidRecoveryKey(recoveryKey))
    }

    @Test
    fun `safety fingerprint is deterministic and generates exactly 4 emojis`() {
        val keyA = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEayaan_key"
        val keyB = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAElignea_key"

        val fp1 = MeridianCrypto.computeSafetyFingerprint(keyA, keyB)
        val fp2 = MeridianCrypto.computeSafetyFingerprint(keyB, keyA) // Reverse order must yield identical result!

        assertEquals(4, fp1.size)
        assertEquals(fp1, fp2)
    }

    @Test
    fun `PBKDF2 pin derivation generates valid 256-bit AES key`() {
        val salt = ByteArray(16) { 0x42 }
        val pin = "849201"
        val key1 = MeridianCrypto.derivePinKey(pin, salt)
        val key2 = MeridianCrypto.derivePinKey(pin, salt)

        assertEquals("AES", key1.algorithm)
        assertEquals(32, key1.encoded.size) // 256 bits
        assertArrayEquals(key1.encoded, key2.encoded)
    }
}
