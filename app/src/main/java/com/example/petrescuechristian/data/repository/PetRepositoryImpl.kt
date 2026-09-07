package com.example.petrescuechristian.data.repository

import com.example.petrescuechristian.data.local.PetLocalDataSource
import com.example.petrescuechristian.data.mapper.toDomain
import com.example.petrescuechristian.domain.model.Pet
import com.example.petrescuechristian.domain.repository.PetRepository

class PetRepositoryImpl(
    private val dataSource: PetLocalDataSource
) : PetRepository {

    override suspend fun getAllPets(): List<Pet> =
        dataSource.getAllPets().map { it.toDomain() }

    override suspend fun getPetById(id: Int): Pet? =
        dataSource.getPetById(id)?.toDomain()
}
