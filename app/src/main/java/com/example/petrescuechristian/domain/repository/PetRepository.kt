package com.example.petrescuechristian.domain.repository

import com.example.petrescuechristian.domain.model.Pet

interface PetRepository {
    suspend fun getAllPets(): List<Pet>
    suspend fun getPetById(id: Int): Pet?
}
