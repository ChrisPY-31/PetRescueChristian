package com.example.petrescuechristian.data

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.example.petrescuechristian.model.User

/**
 * Mantiene la sesión del usuario actual en memoria (sin persistencia ni backend).
 */
object SessionManager {
    var currentUser by mutableStateOf<User?>(null)
        private set

    fun login(user: User) {
        currentUser = user
    }

    fun logout() {
        currentUser = null
    }
}
