package com.example.petrescuechristian.data

import com.example.petrescuechristian.model.Pet

enum class SpeciesFilter(val label: String, val emoji: String) {
    ALL("Todos", "🐾"),
    DOG("Perros", "🐶"),
    CAT("Gatos", "🐱"),
    OTHER("Otros", "🐦");

    fun matches(pet: Pet): Boolean = when (this) {
        ALL -> true
        DOG -> pet.species.equals("Perro", ignoreCase = true)
        CAT -> pet.species.equals("Gato", ignoreCase = true)
        OTHER -> !pet.species.equals("Perro", ignoreCase = true) &&
            !pet.species.equals("Gato", ignoreCase = true)
    }
}
