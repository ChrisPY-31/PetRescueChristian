package com.example.petrescuechristian.data

import com.example.petrescuechristian.model.User

/**
 * Autenticación simulada en memoria (sin backend). Expone un único usuario de prueba.
 */
object AuthRepository {

    const val TEST_USERNAME = "christian"
    const val TEST_PASSWORD = "12345678"

    private val testUser = User(
        id = 1,
        name = "Christian",
        email = "christian@rescuechristian.com"
    )

    private val validIdentifiers = listOf(TEST_USERNAME, testUser.email)

    fun login(identifier: String, password: String): User? {
        val normalizedIdentifier = identifier.trim()
        val matchesIdentifier = validIdentifiers.any { it.equals(normalizedIdentifier, ignoreCase = true) }
        return if (matchesIdentifier && password == TEST_PASSWORD) testUser else null
    }
}
