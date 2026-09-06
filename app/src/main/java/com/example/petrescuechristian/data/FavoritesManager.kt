package com.example.petrescuechristian.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Favoritos manejados en memoria (no se persisten entre sesiones de la app).
 */
object FavoritesManager {
    var favoriteIds by mutableStateOf<Set<Int>>(emptySet())
        private set

    fun isFavorite(petId: Int): Boolean = favoriteIds.contains(petId)

    fun toggleFavorite(petId: Int) {
        favoriteIds = if (favoriteIds.contains(petId)) {
            favoriteIds - petId
        } else {
            favoriteIds + petId
        }
    }
}
