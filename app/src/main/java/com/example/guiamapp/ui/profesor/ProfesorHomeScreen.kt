package com.example.guiamapp.ui.profesor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.ProfesorCursoDto
import com.example.guiamapp.data.repository.ProfesorRepository
import kotlinx.coroutines.launch

@Composable
fun ProfesorHomeScreen(
    repo: ProfesorRepository,
    onOpenCurso: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var cursos: List<ProfesorCursoDto> by remember {
        mutableStateOf(emptyList())
    }

    var loading: Boolean by remember {
        mutableStateOf(true)
    }

    LaunchedEffect(Unit) {
        try {
            cursos = repo.getCursos()
        } catch (e: Exception) {
            snackbarHostState.showSnackbar("Error cargando cursos")
        } finally {
            loading = false
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            Text(
                text = "Mis Cursos",
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(Modifier.height(12.dp))

            if (loading) {
                CircularProgressIndicator()
                return@Column
            }

            cursos.forEach { curso: ProfesorCursoDto ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clickable {
                            onOpenCurso(curso.id)
                        }
                ) {
                    Text(
                        text = curso.name,
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                }
            }
        }
    }
}