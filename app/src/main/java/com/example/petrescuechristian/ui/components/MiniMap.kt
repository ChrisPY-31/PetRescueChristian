package com.example.petrescuechristian.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import com.example.petrescuechristian.model.Pet
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private const val DEFAULT_ZOOM = 13f

/**
 * Mapa real de Google (Maps SDK for Android vía Maps Compose) que ubica al usuario y a las
 * mascotas con marcadores. Requiere una API key de Maps SDK for Android configurada en el
 * manifest (com.google.android.geo.API_KEY).
 */
@Composable
fun MiniMap(
    pets: List<Pet>,
    userLocation: Pair<Double, Double>?,
    modifier: Modifier = Modifier,
    userLocationLabel: String = "Tú"
) {
    val initialTarget = userLocation?.let { LatLng(it.first, it.second) }
        ?: pets.firstOrNull()?.let { LatLng(it.latitude, it.longitude) }
        ?: LatLng(0.0, 0.0)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(initialTarget, DEFAULT_ZOOM)
    }

    LaunchedEffect(userLocation) {
        userLocation?.let { (lat, lon) ->
            cameraPositionState.move(
                CameraUpdateFactory.newLatLngZoom(LatLng(lat, lon), DEFAULT_ZOOM)
            )
        }
    }

    GoogleMap(
        modifier = modifier,
        cameraPositionState = cameraPositionState,
        properties = MapProperties(isMyLocationEnabled = false),
        uiSettings = MapUiSettings(
            zoomControlsEnabled = false,
            myLocationButtonEnabled = false
        )
    ) {
        pets.forEach { pet ->
            Marker(
                state = MarkerState(position = LatLng(pet.latitude, pet.longitude)),
                title = pet.name,
                snippet = if (pet.available) "Disponible" else "Adoptado",
                icon = BitmapDescriptorFactory.defaultMarker(
                    if (pet.available) {
                        BitmapDescriptorFactory.HUE_GREEN
                    } else {
                        BitmapDescriptorFactory.HUE_ORANGE
                    }
                )
            )
        }

        userLocation?.let { (lat, lon) ->
            Marker(
                state = MarkerState(position = LatLng(lat, lon)),
                title = userLocationLabel,
                icon = BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)
            )
        }
    }
}
