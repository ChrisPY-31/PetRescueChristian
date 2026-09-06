package com.example.petrescuechristian.ui.screens.nearby

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.GpsFixed
import androidx.compose.material.icons.filled.GpsOff
import androidx.compose.material.icons.filled.LocationOff
import androidx.compose.material.icons.filled.LocationSearching
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petrescuechristian.data.PetData
import com.example.petrescuechristian.location.LocationStatus
import com.example.petrescuechristian.location.rememberLocationState
import com.example.petrescuechristian.model.Pet
import com.example.petrescuechristian.ui.components.DetailRow
import com.example.petrescuechristian.ui.components.MiniMap
import com.example.petrescuechristian.ui.components.colorForSpecies
import com.example.petrescuechristian.ui.components.emojiForSpecies
import com.example.petrescuechristian.util.distanceInKm
import com.example.petrescuechristian.util.formatDistance
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

@Composable
fun NearbyScreen(
    onPetClick: (Int) -> Unit,
    onBackClick: () -> Unit
) {
    val (locationState, retryLocation) = rememberLocationState()
    val availablePets = PetData.pets.filter { it.available }

    val nearbyPets: List<Pair<Pet, Double>> = run {
        val lat = locationState.latitude
        val lon = locationState.longitude
        if (lat != null && lon != null) {
            availablePets
                .map { pet -> pet to distanceInKm(lat, lon, pet.latitude, pet.longitude) }
                .sortedBy { it.second }
        } else {
            emptyList()
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 24.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 4.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Volver")
                }
                Text(
                    text = "Mascotas cercanas",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        StatusIcon(locationState.status)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusLabel(locationState.status),
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    if (locationState.status == LocationStatus.PERMISSION_DENIED ||
                        locationState.status == LocationStatus.PROVIDER_DISABLED ||
                        locationState.status == LocationStatus.ERROR
                    ) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Button(onClick = retryLocation) {
                            Text("Reintentar")
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    DetailRow(
                        label = "Latitud",
                        value = locationState.latitude?.let { String.format(Locale.US, "%.5f", it) } ?: "—"
                    )
                    DetailRow(
                        label = "Longitud",
                        value = locationState.longitude?.let { String.format(Locale.US, "%.5f", it) } ?: "—"
                    )
                    DetailRow(
                        label = "Precisión aproximada",
                        value = locationState.accuracy?.let { "±${it.roundToInt()} m" } ?: "—"
                    )
                    DetailRow(
                        label = "Última actualización",
                        value = locationState.lastUpdated?.let {
                            SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(Date(it))
                        } ?: "—"
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Mapa",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            MiniMap(
                pets = availablePets,
                userLocation = locationState.latitude?.let { lat ->
                    locationState.longitude?.let { lon -> lat to lon }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "🔵 Tú   🟢 Disponible   🟠 Adoptado  ·  toca un marcador para ver el detalle",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
            Text(
                text = "Mascotas cercanas",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 20.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        if (locationState.latitude == null) {
            item {
                Text(
                    text = "Esperando ubicación para calcular distancias...",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else if (nearbyPets.isEmpty()) {
            item {
                Text(
                    text = "No hay mascotas disponibles",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
            }
        } else {
            items(nearbyPets, key = { it.first.id }) { (pet, distanceKm) ->
                NearbyPetRow(
                    pet = pet,
                    distanceKm = distanceKm,
                    onClick = { onPetClick(pet.id) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp, vertical = 6.dp)
                )
            }
        }
    }
}

@Composable
private fun StatusIcon(status: LocationStatus) {
    when (status) {
        LocationStatus.CHECKING_PERMISSION, LocationStatus.SEARCHING -> {
            CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
        }
        LocationStatus.PERMISSION_DENIED -> {
            Icon(Icons.Filled.GpsOff, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        }
        LocationStatus.PROVIDER_DISABLED -> {
            Icon(Icons.Filled.LocationOff, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        }
        LocationStatus.ERROR -> {
            Icon(Icons.Filled.GpsOff, contentDescription = null, tint = MaterialTheme.colorScheme.error)
        }
        LocationStatus.AVAILABLE -> {
            Icon(Icons.Filled.GpsFixed, contentDescription = null, tint = Color(0xFF2E7D32))
        }
    }
}

private fun statusLabel(status: LocationStatus): String = when (status) {
    LocationStatus.CHECKING_PERMISSION -> "Solicitando permiso de ubicación..."
    LocationStatus.PERMISSION_DENIED -> "Permiso de ubicación denegado"
    LocationStatus.PROVIDER_DISABLED -> "GPS desactivado en el dispositivo"
    LocationStatus.SEARCHING -> "Obteniendo tu ubicación..."
    LocationStatus.ERROR -> "No se pudo obtener tu ubicación (sin señal GPS)"
    LocationStatus.AVAILABLE -> "Ubicación activa"
}

@Composable
private fun NearbyPetRow(
    pet: Pet,
    distanceKm: Double,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(onClick = onClick, modifier = modifier) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(52.dp)
                    .background(colorForSpecies(pet.species), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Text(text = emojiForSpecies(pet.species), fontSize = 26.sp)
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(text = pet.name, fontWeight = FontWeight.Bold)
                Text(
                    text = "${pet.species} • ${pet.breed}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Surface(
                color = MaterialTheme.colorScheme.primaryContainer,
                shape = RoundedCornerShape(10.dp)
            ) {
                Text(
                    text = formatDistance(distanceKm),
                    style = MaterialTheme.typography.labelMedium,
                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                )
            }
        }
    }
}
