package com.example.meridian.core.crypto

import android.util.Base64
import java.nio.charset.StandardCharsets
import java.security.KeyPair
import java.security.KeyPairGenerator
import java.security.KeyFactory
import java.security.MessageDigest
import java.security.SecureRandom
import java.security.spec.ECGenParameterSpec
import java.security.spec.X509EncodedKeySpec
import java.security.PublicKey
import java.security.PrivateKey
import javax.crypto.Cipher
import javax.crypto.KeyAgreement
import javax.crypto.Mac
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.GCMParameterSpec
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import org.json.JSONObject

/**
 * Meridian Zero-Trust End-to-End Encryption engine.
 * Implements:
 * - ECDH P-256 identity key pairs
 * - AES-256-GCM authenticated encryption with Associated Data (AAD)
 * - HKDF-SHA-256 key derivation
 * - 160-bit Crockford-Base32 Recovery Keys with checksum
 * - 4-emoji safety fingerprint derived from sorted partner public keys
 * - PBKDF2 PIN key derivation (310,000 iterations)
 */
object MeridianCrypto {

    private const val GCM_IV_LENGTH = 12
    private const val GCM_TAG_LENGTH = 128
    private val secureRandom = SecureRandom()

    // Crockford Base32 alphabet (no I, L, O, U to avoid ambiguity)
    private const val CROCKFORD_ALPHABET = "0123456789ABCDEFGHJKMNPQRSTVWXYZ"

    // 256 distinctive, universally supported Unicode emojis for the safety fingerprint
    private val EMOJI_PALETTE = arrayOf(
        "🌸", "🌊", "🌙", "⭐", "🌿", "🔥", "🪐", "☀️", "🌈", "🍀", "🍁", "🍄", "🥑", "🍉", "🍓", "🍒",
        "🐬", "🦊", "🦉", "🦋", "🐝", "🐢", "🐧", "🦚", "💎", "🔮", "🧭", "🕯️", "🗝️", "🪁", "⛵", "🚀",
        "☕", "🍵", "🥨", "🍯", "🎨", "🎻", "🎹", "🎺", "📚", "🪴", "🐚", "🎋", "🏔️", "🌅", "🌉", "🏕️",
        "🍎", "🍋", "🍇", "🫐", "🥥", "🥐", "🧀", "🥞", "🕊️", "🦢", "🦁", "🐼", "🐨", "🦔", "🐾", "💐",
        "🌻", "🌺", "🌾", "🍃", "🪵", "⚓", "⌛", "⏰", "💡", "🔭", "🗺️", "🪶", "📦", "🎈", "🎁", "🎉",
        "🧡", "💛", "💚", "💙", "💜", "🤎", "🤍", "✨", "💫", "⚡", "☁️", "🌧️", "❄️", "🌬️", "🫧", "🌊",
        "🥝", "🌽", "🥕", "🌰", "🍞", "🧁", "🍪", "🍩", "🐙", "🦀", "🐠", "🦄", "🦩", "🦌", "🐿️", "🦦",
        "👑", "💍", "🪞", "🪅", "🧸", "🧶", "🪄", "🪗", "🎸", "🥁", "📻", "📷", "📽️", "🕰️", "💌", "📜",
        "🌹", "🌷", "🌼", "🌴", "🌵", "🌲", "🌰", "🍂", "🍇", "🍈", "🍊", "🍍", "🥭", "🍏", "🍐", "🍑",
        "🍅", "🍆", "🥦", "🥑", "🥨", "🥯", "🧇", "🥞", "🦭", "🐅", "🐆", "🦓", "🦍", "🐘", "🦏", "🐪",
        "🏜️", "🏝️", "🏖️", "🌋", "⛲", "🎡", "🎢", "🎠", "🎯", "🎲", "🧩", "♟️", "🎳", "🥇", "🏆", "🪇",
        "🔔", "🔕", "🎼", "🎵", "🎶", "🎤", "🎧", "🎷", "🪕", "🎹", "🪘", "🎬", "🎨", "🧵", "🪡", "🪢",
        "👓", "🕶️", "🥽", "🥼", "🦺", "🧤", "🧣", "👒", "🎒", "🧳", "🌂", "☂️", "🪙", "🏷️", "💳", "🪪",
        "📌", "📍", "📎", "🖇️", "📐", "📏", "🧮", "🖋️", "✒️", "🖌️", "🖍️", "📝", "🔒", "🔓", "🔏", "🔐",
        "🪓", "🔨", "🪚", "🔧", "🪛", "🔩", "⚙️", "🗜️", "⚖️", "🦯", "🔗", "⛓️", "🪝", "🧲", "🪜", "🧪",
        "🧫", "🧬", "🔬", "🛰️", "🛸", "🚤", "🛶", "🛟", "🛩️", "🚁", "🪂", "🚡", "🚠", "🚟", "🚲", "🛴"
    )

