package com.example.petrescuechristian.di

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

/**
 * Factory genérica para construir ViewModels con dependencias por constructor
 * sin necesitar Hilt/Koin. Se usa como: viewModel(factory = ViewModelFactory { MiViewModel(...) })
 */
class ViewModelFactory(private val creator: () -> ViewModel) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T = creator() as T
}
