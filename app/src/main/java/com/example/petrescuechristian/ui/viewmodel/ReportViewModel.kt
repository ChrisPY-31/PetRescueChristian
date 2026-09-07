package com.example.petrescuechristian.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petrescuechristian.domain.repository.PetReportRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class ReportUiState(
    val animalType: String? = null,
    val description: String = "",
    val photoPath: String? = null,
    val animalTypeError: String? = null,
    val descriptionError: String? = null,
    val photoError: String? = null,
    val locationError: String? = null,
    val isSubmitting: Boolean = false,
    val createdReportId: Int? = null
)

class ReportViewModel(
    private val petReportRepository: PetReportRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReportUiState())
    val uiState: StateFlow<ReportUiState> = _uiState.asStateFlow()

    fun onAnimalTypeChange(type: String) {
        _uiState.update { it.copy(animalType = type, animalTypeError = null) }
    }

    fun onDescriptionChange(value: String) {
        _uiState.update { it.copy(description = value, descriptionError = null) }
    }

    fun onPhotoChange(path: String?) {
        _uiState.update { it.copy(photoPath = path, photoError = if (path != null) null else it.photoError) }
    }

    fun onPhotoError(message: String) {
        _uiState.update { it.copy(photoError = message) }
    }

    fun submit(latitude: Double?, longitude: Double?) {
        val state = _uiState.value
        val animalTypeError = if (state.animalType == null) "Selecciona el tipo de animal" else null
        val descriptionError = if (state.description.isBlank()) "Describe a la mascota encontrada" else null
        val photoError = if (state.photoPath == null) "Toma una fotografía de la mascota" else null
        val locationError = if (latitude == null || longitude == null) {
            "No se pudo obtener tu ubicación. Reintenta antes de generar el reporte."
        } else {
            null
        }

        _uiState.update {
            it.copy(
                animalTypeError = animalTypeError,
                descriptionError = descriptionError,
                photoError = photoError,
                locationError = locationError
            )
        }
        if (animalTypeError != null || descriptionError != null || photoError != null || locationError != null) {
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isSubmitting = true) }
            runCatching {
                petReportRepository.createReport(
                    species = state.animalType!!,
                    description = state.description.trim(),
                    latitude = latitude!!,
                    longitude = longitude!!,
                    imageUri = state.photoPath,
                    date = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()),
                    status = "Pendiente de revisión"
                )
            }
                .onSuccess { report ->
                    _uiState.update { it.copy(isSubmitting = false, createdReportId = report.id) }
                }
                .onFailure { error ->
                    _uiState.update {
                        it.copy(isSubmitting = false, locationError = "No se pudo guardar el reporte: ${error.message}")
                    }
                }
        }
    }
}
