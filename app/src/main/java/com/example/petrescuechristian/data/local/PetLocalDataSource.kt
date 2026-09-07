package com.example.petrescuechristian.data.local

import com.example.petrescuechristian.data.local.entity.PetEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.ResultSet

class PetLocalDataSource {

    private val columns =
        "id, name, species, breed, age, size, description, image_uri, latitude, longitude, available"

    suspend fun getAllPets(): List<PetEntity> = withContext(Dispatchers.IO) {
        DatabaseConnectionProvider.getConnection().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT $columns FROM pets ORDER BY id").use { rs ->
                    val pets = mutableListOf<PetEntity>()
                    while (rs.next()) pets.add(rs.toPetEntity())
                    pets
                }
            }
        }
    }

    suspend fun getPetById(id: Int): PetEntity? = withContext(Dispatchers.IO) {
        DatabaseConnectionProvider.getConnection().use { connection ->
            connection.prepareStatement("SELECT $columns FROM pets WHERE id = ?").use { statement ->
                statement.setInt(1, id)
                statement.executeQuery().use { rs ->
                    if (rs.next()) rs.toPetEntity() else null
                }
            }
        }
    }

    private fun ResultSet.toPetEntity() = PetEntity(
        id = getInt("id"),
        name = getString("name"),
        species = getString("species"),
        breed = getString("breed"),
        age = getInt("age"),
        size = getString("size"),
        description = getString("description"),
        imageUri = getString("image_uri"),
        latitude = getDouble("latitude"),
        longitude = getDouble("longitude"),
        available = getBoolean("available")
    )
}
