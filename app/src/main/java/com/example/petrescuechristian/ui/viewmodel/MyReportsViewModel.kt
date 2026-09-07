package com.example.petrescuechristian.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petrescuechristian.domain.model.PetReport
import com.example.petrescuechristian.domain.repository.PetReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class MyReportsUiState(
    val reports: List<PetReport> = emptyList(),
    val isLoading: Boolean = true,
    val error: String? = null
)

class MyReportsViewModel(
    private val petReportRepository: PetReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(MyReportsUiState())
    val uiState: StateFlow<MyReportsUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { petReportRepository.getAllReports() }
                .onSuccess { reports -> _uiState.update { it.copy(reports = reports, isLoading = false) } }
                .onFailure { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
        }
    }
}
