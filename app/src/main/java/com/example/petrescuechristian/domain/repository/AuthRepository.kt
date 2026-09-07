package com.example.petrescuechristian.domain.repository

import com.example.petrescuechristian.domain.model.User

interface AuthRepository {
    suspend fun login(identifier: String, password: String): User?
}
