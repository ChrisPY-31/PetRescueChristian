package com.example.petrescuechristian.data.mapper

import com.example.petrescuechristian.data.local.entity.PetReportEntity
import com.example.petrescuechristian.domain.model.PetReport

fun PetReportEntity.toDomain(): PetReport = PetReport(
    id = id,
    species = species,
    description = description,
    latitude = latitude,
    longitude = longitude,
    imageUri = imageUri,
    date = reportDate,
    status = status
)
