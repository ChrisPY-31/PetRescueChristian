package com.example.petrescuechristian.data

import androidx.compose.runtime.mutableStateListOf
import com.example.petrescuechristian.model.PetReport

/**
 * Reportes de mascotas encontradas, manejados en memoria (sin persistencia ni backend).
 */
object ReportRepository {
    private val _reports = mutableStateListOf<PetReport>()
    val reports: List<PetReport> get() = _reports

    private var nextId = 1

    fun generateId(): Int = nextId++

    fun addReport(report: PetReport) {
        _reports.add(0, report)
    }

    fun findById(id: Int): PetReport? = _reports.firstOrNull { it.id == id }
}
