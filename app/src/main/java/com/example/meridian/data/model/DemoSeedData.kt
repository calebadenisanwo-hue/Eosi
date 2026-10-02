package com.example.meridian.data.model

import java.time.Instant
import java.time.LocalDate
import java.time.LocalTime
import java.time.ZoneId
import java.time.temporal.ChronoUnit

/**
 * Seed data for Meridian Demo Mode.
 * Features Ayaan (Pune) & Linnea (Gothenburg), together for 14 months,
 * with real relative dates computed from current time.
 */
object DemoSeedData {

    const val AYAAN_UID = "user_ayaan_pune"
    const val LINNEA_UID = "user_linnea_gothenburg"
    const val DEMO_SPACE_ID = "space_meridian_demo"

    val ayaanProfile = UserProfile(
        uid = AYAAN_UID,
        displayName = "Ayaan",
        avatarEmoji = "☕",
        homeCity = "Pune",
        timezone = ZoneId.of("Asia/Kolkata"),
        awakeStart = LocalTime.of(7, 30),
        awakeEnd = LocalTime.of(23, 30),
        birthdayMonth = 11,
        birthdayDay = 19,
        publicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEayaan_sample_pubkey_p256_mock_base64_encoded_pune"
    )

    val linneaProfile = UserProfile(
        uid = LINNEA_UID,
        displayName = "Linnea",
        avatarEmoji = "🌿",
        homeCity = "Gothenburg",
        timezone = ZoneId.of("Europe/Stockholm"),
        awakeStart = LocalTime.of(7, 0),
        awakeEnd = LocalTime.of(23, 0),
        birthdayMonth = LocalDate.now().plusDays(12).monthValue,
        birthdayDay = LocalDate.now().plusDays(12).dayOfMonth,
        publicKey = "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAElignea_sample_pubkey_p256_mock_base64_encoded_goth"
    )

    val spaceInfo = SpaceInfo(
        spaceId = DEMO_SPACE_ID,
        members = listOf(AYAAN_UID, LINNEA_UID),
        createdBy = AYAAN_UID,
        status = "active",
        flags = SpaceFlags(
            privateOptIn = mapOf(AYAAN_UID to false, LINNEA_UID to false),
            streakOptIn = mapOf(AYAAN_UID to true, LINNEA_UID to true),
            checkinsOptIn = mapOf(AYAAN_UID to true, LINNEA_UID to true),
            revealUpdatesTogether = false
        ),
        anniversaryDate = LocalDate.now().minusMonths(14)
    )

    val initialVisits = listOf(
        Visit(
            id = "v1",
            place = "Pune",
            startDate = LocalDate.now().minusMonths(10),
            endDate = LocalDate.now().minusMonths(10).plusDays(8),
            notes = "First time in the same room. Rain on the balcony in Koregaon Park.",
            isPast = true
        ),
        Visit(
            id = "v2",
            place = "Gothenburg",
            startDate = LocalDate.now().minusMonths(6),
            endDate = LocalDate.now().minusMonths(6).plusDays(5),
            notes = "Fika at Haga, kanelbullar the size of dinner plates.",
            isPast = true
        ),
        Visit(
            id = "v3",
            place = "Gothenburg",
            startDate = LocalDate.now().minusMonths(2),
            endDate = LocalDate.now().minusMonths(2).plusDays(11),
            notes = "Midsummer archipelago ferry. Neither of us wanted to head to Landvetter.",
            isPast = true
        ),
        Visit(
            id = "v4_next",
            place = "Pune",
            startDate = LocalDate.now().plusDays(41),
            endDate = LocalDate.now().plusDays(54),
            notes = "Winter visit. Exploring Old Pune markets together.",
            isPast = false
        )
    )

    val demoQuestions = listOf(
        DailyQuestion("q1", "deep", "What's the most ordinary thing near you right now that you wish I could see?"),
        DailyQuestion("q2", "future", "Describe a quiet Tuesday evening in our first month living in the same city."),
        DailyQuestion("q3", "silly", "If our relationship were a comedy series, what would be the recurring running gag?"),
        DailyQuestion("q4", "light", "What sound or smell instantly teleports you back to our last visit?")
    )

