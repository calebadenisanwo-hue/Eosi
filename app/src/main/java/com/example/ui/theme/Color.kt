package com.example.ui.theme

import androidx.compose.ui.graphics.Color

// Meridian Palette Design Tokens
val MeridianOffWhite = Color(0xFFFBF7F2)
val MeridianDeepNight = Color(0xFF14152B)
val MeridianSurfaceDark = Color(0xFF1D1F38)
val MeridianSurfaceLight = Color(0xFFFFFFFF)
val MeridianApricot = Color(0xFFE9946B)
val MeridianApricotSoft = Color(0xFFFFD8C6)
val MeridianDuskBlue = Color(0xFF5A6BA8)
val MeridianTextPrimaryLight = Color(0xFF232533)
val MeridianTextSecondaryLight = Color(0xFF6B6E82)
val MeridianTextPrimaryDark = Color(0xFFF4F4F8)
val MeridianTextSecondaryDark = Color(0xFFA6A8BE)
val MeridianCardLight = Color(0xFFFFFFFF)
val MeridianCardDark = Color(0xFF222440)

// Sky Palette Stops
object MeridianSkyPalettes {
    val Night = listOf(Color(0xFF12142E), Color(0xFF0B0D22))
    val Dawn = listOf(Color(0xFF3A4470), Color(0xFFE8A6A1), Color(0xFFF6C393))
    val Morning = listOf(Color(0xFFF8E3B0), Color(0xFFBFE0F0))
    val Midday = listOf(Color(0xFF8EC5E8), Color(0xFFD7EEF8))
    val GoldenHour = listOf(Color(0xFFF4B860), Color(0xFFF7D1A8))
    val Dusk = listOf(Color(0xFF9B4F7D), Color(0xFF2F2A5F))
    val Evening = listOf(Color(0xFF2F2A5F), Color(0xFF171936))

    fun getSkyGradient(hour24: Int): List<Color> {
        return when (hour24) {
            in 0..4 -> Night
            in 5..6 -> Dawn
            in 7..10 -> Morning
            in 11..15 -> Midday
            in 16..18 -> GoldenHour
            in 19..20 -> Dusk
            in 21..23 -> Evening
            else -> Night
        }
    }
}
