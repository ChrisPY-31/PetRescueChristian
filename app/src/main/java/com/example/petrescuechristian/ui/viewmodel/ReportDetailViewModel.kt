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

data class ReportDetailUiState(
    val report: PetReport? = null,
    val isLoading: Boolean = true,
    val error: String? = null
)

class ReportDetailViewModel(
    private val petReportRepository: PetReportRepository,
    private val reportId: Int
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportDetailUiState())
    val uiState: StateFlow<ReportDetailUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { petReportRepository.getReportById(reportId) }
                .onSuccess { report -> _uiState.update { it.copy(report = report, isLoading = false) } }
                .onFailure { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
        }
    }
}
