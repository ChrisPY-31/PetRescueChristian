package com.example.petrescuechristian.data.local

import com.example.petrescuechristian.BuildConfig
import java.sql.Connection
import java.sql.DriverManager
import java.util.Properties

/**
 * Punto único de apertura de conexiones JDBC a PostgreSQL.
 * Cada DataSource abre y cierra su propia conexión por operación (sin pool),
 * suficiente para el alcance de este proyecto.
 */
object DatabaseConnectionProvider {

    init {
        Class.forName("org.postgresql.Driver")
    }

    fun getConnection(): Connection {
        val url = "jdbc:postgresql://${BuildConfig.DB_HOST}:${BuildConfig.DB_PORT}/${BuildConfig.DB_NAME}"
        val props = Properties().apply {
            setProperty("user", BuildConfig.DB_USER)
            setProperty("password", BuildConfig.DB_PASSWORD)
            setProperty("connectTimeout", "5")
            setProperty("socketTimeout", "10")
        }
        return DriverManager.getConnection(url, props)
    }
}
