package com.example.meridian.data.questions

import com.example.meridian.data.model.DailyQuestion

/**
 * 80 Original, calibrated, specific questions for Meridian.
 * 20 Light, 20 Deep, 20 Future, 20 Silly.
 * Designed to spark sensory memories, gentle laughter, and intimate reflection across time zones.
 */
object QuestionsData {

    val allQuestions: List<DailyQuestion> = listOf(
        // === LIGHT DECK (20) ===
        DailyQuestion("l1", "light", "What's the most ordinary thing near you right now that you wish I could see?"),
        DailyQuestion("l2", "light", "What sound or smell instantly teleports you back to our last visit?"),
        DailyQuestion("l3", "light", "What did you eat today that you wish you could share across the table with me?"),
        DailyQuestion("l4", "light", "What is one small song you have had stuck in your head lately?"),
        DailyQuestion("l5", "light", "If you had an unexpected free hour this afternoon, what would you spend it doing?"),
        DailyQuestion("l6", "light", "What's the most comforting corner of your apartment right now?"),
        DailyQuestion("l7", "light", "What was the weather doing outside your window when you first woke up today?"),
        DailyQuestion("l8", "light", "What piece of clothing of mine do you secretly want to borrow and keep?"),
        DailyQuestion("l9", "light", "What is the funniest minor inconvenience you dealt with this week?"),
        DailyQuestion("l10", "light", "If we were grabbing breakfast together tomorrow morning, what's your exact coffee/tea order?"),
        DailyQuestion("l11", "light", "What is a small habit of mine that always makes you smile when you remember it?"),
        DailyQuestion("l12", "light", "What's a show or video that felt like a warm blanket when you watched it?"),
        DailyQuestion("l13", "light", "What is the silliest inside joke we've developed over text?"),
        DailyQuestion("l14", "light", "If you could send a tiny package right now with three things, what would be in it?"),
        DailyQuestion("l15", "light", "What is a street or park in your city that you can't wait to walk down with me?"),
        DailyQuestion("l16", "light", "What time of day do you usually feel most awake and chatty?"),
        DailyQuestion("l17", "light", "What's one thing you bought recently that was worth every cent?"),
        DailyQuestion("l18", "light", "If we were stuck at an airport for 4 hours, what game would we play?"),
        DailyQuestion("l19", "light", "What is your favorite picture of us that isn't posed or polished?"),
        DailyQuestion("l20", "light", "What is the very first thing we should do the hour after we land on our next visit?"),

        // === DEEP DECK (20) ===
        DailyQuestion("d1", "deep", "When did you last feel completely understood, and what made you feel that way?"),
        DailyQuestion("d2", "deep", "What is one fear about our future that feels softer when we talk about it honestly?"),
        DailyQuestion("d3", "deep", "In what way has loving across distance changed how you love yourself?"),
        DailyQuestion("d4", "deep", "What part of your childhood do you feel explains how you handle stress today?"),
        DailyQuestion("d5", "deep", "When was a moment during one of our goodbyes where you felt our strength most clearly?"),
        DailyQuestion("d6", "deep", "What is something you're proud of this year that you haven't given yourself enough credit for?"),
        DailyQuestion("d7", "deep", "How do you feel your communication style has evolved since we started long distance?"),
        DailyQuestion("d8", "deep", "What is a quiet insecurity you rarely say out loud?"),
        DailyQuestion("d9", "deep", "What does 'home' mean to you today, and how has that definition shifted?"),
        DailyQuestion("d10", "deep", "When did you first realize this relationship was worth the time zones and flights?"),
        DailyQuestion("d11", "deep", "What is a boundary you've set recently that helped you protect your peace?"),
        DailyQuestion("d12", "deep", "If you could ask me anything without worrying about my reaction, what would it be?"),
        DailyQuestion("d13", "deep", "What emotion is hardest for you to express when we are on a voice call?"),
        DailyQuestion("d14", "deep", "What is something I did that made you feel deeply respected and safe?"),
        DailyQuestion("d15", "deep", "How can I best support you when you're having an overwhelmed, quiet day?"),
        DailyQuestion("d16", "deep", "What is one dream you had as a teenager that still quietly lives in you?"),
        DailyQuestion("d17", "deep", "What has been the most difficult lesson of your twenties so far?"),
        DailyQuestion("d18", "deep", "What is something tender about our relationship that you protect fiercely?"),
        DailyQuestion("d19", "deep", "When you think about closing the distance, what feeling comes up first: relief or nervous excitement?"),
        DailyQuestion("d20", "deep", "What is one promise you want us to always keep to each other, no matter what?"),

        // === FUTURE DECK (20) ===
        DailyQuestion("f1", "future", "Describe a quiet Tuesday evening in our first month living in the same city."),
        DailyQuestion("f2", "future", "What is the very first piece of shared furniture we should pick out together?"),
        DailyQuestion("f3", "future", "Who will cook more, who will do dishes, and what's our diplomatic compromise?"),
        DailyQuestion("f4", "future", "What kind of neighborhood vibe do you want us to wake up in?"),
        DailyQuestion("f5", "future", "How do you imagine our weekend grocery runs when we no longer have to pack a suitcase?"),
        DailyQuestion("f6", "future", "What is one tradition from your culture or upbringing you definitely want in our home?"),
        DailyQuestion("f7", "future", "What will our morning routine look like when our alarm clocks go off in the same room?"),
        DailyQuestion("f8", "future", "If we host our first dinner party for friends, what dish are we making?"),
        DailyQuestion("f9", "future", "What is a trip we want to take once closing the distance is finalized and behind us?"),
        DailyQuestion("f10", "future", "What will we do with all the old boarding passes and train tickets we saved?"),
        DailyQuestion("f11", "future", "What's a lazy Sunday habit you want us to protect fiercely in our future home?"),
        DailyQuestion("f12", "future", "If our future home has a balcony or window sill, what are we growing on it?"),
        DailyQuestion("f13", "future", "What is a small ritual we should start on day one of having our own keys?"),
        DailyQuestion("f14", "future", "How do you think our dog or cat (if we get one!) will react to our daily antics?"),
        DailyQuestion("f15", "future", "What's the first holiday season in the same city going to feel like?"),
        DailyQuestion("f16", "future", "What is an argument we have now across distance that will instantly vanish when we live together?"),
        DailyQuestion("f17", "future", "What new hobby do you want us to learn together once we have free shared evenings?"),
        DailyQuestion("f18", "future", "How will we celebrate the exact one-year anniversary of closing the gap?"),
        DailyQuestion("f19", "future", "What is one thing about your solo routine now that you'll happily give up to live together?"),
        DailyQuestion("f20", "future", "In ten years, what will make us look back on this long-distance chapter and smile?"),

        // === SILLY DECK (20) ===
        DailyQuestion("s1", "silly", "If our relationship were a sitcom, what would be the recurring running joke?"),
        DailyQuestion("s2", "silly", "Which one of us would survive longer in a mild zombie apocalypse, and why?"),
        DailyQuestion("s3", "silly", "What is the most ridiculous thing you have ever researched at 2am when you couldn't sleep?"),
        DailyQuestion("s4", "silly", "If you had to describe my personality using three random kitchen utensils, what are they?"),
        DailyQuestion("s5", "silly", "What conspiracy theory is so funny or harmless that you want it to be true?"),
        DailyQuestion("s6", "silly", "If we entered a talent show with 24 hours to prepare, what would our act be?"),
        DailyQuestion("s7", "silly", "What is the worst haircut or outfit choice you made in middle school?"),
        DailyQuestion("s8", "silly", "If our text history was published as a novel, what would the dramatic title be?"),
        DailyQuestion("s9", "silly", "Which animal best captures the energy of me when I haven't had breakfast?"),
        DailyQuestion("s10", "silly", "What is the most bizarre food combination you secretly enjoy when no one is watching?"),
        DailyQuestion("s11", "silly", "If we were arrested with no explanation, what would our friends assume we did?"),
        DailyQuestion("s12", "silly", "What is the weirdest habit you've caught yourself doing when completely home alone?"),
        DailyQuestion("s13", "silly", "If you had to swap voices with any cartoon character for 24 hours, who are you picking?"),
        DailyQuestion("s14", "silly", "What is the dumbest argument we've had that ended with both of us laughing?"),
        DailyQuestion("s15", "silly", "If you could ban one phrase from airport security announcements, what would it be?"),
        DailyQuestion("s16", "silly", "What is your signature dance move when our favorite song comes on unexpectedly?"),
        DailyQuestion("s17", "silly", "If we opened a cafe together, what would be the weird specialty drink named after you?"),
        DailyQuestion("s18", "silly", "What would be your superhero origin story, and what trivial power do you get?"),
        DailyQuestion("s19", "silly", "Who takes longer to pick what to watch, and how many trailers do we suffer through?"),
        DailyQuestion("s20", "silly", "If you had to communicate with me for an entire day using only dog barks and nods, how would it go?")
    )

    fun getQuestionsByDeck(deck: String): List<DailyQuestion> {
        return allQuestions.filter { it.deck.equals(deck, ignoreCase = true) }
    }
}
