package com.example.meridian.data.timeline

/**
 * 30 original timeline templates with 2-3 variants each.
 * Tone: dry, tender, slightly wry, never saccharine.
 * BitLife-style life journal for a long-distance relationship.
 */
data class TimelineTemplate(
    val id: String,
    val triggerType: String,
    val emoji: String,
    val category: String,
    val variants: List<String>
)

object TimelineTemplates {
    val templates: List<TimelineTemplate> = listOf(
        TimelineTemplate(
            id = "t_100_questions",
            triggerType = "question_100",
            emoji = "💌",
            category = "Milestone",
            variants = listOf(
                "You answered your 100th daily question. Neither of you made a fuss.",
                "100 daily questions answered. Still learning new things about each other.",
                "Question number 100. That's a whole book of things you'd never have asked on a phone call."
            )
        ),
        TimelineTemplate(
            id = "t_first_visit_start",
            triggerType = "visit_start",
            emoji = "🛬",
            category = "Trip",
            variants = listOf(
                "The arrivals gate opened. The photos didn't do either of you justice.",
                "First time in the same room. You hugged like you forgot how arms work.",
                "Touchdown. No screen between you for the next nine days."
            )
        ),
        TimelineTemplate(
            id = "t_first_visit_end",
            triggerType = "visit_end",
            emoji = "🛫",
            category = "Trip",
            variants = listOf(
                "Airport security goodbye. The quiet on the train home is deafening.",
                "You checked into your flight. One suitcase smells like someone else's shampoo.",
                "First visit wrapped. Nine days felt like twenty minutes."
            )
        ),
        TimelineTemplate(
            id = "t_first_letter",
            triggerType = "letter_opened",
            emoji = "✉️",
            category = "Milestone",
            variants = listOf(
                "The first Open When letter was unsealed. It was read three times before bed.",
                "A sealed letter was finally opened. Exact right words, exact right hour.",
                "The wax broke. A little bit of reassurance delivered across continents."
            )
        ),
        TimelineTemplate(
            id = "t_anniversary_100d",
            triggerType = "days_100",
            emoji = "✨",
            category = "Milestone",
            variants = listOf(
                "100 days together. You've officially memorized each other's time zone math.",
                "100 days. You no longer calculate the offset with your fingers.",
                "100 days. Half of them spent falling asleep on video calls."
            )
        ),
        TimelineTemplate(
            id = "t_anniversary_1y",
            triggerType = "days_365",
            emoji = "🌿",
            category = "Milestone",
            variants = listOf(
                "One whole year. Hundreds of calls, thousands of kilometers, zero regrets.",
                "365 days as a couple. The distance is wide, but the roots are deep.",
                "One year in. You've celebrated birthdays, bad days, and boring Tuesdays."
            )
        ),
        TimelineTemplate(
            id = "t_countdown_set",
            triggerType = "countdown_new",
            emoji = "🗓️",
            category = "Trip",
            variants = listOf(
                "Tickets booked. The countdown ticker starts ticking down from 41 days.",
                "Flight confirmation in the inbox. Suddenly the calendar has a focal point.",
                "Next reunion date locked in. The days apart feel purposeful again."
            )
        ),
        TimelineTemplate(
            id = "t_wifi_crash",
            triggerType = "funny",
            emoji = "📶",
            category = "Funny",
            variants = listOf(
                "The video call froze mid-sentence on the most unflattering face possible.",
                "Five dropped calls in twelve minutes. You switched to voice and talked for two hours.",
                "Bad internet tried to end the conversation early. It lost."
            )
        ),
        TimelineTemplate(
            id = "t_care_package",
            triggerType = "package_received",
            emoji = "📦",
            category = "Milestone",
            variants = listOf(
                "Customs approved the package. Local sweets and handwritten notes arrived safely.",
                "A cardboard box traveled 6,000 km. It was opened with trembling kitchen scissors.",
                "The package arrived smelling faintly of cedar and Indian post office tape."
            )
        ),
        TimelineTemplate(
            id = "t_fika_tradition",
            triggerType = "habit",
            emoji = "☕",
            category = "First",
            variants = listOf(
                "A shared fika at 3pm Gothenburg time and 6:30pm Pune time. A new habit formed.",
                "You drank coffee together 6,480 kilometers apart. Routine is resistance.",
                "Tea in Pune, coffee in Gothenburg. Same warm cup in both hands."
            )
        ),
        TimelineTemplate(
            id = "t_silent_call",
            triggerType = "habit",
            emoji = "🎧",
            category = "Milestone",
            variants = listOf(
                "A call where neither of you spoke for 40 minutes because you were both working.",
                "Just the sound of breathing and typing through the earbuds. Quiet company.",
                "You don't have to entertain each other anymore. That's how you know."
            )
        ),
        TimelineTemplate(
            id = "t_dst_shift",
            triggerType = "dst",
            emoji = "⏰",
            category = "Milestone",
            variants = listOf(
                "The clocks jumped. You are now 4h 30m apart instead of 3h 30m.",
                "Europe fell back an hour. Bedtime arithmetic got recalculated.",
                "Daylight saving time intervened. The morning overlap window shifted."
            )
        ),
        TimelineTemplate(
            id = "t_sick_day",
            triggerType = "care",
            emoji = "🍵",
            category = "Hard-won",
            variants = listOf(
                "One of you had a fever. The other ordered soup from three time zones away.",
                "Being sick apart is the hardest part. You stayed on speaker until sleep came.",
                "Warm tea and soft check-ins. Long distance nursing at its finest."
            )
        ),
        TimelineTemplate(
            id = "t_rain_sound",
            triggerType = "moment",
            emoji = "🌧️",
            category = "Milestone",
            variants = listOf(
                "Monsoon in Pune. You held the phone out the window so Gothenburg could hear the rain.",
                "Rain on the tin roof. The microphone picked up every droplet.",
                "Listening to a storm on another continent. It sounded like home."
            )
        ),
        TimelineTemplate(
            id = "t_movie_sync",
            triggerType = "play",
            emoji = "🎬",
            category = "Funny",
            variants = listOf(
                "Press Play Together on 3-2-1. You both laughed at the exact same punchline.",
                "You spent ten minutes aligning the movie seconds. Worth it.",
                "Synchronized popcorn crunching. High-tech romance."
            )
        ),
        TimelineTemplate(
            id = "t_first_inside_joke",
            triggerType = "funny",
            emoji = "🤫",
            category = "Funny",
            variants = listOf(
                "An inside joke was born that no one else in your respective cities will ever understand.",
                "A typo that became permanent relationship lore.",
                "Someone said something absurd at 1am and it's now a family motto."
            )
        ),
        TimelineTemplate(
            id = "t_travel_pass",
            triggerType = "streak",
            emoji = "🧭",
            category = "Milestone",
            variants = listOf(
                "Streak kept alive across three airport layovers and a dead battery.",
                "Transit mode declared. No guilt, just safe arrivals.",
                "You crossed four borders this year to hold hands."
            )
        ),
        TimelineTemplate(
            id = "t_agreement_made",
            triggerType = "care",
            emoji = "🤝",
            category = "Hard-won",
            variants = listOf(
                "You agreed on the busy-day rule: a quick goodnight ping is always enough.",
                "A difficult conversation ended with an agreement, not a grudge.",
                "You figured out how to fight without letting time zones make it worse."
            )
        ),
        TimelineTemplate(
            id = "t_second_visit",
            triggerType = "visit",
            emoji = "🥨",
            category = "Trip",
            variants = listOf(
                "Gothenburg visit. Walking Haga streets without looking at a map.",
                "Second visit. No awkwardness at the gate, just a quiet sense of 'finally'.",
                "Tram rides through Linnegatan. Familiarity setting in."
            )
        ),
        TimelineTemplate(
            id = "t_shared_goal",
            triggerType = "plan",
            emoji = "🎯",
            category = "Milestone",
            variants = listOf(
                "The relocation savings fund crossed the halfway mark.",
                "A number on a screen that represents your future front door.",
                "Every euro saved is one step closer to the same zip code."
            )
        )
    )
}
