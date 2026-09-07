package com.example.petrescuechristian.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petrescuechristian.data.SpeciesFilter
import com.example.petrescuechristian.domain.model.Pet
import com.example.petrescuechristian.domain.repository.PetRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CatalogUiState(
    val allPets: List<Pet> = emptyList(),
    val searchQuery: String = "",
    val selectedSpecies: SpeciesFilter = SpeciesFilter.ALL,
    val selectedSizes: Set<String> = emptySet(),
    val isLoading: Boolean = true,
    val error: String? = null
) {
    val filteredPets: List<Pet>
        get() = allPets.filter { pet ->
            val matchesQuery = searchQuery.isBlank() ||
                pet.name.contains(searchQuery, ignoreCase = true) ||
                pet.breed.contains(searchQuery, ignoreCase = true)
            val matchesSpecies = selectedSpecies.matches(pet)
            val matchesSize = selectedSizes.isEmpty() || pet.size in selectedSizes
            matchesQuery && matchesSpecies && matchesSize
        }
}

class CatalogViewModel(
    private val petRepository: PetRepository,
    initialCategory: String?
) : ViewModel() {

    private val _uiState = MutableStateFlow(
        CatalogUiState(
            selectedSpecies = initialCategory
                ?.let { name -> runCatching { SpeciesFilter.valueOf(name) }.getOrNull() }
                ?: SpeciesFilter.ALL
        )
    )
    val uiState: StateFlow<CatalogUiState> = _uiState.asStateFlow()

    init {
        load()
    }

    fun load() {
        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            runCatching { petRepository.getAllPets() }
                .onSuccess { pets -> _uiState.update { it.copy(allPets = pets, isLoading = false) } }
                .onFailure { error -> _uiState.update { it.copy(isLoading = false, error = error.message) } }
        }
    }

    fun onSearchChange(value: String) {
        _uiState.update { it.copy(searchQuery = value) }
    }

    fun onSpeciesChange(species: SpeciesFilter) {
        _uiState.update { it.copy(selectedSpecies = species) }
    }

    fun onSizeToggle(size: String) {
        _uiState.update {
            val sizes = if (size in it.selectedSizes) it.selectedSizes - size else it.selectedSizes + size
            it.copy(selectedSizes = sizes)
        }
    }
}
