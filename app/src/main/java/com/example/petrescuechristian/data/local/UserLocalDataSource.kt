package com.example.petrescuechristian.data.local

import com.example.petrescuechristian.data.local.entity.UserEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserLocalDataSource {

    suspend fun findByIdentifier(identifier: String): UserEntity? = withContext(Dispatchers.IO) {
        DatabaseConnectionProvider.getConnection().use { connection ->
            connection.prepareStatement(
                "SELECT id, name, username, email, password FROM users WHERE LOWER(username) = LOWER(?) OR LOWER(email) = LOWER(?)"
            ).use { statement ->
                statement.setString(1, identifier)
                statement.setString(2, identifier)
                statement.executeQuery().use { rs ->
                    if (rs.next()) {
                        UserEntity(
                            id = rs.getInt("id"),
                            name = rs.getString("name"),
                            username = rs.getString("username"),
                            email = rs.getString("email"),
                            password = rs.getString("password")
                        )
                    } else {
                        null
                    }
                }
            }
        }
    }
}
