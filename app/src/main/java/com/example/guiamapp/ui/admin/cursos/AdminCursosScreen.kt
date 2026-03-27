package com.example.guiamapp.ui.admin.cursos

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.CursoDto
import com.example.guiamapp.data.repository.AdminRepository

@Composable
fun AdminCursosScreen(repo: AdminRepository) {
    var cursos by remember { mutableStateOf(listOf<CursoDto>()) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(Unit) {
        try {
            cursos = repo.getCursos()
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(Modifier.padding(16.dp)) {
        Text("Cursos", style = MaterialTheme.typography.headlineSmall)
        Spacer(Modifier.height(8.dp))
        error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error) }

        LazyColumn {
            items(cursos) { c ->
                Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                    Text(c.name, Modifier.padding(16.dp))
                }
            }
        }
    }
}