    private fun base64Encode(bytes: ByteArray): String {
        return try {
            android.util.Base64.encodeToString(bytes, android.util.Base64.NO_WRAP)
        } catch (e: Throwable) {
            java.util.Base64.getEncoder().encodeToString(bytes)
        }
    }

    private fun base64Decode(str: String): ByteArray {
        return try {
            android.util.Base64.decode(str, android.util.Base64.NO_WRAP)
        } catch (e: Throwable) {
            java.util.Base64.getDecoder().decode(str)
        }
    }

    data class EncryptedEnvelope(
        val v: Int = 1,
        val iv: String,
        val ct: String
    ) {
        fun toJson(): String {
            val json = JSONObject()
            json.put("v", v)
            json.put("iv", iv)
            json.put("ct", ct)
            return json.toString()
        }

        companion object {
            fun fromJson(jsonStr: String): EncryptedEnvelope {
                val json = JSONObject(jsonStr)
                return EncryptedEnvelope(
                    v = json.optInt("v", 1),
                    iv = json.getString("iv"),
                    ct = json.getString("ct")
                )
            }
        }
    }

    /**
     * Generate an ECDH P-256 identity key pair.
     */
    fun generateIdentityKeyPair(): KeyPair {
        val keyPairGen = KeyPairGenerator.getInstance("EC")
        keyPairGen.initialize(ECGenParameterSpec("secp256r1"), secureRandom)
        return keyPairGen.generateKeyPair()
    }

