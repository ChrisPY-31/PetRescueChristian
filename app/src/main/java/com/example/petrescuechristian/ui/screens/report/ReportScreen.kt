package com.example.petrescuechristian.ui.screens.report

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.petrescuechristian.di.AppContainer
import com.example.petrescuechristian.di.ViewModelFactory
import com.example.petrescuechristian.location.LocationStatus
import com.example.petrescuechristian.location.rememberLocationState
import com.example.petrescuechristian.ui.viewmodel.ReportViewModel
import com.example.petrescuechristian.util.createPhotoFile
import com.example.petrescuechristian.util.decodeSampledBitmap
import com.example.petrescuechristian.util.getUriForFile
import java.io.File
import java.util.Locale

private val animalTypes = listOf("Perro", "Gato", "Otro")

@Composable
fun ReportScreen(
    onReportCreated: (Int) -> Unit,
    onBackClick: () -> Unit,
    viewModel: ReportViewModel = viewModel(
        factory = ViewModelFactory { ReportViewModel(AppContainer.petReportRepository) }
    )
) {
    val context = LocalContext.current
    val (locationState, retryLocation) = rememberLocationState()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.createdReportId) {
        uiState.createdReportId?.let { onReportCreated(it) }
    }

    var pendingPhotoFile by remember { mutableStateOf<File?>(null) }
    var reviewPhotoPath by remember { mutableStateOf<String?>(null) }

    val takePictureLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            // Paso 4: la foto recién tomada se muestra en vista previa antes de asociarla al reporte.
            reviewPhotoPath = pendingPhotoFile?.absolutePath
        } else {
            pendingPhotoFile?.delete()
        }
        pendingPhotoFile = null
    }

    fun launchCamera() {
        val file = createPhotoFile(context)
        pendingPhotoFile = file
        takePictureLauncher.launch(getUriForFile(context, file))
    }

    val cameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) {
            launchCamera()
        } else {
            viewModel.onPhotoError("Se requiere permiso de cámara para tomar la foto")
        }
    }

    fun requestPhoto() {
        val hasPermission = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (hasPermission) {
            launchCamera()
        } else {
            cameraPermissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    reviewPhotoPath?.let { path ->
        PhotoReviewDialog(
            photoPath = path,
            onAccept = {
                // Paso 6: se acepta y se asocia la imagen (ya almacenada localmente) al reporte.
                viewModel.onPhotoChange(path)
                reviewPhotoPath = null
            },
            onRetake = {
                File(path).delete()
                reviewPhotoPath = null
                requestPhoto()
            },
            onDismiss = {
                File(path).delete()
                reviewPhotoPath = null
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ) {
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
                text = "Encontré una mascota",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
        }

        Column(modifier = Modifier.padding(horizontal = 20.dp)) {
            Text(
                text = "Tipo de animal",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Column {
                animalTypes.forEach { type ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .selectable(
                                selected = uiState.animalType == type,
                                onClick = { viewModel.onAnimalTypeChange(type) }
                            )
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = uiState.animalType == type,
                            onClick = { viewModel.onAnimalTypeChange(type) }
                        )
                        Text(text = type)
                    }
                }
            }
            uiState.animalTypeError?.let {
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Descripción",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))
            OutlinedTextField(
                value = uiState.description,
                onValueChange = viewModel::onDescriptionChange,
                placeholder = { Text("Color, tamaño, lugar exacto, estado de la mascota...") },
                isError = uiState.descriptionError != null,
                supportingText = { uiState.descriptionError?.let { Text(it) } },
                minLines = 3,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Fotografía",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            val previewBitmap = remember(uiState.photoPath) {
                uiState.photoPath?.let { decodeSampledBitmap(it, 900, 900) }
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .clickable { requestPhoto() },
                contentAlignment = Alignment.Center
            ) {
                if (previewBitmap != null) {
                    Image(
                        bitmap = previewBitmap.asImageBitmap(),
                        contentDescription = "Fotografía de la mascota encontrada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Filled.CameraAlt,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Toca para tomar una fotografía",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            if (previewBitmap != null) {
                Spacer(modifier = Modifier.height(8.dp))
                OutlinedButton(onClick = { requestPhoto() }) {
                    Icon(Icons.Filled.CameraAlt, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Volver a tomar la foto")
                }
            }

            uiState.photoError?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Ubicación",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(8.dp))

            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (locationState.status == LocationStatus.SEARCHING ||
                        locationState.status == LocationStatus.CHECKING_PERMISSION
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                    } else {
                        Icon(
                            imageVector = Icons.Filled.LocationOn,
                            contentDescription = null,
                            tint = if (locationState.status == LocationStatus.AVAILABLE) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.error
                            }
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = when (locationState.status) {
                                LocationStatus.AVAILABLE ->
                                    "Lat: %.5f, Lon: %.5f".format(
                                        Locale.US,
                                        locationState.latitude,
                                        locationState.longitude
                                    )
                                LocationStatus.CHECKING_PERMISSION -> "Solicitando permiso de ubicación..."
                                LocationStatus.SEARCHING -> "Obteniendo tu ubicación..."
                                LocationStatus.PERMISSION_DENIED -> "Permiso de ubicación denegado"
                                LocationStatus.PROVIDER_DISABLED -> "GPS desactivado"
                                LocationStatus.ERROR -> "No se pudo obtener tu ubicación"
                            },
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    if (locationState.status == LocationStatus.PERMISSION_DENIED ||
                        locationState.status == LocationStatus.PROVIDER_DISABLED ||
                        locationState.status == LocationStatus.ERROR
                    ) {
                        OutlinedButton(onClick = retryLocation) {
                            Text("Reintentar")
                        }
                    }
                }
            }

            uiState.locationError?.let {
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = it,
                    color = MaterialTheme.colorScheme.error,
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Spacer(modifier = Modifier.height(28.dp))

            Button(
                onClick = { viewModel.submit(locationState.latitude, locationState.longitude) },
                enabled = !uiState.isSubmitting,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                if (uiState.isSubmitting) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                } else {
                    Text(
                        text = "GENERAR REPORTE",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(28.dp))
        }
    }
}

/** Paso 4 y 5 del flujo de cámara: muestra la vista previa y permite aceptar o repetir la foto. */
@Composable
private fun PhotoReviewDialog(
    photoPath: String,
    onAccept: () -> Unit,
    onRetake: () -> Unit,
    onDismiss: () -> Unit
) {
    val bitmap = remember(photoPath) { decodeSampledBitmap(photoPath, 900, 900) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = MaterialTheme.colorScheme.surface
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Vista previa de la fotografía",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(12.dp))

                if (bitmap != null) {
                    Image(
                        bitmap = bitmap.asImageBitmap(),
                        contentDescription = "Vista previa de la fotografía capturada",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(240.dp)
                            .clip(RoundedCornerShape(14.dp))
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedButton(
                        onClick = onRetake,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Replay, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Volver a tomar")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = onAccept,
                        modifier = Modifier.weight(1f)
                    ) {
                        Icon(Icons.Filled.Check, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Aceptar")
                    }
                }
            }
        }
    }
}
