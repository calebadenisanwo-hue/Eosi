# Meridian Privacy Architecture & Threat Model

Meridian is designed around **zero-trust privacy by construction**. Personal content is end-to-end encrypted on the client device before touching any network or database.

## 1. What the Server Can and Cannot See

### Visible to the Database Server (Plaintext metadata only)
- User identifiers (`uid`), public ECDH identity keys (`publicKey`), and creation timestamps.
- Space membership array: exactly two member UIDs (`members: [uidA, uidB]`).
- Single-use invite hash: SHA-256 hash of the 10-character code (`ABCD-EFGH-JK`), expiration timestamp, and join request public key.
- Time-gating and reveal-together coordination fields:
  - `unsealAt`: timestamp when a letter becomes readable to the recipient.
  - `questionId`: identifier of the daily question being answered.
  - `dateKey`: author's local date (`YYYY-MM-DD`) for daily updates.
  - `kind` / `status`: system enum for routing (e.g. `tap`, `hug`, `arrived_safe`).
- Opt-in consent flags: `privateOptIn`, `streakOptIn`, `checkinsOptIn`.

### Completely Encrypted (Inaccessible to Host, DB Admin, or Network Eavesdropper)
- **All message & letter contents:** text bodies, letter subjects, photo captions, voice note chunks.
- **Daily updates:** feeling selections, feeling notes, action selections, action notes.
- **Question answers:** both partners' answers are AES-GCM encrypted and locked behind reveal-together Firestore rules.
- **Profiles:** timezone, home city name/coordinates, birthdays, pronouns, awake/quiet hours.
- **Presence note:** custom status notes.
- **Care & Agreements:** Pause & Repair prompts (*what I heard*, *what I felt*, *what I need*, *one thing I can do*), relationship agreements, check-in answers.
- **Timeline & Moments:** memory titles, emojis, custom photos, anniversary milestones.
- **Private Mode Vault:** dual-consent encrypted with a secondary Vault Key wrapped by local PIN PBKDF2 keys.

## 2. Cryptographic Architecture

1. **Identity Key Pair:** ECDH P-256 generated on-device. Private key kept in local secure storage; public key shared via Firestore.
2. **Space Key:** 256-bit AES-GCM key generated upon Space creation. Wrapped for each partner using ECDH shared secret derived with HKDF-SHA256 (`meridian-space-key-v1`).
3. **Authenticated Additional Data (AAD):** Every encrypted field binds `spaceId|collection|docId|field` as AAD to prevent ciphertext relocation attacks.
4. **Recovery Key:** 160 random bits encoded into 8 groups of 4 Crockford-Base32 characters (`7QHM-2D4X-...`) with a check group. The private key is wrapped using an HKDF derivation of this recovery key.
5. **4-Emoji Safety Fingerprint:** Deterministic 4-emoji fingerprint derived from the SHA-256 hash of the lexicographically sorted public keys of both partners, mapped into a curated 256-emoji lookup table.
6. **Local App & Vault PIN:** PBKDF2 with HMAC-SHA-256 (310,000+ iterations) with per-device random salt.

## 3. What Meridian Cannot Protect Against
- Physical compromise of an unlocked device.
- Screenshots taken on the partner's device (honest disclosure provided in UI).
- Compromised or malicious operating system binaries.
