package com.example.petrescuechristian.data.mapper

import com.example.petrescuechristian.data.local.entity.UserEntity
import com.example.petrescuechristian.domain.model.User

// Solo Entity -> Domain: la contraseña vive en la Entity y nunca debe llegar al dominio/UI.
fun UserEntity.toDomain(): User = User(
    id = id,
    name = name,
    email = email
)
