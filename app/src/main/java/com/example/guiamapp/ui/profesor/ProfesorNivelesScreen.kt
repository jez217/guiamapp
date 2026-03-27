package com.example.guiamapp.ui.profesor

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.ProfesorNivelDto
import com.example.guiamapp.data.repository.ProfesorRepository
import kotlinx.coroutines.launch

@Composable
fun ProfesorNivelesScreen(
    repo: ProfesorRepository,
    cursoId: Int,
    onOpenNivel: (Int) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    var niveles by remember { mutableStateOf<List<ProfesorNivelDto>>(emptyList()) }
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }

    LaunchedEffect(cursoId) {
        try {
            niveles = repo.getNiveles(cursoId)
        } catch (e: Exception) {
            error = "Error cargando niveles"
            snackbarHostState.showSnackbar("Error cargando niveles")
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
                text = "Niveles del curso",
                style = MaterialTheme.typography.headlineMedium
            )
            Spacer(Modifier.height(12.dp))
            if (loading) {
                CircularProgressIndicator()
                return@Column
            }

            error?.let {
                Text(it, color = MaterialTheme.colorScheme.error)
                return@Column
            }

            LazyColumn {
                items(niveles) { nivel ->
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .clickable {
                                onOpenNivel(nivel.id)
                            }
                    ) {
                        Column(Modifier.padding(16.dp)) {
                            Text(
                                text = nivel.name,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }
            }
        }
    }
}