package com.example.petrescuechristian.data.local

import com.example.petrescuechristian.data.local.entity.PetReportEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.sql.ResultSet
import java.sql.Types

class PetReportLocalDataSource {

    private val columns = "id, species, description, latitude, longitude, image_uri, report_date, status"

    suspend fun getAllReports(): List<PetReportEntity> = withContext(Dispatchers.IO) {
        DatabaseConnectionProvider.getConnection().use { connection ->
            connection.createStatement().use { statement ->
                statement.executeQuery("SELECT $columns FROM pet_reports ORDER BY id DESC").use { rs ->
                    val reports = mutableListOf<PetReportEntity>()
                    while (rs.next()) reports.add(rs.toPetReportEntity())
                    reports
                }
            }
        }
    }

    suspend fun getReportById(id: Int): PetReportEntity? = withContext(Dispatchers.IO) {
        DatabaseConnectionProvider.getConnection().use { connection ->
            connection.prepareStatement("SELECT $columns FROM pet_reports WHERE id = ?").use { statement ->
                statement.setInt(1, id)
                statement.executeQuery().use { rs ->
                    if (rs.next()) rs.toPetReportEntity() else null
                }
            }
        }
    }

    suspend fun insertReport(entity: PetReportEntity): PetReportEntity = withContext(Dispatchers.IO) {
        DatabaseConnectionProvider.getConnection().use { connection ->
            connection.prepareStatement(
                """
                INSERT INTO pet_reports (species, description, latitude, longitude, image_uri, report_date, status)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                RETURNING id
                """.trimIndent()
            ).use { statement ->
                statement.setString(1, entity.species)
                statement.setString(2, entity.description)
                statement.setDouble(3, entity.latitude)
                statement.setDouble(4, entity.longitude)
                if (entity.imageUri != null) {
                    statement.setString(5, entity.imageUri)
                } else {
                    statement.setNull(5, Types.VARCHAR)
                }
                statement.setString(6, entity.reportDate)
                statement.setString(7, entity.status)
                statement.executeQuery().use { rs ->
                    rs.next()
                    entity.copy(id = rs.getInt("id"))
                }
            }
        }
    }

    private fun ResultSet.toPetReportEntity() = PetReportEntity(
        id = getInt("id"),
        species = getString("species"),
        description = getString("description"),
        latitude = getDouble("latitude"),
        longitude = getDouble("longitude"),
        imageUri = getString("image_uri"),
        reportDate = getString("report_date"),
        status = getString("status")
    )
}
