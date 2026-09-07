package com.example.petrescuechristian.data.repository

import com.example.petrescuechristian.data.local.PetReportLocalDataSource
import com.example.petrescuechristian.data.local.entity.PetReportEntity
import com.example.petrescuechristian.data.mapper.toDomain
import com.example.petrescuechristian.domain.model.PetReport
import com.example.petrescuechristian.domain.repository.PetReportRepository

class PetReportRepositoryImpl(
    private val dataSource: PetReportLocalDataSource
) : PetReportRepository {

    override suspend fun getAllReports(): List<PetReport> =
        dataSource.getAllReports().map { it.toDomain() }

    override suspend fun getReportById(id: Int): PetReport? =
        dataSource.getReportById(id)?.toDomain()

    override suspend fun createReport(
        species: String,
        description: String,
        latitude: Double,
        longitude: Double,
        imageUri: String?,
        date: String,
        status: String
    ): PetReport {
        val entity = PetReportEntity(
            id = 0,
            species = species,
            description = description,
            latitude = latitude,
            longitude = longitude,
            imageUri = imageUri,
            reportDate = date,
            status = status
        )
        return dataSource.insertReport(entity).toDomain()
    }
}
