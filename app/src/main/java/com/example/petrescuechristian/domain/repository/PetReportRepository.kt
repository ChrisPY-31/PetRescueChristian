package com.example.petrescuechristian.domain.repository

import com.example.petrescuechristian.domain.model.PetReport

interface PetReportRepository {
    suspend fun getAllReports(): List<PetReport>
    suspend fun getReportById(id: Int): PetReport?

    /**
     * El id lo asigna la base de datos (columna autogenerada) al insertar,
     * por eso no se recibe aquí — se devuelve en el PetReport resultante.
     */
    suspend fun createReport(
        species: String,
        description: String,
        latitude: Double,
        longitude: Double,
        imageUri: String?,
        date: String,
        status: String
    ): PetReport
}