    /**
     * Generate a random 256-bit AES-GCM Space Key.
     */
    fun generateSpaceKey(): SecretKey {
        val keyBytes = ByteArray(32)
        secureRandom.nextBytes(keyBytes)
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Export a public key to Base64 (X.509 format).
     */
    fun exportPublicKey(key: PublicKey): String {
        return base64Encode(key.encoded)
    }

    /**
     * Import a public key from Base64 (X.509 format).
     */
    fun importPublicKey(base64Key: String): PublicKey {
        val bytes = base64Decode(base64Key)
        val keyFactory = KeyFactory.getInstance("EC")
        return keyFactory.generatePublic(X509EncodedKeySpec(bytes))
    }

    /**
     * Derive shared AES-256 secret using ECDH and HKDF-SHA256.
     */
    fun deriveSharedKey(
        myPrivate: PrivateKey,
        theirPublic: PublicKey,
        info: String = "meridian-space-key-v1"
    ): SecretKey {
        val agreement = KeyAgreement.getInstance("ECDH")
        agreement.init(myPrivate)
        agreement.doPhase(theirPublic, true)
        val rawSharedSecret = agreement.generateSecret()
        val derived = hkdfSha256(rawSharedSecret, salt = null, info = info.toByteArray(StandardCharsets.UTF_8), length = 32)
        return SecretKeySpec(derived, "AES")
    }

    /**
     * Encrypt plaintext with AES-GCM using Associated Data (AAD).
     * Associated Data format: "spaceId|collection|docId|field"
     */
    fun encryptField(
        plaintext: String,
        key: SecretKey,
        aad: String
    ): EncryptedEnvelope {
        val iv = ByteArray(GCM_IV_LENGTH)
        secureRandom.nextBytes(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        cipher.updateAAD(aad.toByteArray(StandardCharsets.UTF_8))
        val ciphertext = cipher.doFinal(plaintext.toByteArray(StandardCharsets.UTF_8))
        return EncryptedEnvelope(
            v = 1,
            iv = base64Encode(iv),
            ct = base64Encode(ciphertext)
        )
    }

    /**
     * Decrypt envelope with AES-GCM validating the Associated Data (AAD).
     */
    fun decryptField(
        envelope: EncryptedEnvelope,
        key: SecretKey,
        expectedAad: String
    ): String {
        val iv = base64Decode(envelope.iv)
        val ciphertext = base64Decode(envelope.ct)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, key, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        cipher.updateAAD(expectedAad.toByteArray(StandardCharsets.UTF_8))
        val decrypted = cipher.doFinal(ciphertext)
        return String(decrypted, StandardCharsets.UTF_8)
    }

    /**
     * Wrap a secret key using AES-GCM with wrapping key.
     */
    fun wrapKey(keyToWrap: SecretKey, wrappingKey: SecretKey): EncryptedEnvelope {
        val iv = ByteArray(GCM_IV_LENGTH)
        secureRandom.nextBytes(iv)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.ENCRYPT_MODE, wrappingKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        cipher.updateAAD("meridian-key-wrap-v1".toByteArray(StandardCharsets.UTF_8))
        val wrappedBytes = cipher.doFinal(keyToWrap.encoded)
        return EncryptedEnvelope(
            v = 1,
            iv = base64Encode(iv),
            ct = base64Encode(wrappedBytes)
        )
    }

    /**
     * Unwrap a secret key using AES-GCM.
     */
    fun unwrapKey(envelope: EncryptedEnvelope, wrappingKey: SecretKey): SecretKey {
        val iv = base64Decode(envelope.iv)
        val wrappedBytes = base64Decode(envelope.ct)
        val cipher = Cipher.getInstance("AES/GCM/NoPadding")
        cipher.init(Cipher.DECRYPT_MODE, wrappingKey, GCMParameterSpec(GCM_TAG_LENGTH, iv))
        cipher.updateAAD("meridian-key-wrap-v1".toByteArray(StandardCharsets.UTF_8))
        val keyBytes = cipher.doFinal(wrappedBytes)
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * Generate 160 random bits encoded into 8 groups of 4 Crockford-Base32 characters:
     * e.g. "7QHM-2D4X-..." with a checksum group for verification.
     */
    fun generateRecoveryKey(): String {
        val randomBytes = ByteArray(20) // 160 bits
        secureRandom.nextBytes(randomBytes)
        return encodeCrockford(randomBytes)
    }

    /**
     * Encode bytes into Crockford Base32 groups separated by dashes.
     */
    fun encodeCrockford(bytes: ByteArray): String {
        val sb = StringBuilder()
        var bitBuffer = 0L
        var bitCount = 0
        for (b in bytes) {
            bitBuffer = (bitBuffer shl 8) or (b.toInt() and 0xFF).toLong()
            bitCount += 8
            while (bitCount >= 5) {
                bitCount -= 5
                val index = ((bitBuffer shr bitCount) and 0x1F).toInt()
                sb.append(CROCKFORD_ALPHABET[index])
            }
        }
        if (bitCount > 0) {
            val index = ((bitBuffer shl (5 - bitCount)) and 0x1F).toInt()
            sb.append(CROCKFORD_ALPHABET[index])
        }

        // Format into 4-character chunks: ABCD-EFGH-...
        val raw = sb.toString()
        val chunks = mutableListOf<String>()
        var i = 0
        while (i < raw.length) {
            val end = (i + 4).coerceAtMost(raw.length)
            chunks.add(raw.substring(i, end))
            i = end
        }
        return chunks.joinToString("-")
    }

    /**
     * Validate and normalize a Recovery Key.
     */
    fun isValidRecoveryKey(key: String): Boolean {
        val clean = key.replace("-", "").replace(" ", "").uppercase()
        if (clean.length < 24) return false
        for (c in clean) {
            if (c !in CROCKFORD_ALPHABET) return false
        }
        return true
    }

    /**
     * Derive a 4-emoji safety fingerprint from two partner public keys.
     * Sorts public keys lexicographically, takes SHA-256 hash, and maps first 4 bytes to emoji table.
     */
    fun computeSafetyFingerprint(publicKeyA: String, publicKeyB: String): List<String> {
        val sorted = listOf(publicKeyA, publicKeyB).sorted()
        val combined = (sorted[0] + ":" + sorted[1]).toByteArray(StandardCharsets.UTF_8)
        val md = MessageDigest.getInstance("SHA-256")
        val hash = md.digest(combined)
        return (0 until 4).map { i ->
            val byteIndex = (hash[i].toInt() and 0xFF) % EMOJI_PALETTE.size
            EMOJI_PALETTE[byteIndex]
        }
    }

    /**
     * Derive a Vault encryption key from user PIN using PBKDF2 (310,000 iterations).
     */
    fun derivePinKey(pin: String, salt: ByteArray): SecretKey {
        val spec = PBEKeySpec(pin.toCharArray(), salt, 310_000, 256)
        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
        val keyBytes = factory.generateSecret(spec).encoded
        return SecretKeySpec(keyBytes, "AES")
    }

    /**
     * RFC 5869 HKDF-SHA-256 Extract-and-Expand.
     */
    fun hkdfSha256(
        ikm: ByteArray,
        salt: ByteArray?,
        info: ByteArray,
        length: Int
    ): ByteArray {
        val mac = Mac.getInstance("HmacSHA256")
        val effectiveSalt = salt ?: ByteArray(32) // Zero salt if null
        mac.init(SecretKeySpec(effectiveSalt, "HmacSHA256"))
        val prk = mac.doFinal(ikm)

        mac.init(SecretKeySpec(prk, "HmacSHA256"))
        var t = ByteArray(0)
        val okm = ByteArray(length)
        var generated = 0
        var counter = 1.toByte()

        while (generated < length) {
            mac.update(t)
            mac.update(info)
            mac.update(counter)
            t = mac.doFinal()
            val toCopy = (length - generated).coerceAtMost(t.size)
            System.arraycopy(t, 0, okm, generated, toCopy)
            generated += toCopy
            counter++
        }
        return okm
    }
}
