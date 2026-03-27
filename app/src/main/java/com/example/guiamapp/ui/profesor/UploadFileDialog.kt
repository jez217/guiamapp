package com.example.guiamapp.ui.profesor

import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

@Composable
fun UploadFileDialog(
    folderLevelId: Int,
    onDismiss: () -> Unit,
    onUpload: (MultipartBody.Part, RequestBody) -> Unit
) {
    val context = LocalContext.current

    var selectedUri: Uri? by remember { mutableStateOf(null) }
    var fileName: String? by remember { mutableStateOf(null) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            selectedUri = uri

            // Intenta obtener nombre amigable
            fileName = uri.lastPathSegment?.substringAfterLast("/")
        }
    }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Subir archivo") },
        text = {
            Column {
                Button(
                    modifier = Modifier.fillMaxWidth(),
                    onClick = {
                        launcher.launch(arrayOf("*/*"))
                    }
                ) {
                    Text("Seleccionar archivo")
                }

                Spacer(Modifier.height(12.dp))

                selectedUri?.let {
                    Text(
                        text = "Archivo: ${fileName ?: "seleccionado"}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        },
        confirmButton = {
            Button(
                enabled = selectedUri != null,
                onClick = {
                    val uri = selectedUri ?: return@Button
                    val stream = context.contentResolver.openInputStream(uri) ?: return@Button

                    val bytes = stream.readBytes()
                    stream.close()

                    val requestFile: RequestBody =
                        bytes.toRequestBody("application/octet-stream".toMediaTypeOrNull())

                    val part = MultipartBody.Part.createFormData(
                        name = "file",
                        filename = fileName ?: "upload.bin",
                        body = requestFile
                    )

                    val folderIdBody: RequestBody =
                        folderLevelId
                            .toString()
                            .toRequestBody("text/plain".toMediaTypeOrNull())

                    onUpload(part, folderIdBody)
                }
            ) {
                Text("Subir")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}