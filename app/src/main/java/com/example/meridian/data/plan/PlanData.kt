package com.example.meridian.data.plan

data class RoadmapStage(
    val stageNumber: Int,
    val name: String,
    val status: String, // "COMPLETED", "IN_PROGRESS", "UPCOMING"
    val description: String,
    val tasks: List<RoadmapTask>
)

data class RoadmapTask(
    val id: String,
    val title: String,
    val isCompleted: Boolean,
    val assignedTo: String
)

data class DecisionTopic(
    val id: String,
    val title: String,
    val prosAyaan: List<String>,
    val prosLinnea: List<String>,
    val feelingsAyaan: Int = 85,
    val feelingsLinnea: Int = 90
)

data class PackingItem(
    val id: String,
    val name: String,
    val assignedUid: String,
    val isChecked: Boolean = false
)

data class ItineraryItem(
    val id: String,
    val activity: String,
    val category: String, // "Must Do", "Nice to Have", "Rain Backup"
    val isDone: Boolean = false
)

object PlanData {
    var pinnedWhy: String = "Why we are doing this: A sunlit apartment with a yellow door, Sunday fika without train departures, and never having to say goodbye at an airport gate again."

    var sharedGoalTarget: Double = 12000.0
    var sharedGoalCurrent: Double = 6850.0

    val initialStages = listOf(
        RoadmapStage(
            stageNumber = 1,
            name = "Exploring & Research",
            status = "COMPLETED",
            description = "Comparing immigration routes, work permits, and living costs.",
            tasks = listOf(
                RoadmapTask("r1", "Research Swedish Sambo residence visa rules", true, "Ayaan"),
                RoadmapTask("r2", "Compare tech design job market in Gothenburg", true, "Linnea"),
                RoadmapTask("r3", "Calculate initial transition budget & safety net", true, "Both")
            )
        ),
        RoadmapStage(
            stageNumber = 2,
            name = "Decided & Committed",
            status = "IN_PROGRESS",
            description = "Target city locked: Gothenburg, Sweden. Target window: Autumn 2027.",
            tasks = listOf(
                RoadmapTask("r4", "Gather relationship proof (boarding passes, letters, photos)", true, "Both"),
                RoadmapTask("r5", "Apostille degree certificates & tax transcripts", false, "Ayaan"),
                RoadmapTask("r6", "Swedish language basics (Duolingo / SFI prep)", false, "Ayaan"),
                RoadmapTask("r7", "Apartment lease extension review in Gothenburg", true, "Linnea")
            )
        ),
        RoadmapStage(
            stageNumber = 3,
            name = "In Progress",
            status = "UPCOMING",
            description = "Submitting official applications and preparing physical relocations.",
            tasks = listOf(
                RoadmapTask("r8", "Submit Migrationsverket cohabitation visa application", false, "Both"),
                RoadmapTask("r9", "Attend embassy biometric interview in Mumbai", false, "Ayaan"),
                RoadmapTask("r10", "Sell solo furniture and downsize luggage to two suitcases", false, "Ayaan")
            )
        ),
        RoadmapStage(
            stageNumber = 4,
            name = "Closing the Distance",
            status = "UPCOMING",
            description = "The one-way flight, key pickup, and the start of daily life together.",
            tasks = listOf(
                RoadmapTask("r11", "Book one-way flight BOM -> GOT", false, "Both"),
                RoadmapTask("r12", "Welcome dinner at Linnegatan with friends", false, "Linnea"),
                RoadmapTask("r13", "Hang yellow door numbers and celebrate closure of distance", false, "Both")
            )
        )
    )

    val decisionTopics = listOf(
        DecisionTopic(
            id = "dec_1",
            title = "Which Gothenburg neighborhood to start in? (Majorna vs Linne)",
            prosAyaan = listOf("Majorna has character, quiet cafes, and great bakeries", "More affordable square meters for an art desk"),
            prosLinnea = listOf("Linne is walking distance to Slottsskogen and my studio", "Easier tram connections when winter weather hits"),
            feelingsAyaan = 85,
            feelingsLinnea = 90
        )
    )

    val packingItems = listOf(
        PackingItem("p1", "Indian spice blend from Pune market", "Ayaan", false),
        PackingItem("p2", "Extra warm wool mittens for Sweden winter", "Linnea", true),
        PackingItem("p3", "Universal power adapter & backup battery", "Ayaan", true),
        PackingItem("p4", "Favorite Gothenburg bakery cardamom buns", "Linnea", false),
        PackingItem("p5", "Hard drive with archived photos and videos", "Ayaan", true)
    )

    val itineraryItems = listOf(
        ItineraryItem("i1", "Sunset walk along Shaniwar Wada & old Pune alleys", "Must Do", false),
        ItineraryItem("i2", "Cook mom's Poha breakfast together on Saturday", "Must Do", false),
        ItineraryItem("i3", "Day trip to Sinhagad Fort for mountain breeze", "Nice to Have", false),
        ItineraryItem("i4", "Indoor chai cafe & book reading if it rains", "Rain Backup", false)
    )
}
