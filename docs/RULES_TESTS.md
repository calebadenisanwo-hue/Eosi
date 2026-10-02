# Firestore Security Rules Test Checklist

This test plan validates zero-trust constraints in `firestore.rules`.

## Test Scenarios

### 1. Two-Member Space Cap & Non-Member Denial
- [x] **TC-1.1**: Authenticated user can create a space with `members: [uidA, uidB]` (size <= 2).
- [x] **TC-1.2**: Attempt to create a space with 3 or more members is rejected by Firestore rules.
- [x] **TC-1.3**: Third-party user (`uidC` not in `members`) attempting `get` or `list` on `/spaces/{spaceId}/**` receives `PERMISSION_DENIED`.

### 2. Single-Use Invite Protection
- [x] **TC-2.1**: Invites collection denies `list` requests globally (enumeration protection).
- [x] **TC-2.2**: Invites can only be read with exact SHA-256 document key (`invites/{hash}`).
- [x] **TC-2.3**: Expired invite cannot be joined (`request.time > expiresAt`).

### 3. Reveal-Together Question Answers
- [x] **TC-3.1**: Partner A writes answer to `question_101`. Write succeeds.
- [x] **TC-3.2**: Partner B attempts to read Partner A's answer BEFORE writing their own answer. Read is **DENIED** (`exists(...)` rule failure).
- [x] **TC-3.3**: Partner B submits their own answer to `question_101`. Write succeeds.
- [x] **TC-3.4**: Partner B attempts to read Partner A's answer AFTER writing their own answer. Read **SUCCEEDS**.

### 4. Time-Gated Letters (Open When / Birthday)
- [x] **TC-4.1**: Author can always read their own scheduled letter at any time.
- [x] **TC-4.2**: Recipient attempts to read letter where `request.time < unsealAt`. Read is **DENIED**.
- [x] **TC-4.3**: Recipient attempts to read letter where `request.time >= unsealAt`. Read **SUCCEEDS**.
- [x] **TC-4.4**: Recipient attempts to modify letter body. Write is **DENIED** (only sender or allowed unseal state updates permitted).

### 5. Private Mode Vault Dual-Consent Gate
- [x] **TC-5.1**: Neither partner opted in (`privateOptIn[uidA] == false`). Reads to `/spaces/{spaceId}/vault/**` are **DENIED**.
- [x] **TC-5.2**: Partner A opts in, Partner B has not opted in. Reads to `/spaces/{spaceId}/vault/**` are **DENIED**.
- [x] **TC-5.3**: Both Partner A and Partner B opt in (`privateOptIn[uidA] == true` AND `privateOptIn[uidB] == true`). Reads **SUCCEED**.
- [x] **TC-5.4**: Either partner toggles opt-in to `false`. Reads immediately return **DENIED** for both.
