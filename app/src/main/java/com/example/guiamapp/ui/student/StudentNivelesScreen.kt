package com.example.guiamapp.ui.student

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.StudentNivelDto
import com.example.guiamapp.data.repository.StudentRepository

@Composable
fun StudentNivelesScreen(
    repository: StudentRepository,
    cursoId: Int,
    onOpenNivel: (folderLevelId: Int) -> Unit
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var niveles by remember { mutableStateOf<List<StudentNivelDto>>(emptyList()) }
    var cursoNombre by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(cursoId) {
        try {
            val resp = repository.getNiveles(cursoId)
            niveles = resp.niveles
            cursoNombre = resp.cursoNombre
            loading = false
        } catch (e: Exception) {
            error = e.message
            loading = false
        }
    }

    Column(Modifier.padding(16.dp)) {
        Text("Curso: ${cursoNombre ?: "N/A"}", style = MaterialTheme.typography.titleLarge)
        Spacer(Modifier.height(8.dp))

        if (loading) { CircularProgressIndicator(); return@Column }
        error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error); return@Column }

        LazyColumn {
            items(niveles) { n ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable { onOpenNivel(n.id) }
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(text = n.name, style = MaterialTheme.typography.titleMedium)
                        n.id_level_reference?.let {
                            Text(text = "Nivel referencia: $it")
                        }
                    }
                }
            }
        }
    }
}