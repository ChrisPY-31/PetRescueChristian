package com.example.petrescuechristian.di

import com.example.petrescuechristian.data.local.PetLocalDataSource
import com.example.petrescuechristian.data.local.PetReportLocalDataSource
import com.example.petrescuechristian.data.local.UserLocalDataSource
import com.example.petrescuechristian.data.repository.AuthRepositoryImpl
import com.example.petrescuechristian.data.repository.PetRepositoryImpl
import com.example.petrescuechristian.data.repository.PetReportRepositoryImpl
import com.example.petrescuechristian.domain.repository.AuthRepository
import com.example.petrescuechristian.domain.repository.PetRepository
import com.example.petrescuechristian.domain.repository.PetReportRepository

/**
 * ServiceLocator manual: no se usa Hilt/Koin en este proyecto. Provee una única
 * instancia de cada repositorio, respaldada por la BD real (PostgreSQL vía JDBC).
 */
object AppContainer {
    val petRepository: PetRepository = PetRepositoryImpl(PetLocalDataSource())
    val petReportRepository: PetReportRepository = PetReportRepositoryImpl(PetReportLocalDataSource())
    val authRepository: AuthRepository = AuthRepositoryImpl(UserLocalDataSource())
}
