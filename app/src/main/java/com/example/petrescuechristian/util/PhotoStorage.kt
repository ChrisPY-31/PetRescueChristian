package com.example.petrescuechristian.util

import android.content.Context
import android.net.Uri
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

private const val PHOTOS_DIR_NAME = "photos"

/**
 * Crea un archivo nuevo dentro del almacenamiento privado de la app (almacenamiento local
 * del dispositivo, sin backend) donde la cámara guardará la fotografía capturada.
 */
fun createPhotoFile(context: Context): File {
    val photosDir = File(context.filesDir, PHOTOS_DIR_NAME).apply { mkdirs() }
    val timestamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    return File(photosDir, "REPORT_$timestamp.jpg")
}

/** URI segura (content://) para que la app de cámara pueda escribir en el archivo local. */
fun getUriForFile(context: Context, file: File): Uri =
    FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
