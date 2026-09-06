package com.example.petrescuechristian.ui.components

import androidx.compose.ui.graphics.Color

fun emojiForSpecies(species: String): String = when (species.lowercase()) {
    "perro" -> "🐶"
    "gato" -> "🐱"
    "ave" -> "🐦"
    "conejo" -> "🐰"
    else -> "🐾"
}

fun colorForSpecies(species: String): Color = when (species.lowercase()) {
    "perro" -> Color(0xFFFFE0B2)
    "gato" -> Color(0xFFE1BEE7)
    "ave" -> Color(0xFFB3E5FC)
    "conejo" -> Color(0xFFF8BBD0)
    else -> Color(0xFFC8E6C9)
}
