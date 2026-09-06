package com.example.petrescuechristian.location

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationListener
import android.location.LocationManager
import android.os.Looper
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import kotlinx.coroutines.delay

private const val LOCATION_TIMEOUT_MS = 12_000L

enum class LocationStatus {
    CHECKING_PERMISSION,
    PERMISSION_DENIED,
    PROVIDER_DISABLED,
    SEARCHING,
    AVAILABLE,
    ERROR
}

data class LocationState(
    val status: LocationStatus,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val accuracy: Float? = null,
    val lastUpdated: Long? = null
)

/**
 * Obtiene la ubicación del dispositivo mediante [LocationManager] (sin Google Play Services).
 * Maneja el permiso en tiempo de ejecución y expone un [LocationState] reactivo junto con
 * una función para reintentar (solicitar permiso de nuevo o re-consultar el proveedor GPS).
 */
@Composable
fun rememberLocationState(): Pair<LocationState, () -> Unit> {
    val context = LocalContext.current

    var permissionGranted by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    var state by remember {
        mutableStateOf(
            LocationState(
                status = if (permissionGranted) LocationStatus.SEARCHING else LocationStatus.CHECKING_PERMISSION
            )
        )
    }

    var retryTrigger by remember { mutableIntStateOf(0) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { results ->
        val granted = results[Manifest.permission.ACCESS_FINE_LOCATION] == true ||
            results[Manifest.permission.ACCESS_COARSE_LOCATION] == true
        permissionGranted = granted
        if (!granted) {
            state = LocationState(status = LocationStatus.PERMISSION_DENIED)
        }
    }

    LaunchedEffect(Unit) {
        if (!permissionGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        }
    }

    DisposableEffect(permissionGranted, retryTrigger) {
        if (!permissionGranted) {
            onDispose { }
        } else {
            val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
            val provider = when {
                locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) -> LocationManager.GPS_PROVIDER
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER) -> LocationManager.NETWORK_PROVIDER
                else -> null
            }

            if (provider == null) {
                state = LocationState(status = LocationStatus.PROVIDER_DISABLED)
                onDispose { }
            } else {
                state = LocationState(status = LocationStatus.SEARCHING)

                val listener = LocationListener { location ->
                    state = LocationState(
                        status = LocationStatus.AVAILABLE,
                        latitude = location.latitude,
                        longitude = location.longitude,
                        accuracy = location.accuracy,
                        lastUpdated = System.currentTimeMillis()
                    )
                }

                try {
                    locationManager.getLastKnownLocation(provider)?.let { last ->
                        state = LocationState(
                            status = LocationStatus.AVAILABLE,
                            latitude = last.latitude,
                            longitude = last.longitude,
                            accuracy = last.accuracy,
                            lastUpdated = System.currentTimeMillis()
                        )
                    }
                    // minTime=2s / minDistance=3m: la ubicación se sigue actualizando
                    // mientras el usuario se desplaza, no es una lectura única.
                    locationManager.requestLocationUpdates(
                        provider,
                        2000L,
                        3f,
                        listener,
                        Looper.getMainLooper()
                    )
                } catch (securityException: SecurityException) {
                    state = LocationState(status = LocationStatus.PERMISSION_DENIED)
                }

                onDispose {
                    locationManager.removeUpdates(listener)
                }
            }
        }
    }

    // Error de GPS: si tras un permiso concedido y proveedor activo nunca llega una posición
    // (sin señal, emulador sin ubicación configurada, etc.), se informa en vez de dejar
    // "buscando..." indefinidamente.
    LaunchedEffect(permissionGranted, retryTrigger) {
        delay(LOCATION_TIMEOUT_MS)
        if (state.status == LocationStatus.SEARCHING) {
            state = LocationState(status = LocationStatus.ERROR)
        }
    }

    val retry: () -> Unit = {
        if (!permissionGranted) {
            permissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            retryTrigger++
        }
    }

    return state to retry
}
