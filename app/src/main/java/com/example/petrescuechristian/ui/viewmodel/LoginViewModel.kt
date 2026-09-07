package com.example.petrescuechristian.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petrescuechristian.data.SessionManager
import com.example.petrescuechristian.domain.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LoginUiState(
    val identifier: String = "",
    val password: String = "",
    val passwordVisible: Boolean = false,
    val identifierError: String? = null,
    val passwordError: String? = null,
    val loginError: String? = null,
    val isLoading: Boolean = false,
    val loginSuccess: Boolean = false
)

class LoginViewModel(
    private val authRepository: AuthRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onIdentifierChange(value: String) {
        _uiState.update { it.copy(identifier = value, identifierError = null, loginError = null) }
    }

    fun onPasswordChange(value: String) {
        _uiState.update { it.copy(password = value, passwordError = null, loginError = null) }
    }

    fun onTogglePasswordVisibility() {
        _uiState.update { it.copy(passwordVisible = !it.passwordVisible) }
    }

    fun login() {
        val state = _uiState.value
        val identifierError = if (state.identifier.isBlank()) "Ingresa tu correo o usuario" else null
        val passwordError = when {
            state.password.isBlank() -> "Ingresa tu contraseña"
            state.password.length < 6 -> "Debe tener al menos 6 caracteres"
            else -> null
        }

        _uiState.update {
            it.copy(identifierError = identifierError, passwordError = passwordError, loginError = null)
        }
        if (identifierError != null || passwordError != null) return

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true) }
            runCatching { authRepository.login(state.identifier, state.password) }
                .onSuccess { user ->
                    if (user != null) {
                        SessionManager.login(user)
                        _uiState.update { it.copy(isLoading = false, loginSuccess = true) }
                    } else {
                        _uiState.update {
                            it.copy(isLoading = false, loginError = "Usuario o contraseña incorrectos")
                        }
                    }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isLoading = false, loginError = "No se pudo conectar a la base de datos: ${error.message}")
                    }
                }
        }
    }

    fun consumeLoginSuccess() {
        _uiState.update { it.copy(loginSuccess = false) }
    }
}
