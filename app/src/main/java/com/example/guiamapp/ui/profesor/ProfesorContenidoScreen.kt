package com.example.guiamapp.ui.profesor

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.ProfesorContenidoDto
import com.example.guiamapp.data.remote.api.ProfesorFileDto
import com.example.guiamapp.data.repository.ProfesorRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

@Composable
fun ProfesorContenidoScreen(
    repo: ProfesorRepository,
    folderLevelId: Int
) {
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var contenido: ProfesorContenidoDto? by remember {
        mutableStateOf(null)
    }
    var showUpload by remember { mutableStateOf(false) }
    var loading by remember { mutableStateOf(true) }

    // Carga inicial
    LaunchedEffect(folderLevelId) {
        try {
            contenido = repo.getContenido(folderLevelId)
        } catch (e: Exception) {
            snackbarHostState.showSnackbar("Error cargando contenido")
        } finally {
            loading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
        ) {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "Contenido del nivel",
                    style = MaterialTheme.typography.headlineSmall
                )

                Button(onClick = { showUpload = true }) {
                    Text("Subir archivo")
                }
            }

            Spacer(Modifier.height(12.dp))

            if (loading) {
                CircularProgressIndicator()
                return@Column
            }

            val files: List<ProfesorFileDto> = contenido?.files ?: emptyList()

            LazyColumn {
                items(files) { file: ProfesorFileDto ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(file.name)

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                onClick = {
                                    scope.launch {
                                        try {
                                            repo.deleteFile(file.id)
                                            contenido = repo.getContenido(folderLevelId)
                                            snackbarHostState.showSnackbar("Archivo eliminado")
                                        } catch (e: Exception) {
                                            snackbarHostState.showSnackbar("Error eliminando archivo")
                                        }
                                    }
                                }
                            ) {
                                Text("Eliminar")
                            }
                        }
                    }
                }
            }
        }
    }

    // Dialog de subida
    if (showUpload) {
        UploadFileDialog(
            folderLevelId = folderLevelId,
            onDismiss = { showUpload = false },
            onUpload = { part: MultipartBody.Part, folderId: RequestBody ->
                scope.launch {
                    try {
                        repo.uploadFile(part, folderId)
                        contenido = repo.getContenido(folderLevelId)
                        snackbarHostState.showSnackbar("Archivo subido correctamente")
                    } catch (e: Exception) {
                        snackbarHostState.showSnackbar("Error subiendo archivo")
                    } finally {
                        showUpload = false
                    }
                }
            }
        )
    }
}