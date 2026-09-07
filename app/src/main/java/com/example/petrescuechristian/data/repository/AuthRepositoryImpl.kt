package com.example.petrescuechristian.data.repository

import com.example.petrescuechristian.data.local.UserLocalDataSource
import com.example.petrescuechristian.data.mapper.toDomain
import com.example.petrescuechristian.domain.model.User
import com.example.petrescuechristian.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val dataSource: UserLocalDataSource
) : AuthRepository {

    override suspend fun login(identifier: String, password: String): User? {
        val entity = dataSource.findByIdentifier(identifier.trim()) ?: return null
        return if (entity.password == password) entity.toDomain() else null
    }
}
