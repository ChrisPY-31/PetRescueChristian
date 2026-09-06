package com.example.petrescuechristian.model

data class PetReport(
    val id: Int,
    val species: String,
    val description: String,
    val latitude: Double,
    val longitude: Double,
    val imageUri: String?,
    val date: String,
    val status: String
)
