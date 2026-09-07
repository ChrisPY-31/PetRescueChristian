package com.example.petrescuechristian.data.local.entity

data class PetEntity(
    val id: Int,
    val name: String,
    val species: String,
    val breed: String,
    val age: Int,
    val size: String,
    val description: String,
    val imageUri: String?,
    val latitude: Double,
    val longitude: Double,
    val available: Boolean
)
