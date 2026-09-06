package com.example.petrescuechristian.data

import com.example.petrescuechristian.model.Pet

/**
 * Mascotas disponibles para adopción, definidas en memoria (sin backend ni base de datos).
 */
object PetData {

    val pets: List<Pet> = listOf(
        Pet(
            id = 1,
            name = "Max",
            species = "Perro",
            breed = "Labrador",
            age = 2,
            size = "Grande",
            description = "Max es un labrador muy juguetón y cariñoso, ideal para familias con niños.",
            imageUri = null,
            latitude = 19.1809,
            longitude = -99.4667,
            available = true
        ),
        Pet(
            id = 2,
            name = "Luna",
            species = "Gato",
            breed = "Siamés",
            age = 1,
            size = "Pequeño",
            description = "Luna es curiosa y tranquila, se lleva bien con otros gatos.",
            imageUri = null,
            latitude = 19.1860,
            longitude = -99.4701,
            available = true
        ),
        Pet(
            id = 3,
            name = "Rocky",
            species = "Perro",
            breed = "Mestizo",
            age = 3,
            size = "Mediano",
            description = "Rocky fue rescatado de la calle, es leal y muy activo.",
            imageUri = null,
            latitude = 19.1724,
            longitude = -99.4590,
            available = true
        ),
        Pet(
            id = 4,
            name = "Kiwi",
            species = "Ave",
            breed = "Periquito",
            age = 1,
            size = "Pequeño",
            description = "Kiwi canta todas las mañanas y le encanta la compañía.",
            imageUri = null,
            latitude = 19.1882,
            longitude = -99.4812,
            available = true
        ),
        Pet(
            id = 5,
            name = "Milo",
            species = "Gato",
            breed = "Persa",
            age = 4,
            size = "Mediano",
            description = "Milo ya fue adoptado y tiene un nuevo hogar feliz.",
            imageUri = null,
            latitude = 19.1966,
            longitude = -99.4739,
            available = false
        ),
        Pet(
            id = 6,
            name = "Toby",
            species = "Perro",
            breed = "Beagle",
            age = 2,
            size = "Mediano",
            description = "Toby es muy sociable y le encanta pasear.",
            imageUri = null,
            latitude = 19.1638,
            longitude = -99.4539,
            available = true
        ),
        Pet(
            id = 7,
            name = "Nube",
            species = "Conejo",
            breed = "Holandés",
            age = 1,
            size = "Pequeño",
            description = "Nube es tranquila y dócil, perfecta para departamentos.",
            imageUri = null,
            latitude = 19.2016,
            longitude = -99.4889,
            available = true
        ),
        Pet(
            id = 8,
            name = "Simón",
            species = "Perro",
            breed = "Pastor Alemán",
            age = 5,
            size = "Grande",
            description = "Simón ya encontró un hogar responsable.",
            imageUri = null,
            latitude = 19.1596,
            longitude = -99.4869,
            available = false
        )
    )

    fun findById(id: Int): Pet? = pets.firstOrNull { it.id == id }
}
