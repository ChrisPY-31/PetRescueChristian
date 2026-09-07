package com.example.petrescuechristian.data.mapper

import com.example.petrescuechristian.data.local.entity.PetEntity
import com.example.petrescuechristian.domain.model.Pet

fun PetEntity.toDomain(): Pet = Pet(
    id = id,
    name = name,
    species = species,
    breed = breed,
    age = age,
    size = size,
    description = description,
    imageUri = imageUri,
    latitude = latitude,
    longitude = longitude,
    available = available
)
