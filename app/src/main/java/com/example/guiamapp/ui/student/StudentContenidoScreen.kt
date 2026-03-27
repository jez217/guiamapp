package com.example.guiamapp.ui.student

import android.net.Uri
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavController
import com.example.guiamapp.data.remote.api.StudentContenidoResponse
import com.example.guiamapp.data.remote.api.StudentFileDto
import com.example.guiamapp.data.repository.StudentRepository
import com.example.guiamapp.ui.student.download.DownloadUtil

@Composable
fun StudentContenidoScreen(
    repository: StudentRepository,
    folderLevelId: Int,
    navController: NavController
) {
    val context = LocalContext.current

    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var contenido by remember { mutableStateOf<StudentContenidoResponse?>(null) }

    LaunchedEffect(folderLevelId) {
        try {
            contenido = repository.getContenidoNivel(folderLevelId)
            loading = false
        } catch (e: Exception) {
            error = e.message
            loading = false
        }
    }

    Column(Modifier.padding(16.dp)) {
        Text("Contenido del nivel", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))

        if (loading) {
            CircularProgressIndicator()
            return@Column
        }

        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
            return@Column
        }

        val files = contenido?.files.orEmpty()
        Text("Carpeta: ${contenido?.folderNombre ?: "N/A"}")
        Spacer(Modifier.height(8.dp))


        LazyColumn {
            items(files) { file ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Column(Modifier.padding(12.dp)) {
                        Text(file.name, style = MaterialTheme.typography.titleMedium)
                        val url = FileHelpers.absoluteUrl(file.filePath)

                        // ACCIONES
                        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            // Abrir
                            Button(onClick = {
                                val kind = FileHelpers.detectKind(file.filePath)
                                val abs = url ?: return@Button
                                when (kind) {
                                    FileHelpers.FileKind.IMAGE -> navController.navigate("image_viewer/${Uri.encode(abs)}")
                                    FileHelpers.FileKind.VIDEO -> navController.navigate("video_player/${Uri.encode(abs)}")
                                    FileHelpers.FileKind.PDF,
                                    FileHelpers.FileKind.OTHER -> {
                                        // Descargar y abrir (se abrirá al finalizar desde notificación; si quieres abrir inmediato, implementa callback del WorkManager)
                                        DownloadUtil.enqueueDownload(context, abs, file.name, FileHelpers.guessMime(file.name))
                                    }
                                }
                            }) { Text("Abrir") }

                            // Descargar
                            Button(onClick = {
                                val abs = url ?: return@Button
                                DownloadUtil.enqueueDownload(context, abs, file.name, FileHelpers.guessMime(file.name))
                            }) { Text("Descargar") }

                            // Compartir (si ya está en caché de descargas)
                            Button(onClick = {
                                val cached = FileHelpers.cacheFileIfExists(context, file.name)
                                if (cached != null) {
                                    FileHelpers.shareFile(context, cached, FileHelpers.guessMime(file.name))
                                } else {
                                    // Si no existe aún, dispara la descarga primero
                                    val abs = url ?: return@Button
                                    DownloadUtil.enqueueDownload(context, abs, file.name, FileHelpers.guessMime(file.name))
                                }
                            }) { Text("Compartir") }
                        }
                    }
                }
            }
        }
    }
}