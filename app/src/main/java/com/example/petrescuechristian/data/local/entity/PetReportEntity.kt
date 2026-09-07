package com.example.petrescuechristian.data.local.entity

data class PetReportEntity(
    val id: Int,
    val species: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUri: String?,
    val reportDate: String,
    val status: String
)