    val initialAnswers = mapOf(
        ("q1" to AYAAN_UID) to QuestionAnswer(
            questionId = "q1",
            uid = AYAAN_UID,
            answerText = "The way afternoon sun reflects off the stainless steel kettle onto the kitchen tiles. You'd love making tea right here."
        ),
        ("q1" to LINNEA_UID) to QuestionAnswer(
            questionId = "q1",
            uid = LINNEA_UID,
            answerText = "The pile of sweaters on my armchair. One of them is still yours and smells faintly like cardamom and cedar."
        )
    )

    val initialLetters = listOf(
        Letter(
            id = "let_1",
            fromUid = AYAAN_UID,
            toUid = LINNEA_UID,
            trigger = "can't sleep",
            title = "When 2am gets too quiet",
            body = "Breathe in for four, hold for four, out for six. Even if I'm fast asleep in Pune, picture me holding your left hand. Close your eyes, you're safe.",
            unsealAt = null,
            openedAt = null
        ),
        Letter(
            id = "let_2",
            fromUid = LINNEA_UID,
            toUid = AYAAN_UID,
            trigger = "bad day",
            title = "A gentle reminder for tired shoulders",
            body = "Whatever happened today is just one bad chapter, not the whole book. Drop your shoulders away from your ears right now. Drink a tall glass of water. Call me when you can.",
            unsealAt = null,
            openedAt = null
        ),
        Letter(
            id = "let_3",
            fromUid = AYAAN_UID,
            toUid = LINNEA_UID,
            trigger = "birthday",
            title = "Happy Birthday my love",
            body = "Happy Birthday! Today is all yours. Waking up in Gothenburg to another year of you in the world. I have a tiny package on its way via post!",
            unsealAt = Instant.now().plus(12, ChronoUnit.DAYS),
            openedAt = null
        ),
        Letter(
            id = "let_4",
            fromUid = LINNEA_UID,
            toUid = AYAAN_UID,
            trigger = "I miss you",
            title = "A little pocket of Gothenburg sun",
            body = "Sitting by the canal near Brunnsparken. A seagull just tried to steal someone's pastry. Wishing your laugh was right next to my ear.",
            unsealAt = null,
            openedAt = Instant.now().minus(2, ChronoUnit.DAYS)
        )
    )

    val initialTimeline = listOf(
        TimelineItem("t1", "14 months ago", "✨", "First call that lasted until sunrise in Pune. Neither noticed the hours passing.", "First"),
        TimelineItem("t2", "10 months ago", "🛬", "Day 122: First visit. Pune. 9 days together in the rain.", "Trip", autoGenerated = true),
        TimelineItem("t3", "6 months ago", "☕", "Day 240: Gothenburg. 6 days of kanelbullar and tram rides.", "Trip", autoGenerated = true),
        TimelineItem("t4", "3 months ago", "💌", "You answered your 100th daily question. Neither of you made a fuss.", "Milestone", autoGenerated = true),
        TimelineItem("t5", "2 months ago", "🌊", "Day 360: Archipelago ferries and midnight twilight.", "Trip", autoGenerated = true),
        TimelineItem("t6", "Today", "🧭", "312 days connected. 41 days until the Pune reunion.", "Milestone")
    )

    val initialOccasions = listOf(
        Occasion("o1", "birthday", "Linnea's Birthday", LocalDate.now().plusDays(12).monthValue, LocalDate.now().plusDays(12).dayOfMonth, forUid = LINNEA_UID),
        Occasion("o2", "birthday", "Ayaan's Birthday", 11, 19, forUid = AYAAN_UID),
        Occasion("o3", "anniversary", "Together Since", LocalDate.now().minusMonths(14).monthValue, LocalDate.now().minusMonths(14).dayOfMonth),
        Occasion("o4", "person", "Meenal (Ayaan's mum)", 8, 15),
        Occasion("o5", "person", "Saga (Linnea's best friend)", 4, 3)
    )
}
