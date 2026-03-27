package com.example.guiamapp.ui.student

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.repository.StudentRepository
import kotlinx.coroutines.launch

@Composable
fun StudentHomeScreen(
    repository: StudentRepository,
    onVerNiveles: (cursoId: Int) -> Unit
) {
    val scope = rememberCoroutineScope()
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    var cursoId by remember { mutableStateOf<Int?>(null) }
    var cursoNombre by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            val resp = repository.getCursoActual()
            cursoId = resp.cursoId
            cursoNombre = resp.cursoNombre
            loading = false
        } catch (e: Exception) {
            error = e.message
            loading = false
        }
    }

    Column(Modifier.padding(16.dp)) {
        Text("Panel Estudiante", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))

        if (loading) {
            CircularProgressIndicator()
            return@Column
        }

        error?.let {
            Text("Error: $it", color = MaterialTheme.colorScheme.error)
            return@Column
        }

        Text("Curso asignado: ${cursoNombre ?: "N/A"}")
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = { cursoId?.let(onVerNiveles) },
            enabled = (cursoId != null),
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Ver niveles")
        }
    }
}