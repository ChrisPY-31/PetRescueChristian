package com.example.petrescuechristian.ui.screens.home

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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Assignment
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.AddAPhoto
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.petrescuechristian.data.PetData
import com.example.petrescuechristian.data.SessionManager
import com.example.petrescuechristian.data.SpeciesFilter
import com.example.petrescuechristian.ui.components.CategoryChip
import com.example.petrescuechristian.ui.components.PetCard
import com.example.petrescuechristian.ui.components.QuickAccessCard
import com.example.petrescuechristian.ui.theme.PetRescueChristianTheme

@Composable
fun HomeScreen(
    onCategoryClick: (SpeciesFilter) -> Unit,
    onPetClick: (Int) -> Unit,
    onSeeAllClick: () -> Unit,
    onReportClick: () -> Unit,
    onNearbyClick: () -> Unit,
    onFavoritesClick: () -> Unit,
    onMyReportsClick: () -> Unit,
    onLogout: () -> Unit
) {
    val user = SessionManager.currentUser
    val availablePets = PetData.pets.filter { it.available }

    Column(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    Brush.horizontalGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.tertiary
                        )
                    )
                )
                .padding(horizontal = 20.dp, vertical = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.25f),
                modifier = Modifier.size(48.dp)
            ) {
                Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxSize()) {
                    Text(
                        text = user?.name?.firstOrNull()?.uppercase() ?: "?",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = "Hola, ${user?.name.orEmpty()} 👋",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = "¿Qué mascota buscas hoy?",
                    color = Color.White.copy(alpha = 0.9f),
                    style = MaterialTheme.typography.bodySmall
                )
            }

            IconButton(onClick = onMyReportsClick) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Assignment,
                    contentDescription = "Mis reportes",
                    tint = Color.White
                )
            }

            IconButton(onClick = onFavoritesClick) {
                Icon(
                    imageVector = Icons.Filled.Favorite,
                    contentDescription = "Mis favoritos",
                    tint = Color.White
                )
            }

            IconButton(onClick = onLogout) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Logout,
                    contentDescription = "Cerrar sesión",
                    tint = Color.White
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(vertical = 20.dp)
        ) {
            item {
                Text(
                    text = "Categorías",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 20.dp)
                )
                Spacer(modifier = Modifier.height(12.dp))
                LazyRow(
                    contentPadding = PaddingValues(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    items(SpeciesFilter.entries) { category ->
                        CategoryChip(
                            label = category.label,
                            emoji = category.emoji,
                            selected = false,
                            onClick = { onCategoryClick(category) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp)
                ) {
                    QuickAccessCard(
                        title = "ENCONTRÉ UNA MASCOTA",
                        subtitle = "Repórtala con foto y ubicación",
                        icon = Icons.Filled.AddAPhoto,
                        containerColor = MaterialTheme.colorScheme.primary,
                        onClick = onReportClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    QuickAccessCard(
                        title = "Mascotas cercanas",
                        subtitle = "Ver mascotas cerca de tu ubicación",
                        icon = Icons.Filled.LocationOn,
                        containerColor = MaterialTheme.colorScheme.tertiary,
                        onClick = onNearbyClick,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
            }

            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 20.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Mascotas disponibles",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    TextButton(onClick = onSeeAllClick) {
                        Text("Ver todas")
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                if (availablePets.isEmpty()) {
                    Text(
                        text = "No hay mascotas disponibles por el momento",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 20.dp)
                    )
                } else {
                    LazyRow(
                        contentPadding = PaddingValues(horizontal = 20.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        items(availablePets) { pet ->
                            PetCard(
                                pet = pet,
                                onClick = { onPetClick(pet.id) },
                                modifier = Modifier.width(160.dp)
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    PetRescueChristianTheme {
        HomeScreen(
            onCategoryClick = {},
            onPetClick = {},
            onSeeAllClick = {},
            onReportClick = {},
            onNearbyClick = {},
            onFavoritesClick = {},
            onMyReportsClick = {},
            onLogout = {}
        )
    }
}
