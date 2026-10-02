package com.example.meridian.data.dates

data class DateIdea(
    val id: String,
    val title: String,
    val description: String,
    val energy: String, // "Low", "Medium", "High"
    val cost: String,   // "Free", "Low Cost"
    val timeRequired: String, // "15 min", "1 hour", "Evening"
    val asyncFriendly: Boolean = false
)

object DateIdeasData {
    val ideas: List<DateIdea> = listOf(
        DateIdea("d1", "Cook the Same Recipe", "Pick a recipe neither has cooked before. Set your phones up on the cutting boards and eat together on speaker.", "Medium", "Low Cost", "Evening"),
        DateIdea("d2", "Sleep Story Chapter", "One person reads a chapter of a soothing book out loud until the other falls asleep.", "Low", "Free", "1 hour", true),
        DateIdea("d3", "Street View Walking Tour", "Open Google Street View on a city neither has visited (e.g., Kyoto or Porto) and walk down alleys together.", "Low", "Free", "1 hour"),
        DateIdea("d4", "3-Minute Blind Portrait", "Draw each other in 3 minutes without looking down at the paper. Hold them up to the camera simultaneously.", "Low", "Free", "15 min"),
        DateIdea("d5", "Supermarket Scavenger Hunt", "While on a call at your respective local grocery stores, find the weirdest snack and trade photos.", "Medium", "Low Cost", "1 hour"),
        DateIdea("d6", "Silent Coworking Fika", "Stay on a video call on mute with lofi music while working or studying. Wave whenever you take a coffee sip.", "Low", "Free", "Evening", true),
        DateIdea("d7", "Synchronized Movie Night", "Pick a movie, grab popcorn, use Press Play Together on 3-2-1, and leave a voice call open for reactions.", "Low", "Low Cost", "Evening"),
        DateIdea("d8", "House Tour of Childhood", "Pull up satellite imagery of your childhood neighborhood and show each other your walk to school.", "Medium", "Free", "1 hour"),
        DateIdea("d9", "Shared Playlist Listening Party", "Put together a 10-song playlist of tracks that remind you of specific moments, and listen in order.", "Low", "Free", "1 hour", true),
        DateIdea("d10", "Online Apartment Hunting", "Browse rental listings in our target future city. Pick the apartment with the best kitchen and argue lovingly over layouts.", "Medium", "Free", "1 hour"),
        DateIdea("d11", "Taste Test Swap", "Mail each other a box of 3 mystery snacks from your local convenience store. Open and taste them on camera.", "High", "Low Cost", "1 hour"),
        DateIdea("d12", "Virtual Museum Walk", "Tour the Louvre, Rijksmuseum, or British Museum via virtual 360 interactive exhibits.", "Low", "Free", "1 hour"),
        DateIdea("d13", "Two-Person Trivia Night", "Pick a category on a free trivia app or make 10 personalized quiz questions about each other's favorites.", "Medium", "Free", "1 hour"),
        DateIdea("d14", "Candlelit Evening Call", "Turn off all overhead apartment lights, light a candle on each end, and have a quiet wine or tea talk.", "Low", "Free", "1 hour"),
        DateIdea("d15", "Recreate Our First Date", "Order or cook what you ate on your very first date together, dress up, and pretend you're meeting for the first time.", "High", "Low Cost", "Evening"),
        DateIdea("d16", "Draw Our Future Living Room", "Sketch out on paper what you want our shared living room to look like. Compare colors and couch placements.", "Medium", "Free", "1 hour"),
        DateIdea("d17", "Soundscape Meditation", "Turn on Pune Rain or Gothenburg Harbor in Meridian, close your eyes, and breathe in sync on speaker.", "Low", "Free", "15 min"),
        DateIdea("d18", "Wikipedia Game Race", "Start on a random Wikipedia page and race to reach 'Gothenburg' or 'Pune' using only hyperlinked text.", "Medium", "Free", "15 min"),
        DateIdea("d19", "Voice Note Podcast", "Record a 5-minute 'podcast' about the weirdest thing that happened today and send it for the morning commute.", "Low", "Free", "15 min", true),
        DateIdea("d20", "Travel Itinerary Deep Dive", "Plan every hour of day 1 of our next visit, down to the exact bakery for morning buns.", "Medium", "Free", "1 hour"),
        DateIdea("d21", "Lego / Puzzle Sprint", "Buy matching mini puzzle sets or miniature Lego kits and race to assemble them on camera.", "High", "Low Cost", "1 hour"),
        DateIdea("d22", "Late-Night Q&A in the Dark", "Turn all room lights off so only screen glows softly, and pull 5 cards from the Deep deck.", "Low", "Free", "1 hour"),
        DateIdea("d23", "Swap Desktop Backgrounds", "Design a custom phone or laptop wallpaper for each other using inside jokes, and set it live.", "Medium", "Free", "1 hour", true),
        DateIdea("d24", "Stargazing on Speaker", "Step out onto your balcony or open a window. Point out what the sky looks like from your coordinate.", "Low", "Free", "15 min"),
        DateIdea("d25", "Bake the Same Cookies", "Measure flour, whisk eggs, and pull warm chocolate chip cookies out of the oven at the same minute.", "High", "Low Cost", "Evening"),
        DateIdea("d26", "Photo Roll Nostalgia Dive", "Open your camera roll from 3 years ago and explain the story behind three random photos.", "Low", "Free", "1 hour"),
        DateIdea("d27", "Write a Joint Letter to 2030", "Take turns writing alternate sentences in an Open When letter scheduled for 5 years into the future.", "Medium", "Free", "1 hour"),
        DateIdea("d28", "Foreign Language Mini Lesson", "Teach each other 5 colloquial slang phrases in Marathi or Swedish.", "Low", "Free", "15 min"),
        DateIdea("d29", "Sunrise & Sunset Handshake", "Catch Linnea's sunset while Ayaan wakes up for sunrise on video call.", "Medium", "Free", "15 min"),
        DateIdea("d30", "Rapid-Fire This or That", "30 rapid questions: coffee or tea, window or aisle, morning or night, beach or mountains.", "Low", "Free", "15 min"),
        DateIdea("d31", "Paper Airplane Distance Challenge", "Fold paper airplanes on camera, throw them toward your screens, and see whose design flies further.", "Low", "Free", "15 min"),
        DateIdea("d32", "Secret Comfort Delivery", "Use a delivery app to order a warm drink or dessert to each other's address without revealing what it is.", "High", "Low Cost", "1 hour"),
        DateIdea("d33", "Book Club of One Chapter", "Agree to read just 20 pages of an article or essay and discuss over evening tea.", "Medium", "Free", "1 hour", true),
        DateIdea("d34", "Closet Fashion Show", "Put together the most ridiculous or stylish outfits from your closet and strut across your room on video.", "High", "Free", "1 hour"),
        DateIdea("d35", "Origami Tutorial Together", "Follow a beginner origami YouTube video and see whose paper crane actually stands upright.", "Medium", "Free", "1 hour"),
        DateIdea("d36", "Map Out Next Year's Holidays", "Open a blank calendar and mark out every long weekend where flights could work.", "Medium", "Free", "1 hour"),
        DateIdea("d37", "Worst Advice Game", "Ask each other for real dilemmas and give the most wildly terrible advice possible with a straight face.", "Low", "Free", "15 min"),
        DateIdea("d38", "Co-op Wordle Duel", "Solve the daily Wordle together by trading guesses and brainstorming letter placements.", "Low", "Free", "15 min"),
        DateIdea("d39", "Dream House Zillow Tour", "Look at multimillion-dollar houses with indoor pools and decide who gets which wing.", "Low", "Free", "1 hour"),
        DateIdea("d40", "Gratitude Jar Recording", "Spend 10 minutes listing 5 small things about each other you're grateful for right this minute.", "Low", "Free", "15 min")
    )
}
