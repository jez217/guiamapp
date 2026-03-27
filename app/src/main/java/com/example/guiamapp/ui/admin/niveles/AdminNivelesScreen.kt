package com.example.guiamapp.ui.admin.niveles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.LevelDto
import com.example.guiamapp.data.remote.api.UserDto
import com.example.guiamapp.data.repository.AdminRepository
import kotlinx.coroutines.launch

@Composable
fun AdminNivelesScreen(repo: AdminRepository) {

    val scope = rememberCoroutineScope()

    var niveles by remember { mutableStateOf<List<LevelDto>>(emptyList()) }
    var estudiantes by remember { mutableStateOf<List<UserDto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var showAssign by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        try {
            niveles = repo.getLevels()
            estudiantes = repo.getUsers().filter { it.role.equals("Student", true) }
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(Modifier.padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Niveles", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { showAssign = true }) {
                Text("Asignar nivel")
            }
        }

        Spacer(Modifier.height(8.dp))

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(niveles) { nivel ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Text(
                        "${nivel.id} - ${nivel.name}",
                        Modifier.padding(16.dp)
                    )
                }
            }
        }
    }

    if (showAssign) {
        AssignLevelDialog(
            estudiantes = estudiantes,
            niveles = niveles,
            onDismiss = { showAssign = false },
            onConfirm = { userId: Int, levelId: Int ->
                scope.launch {
                    try {
                        repo.assignLevel(userId, levelId)
                        showAssign = false
                    } catch (e: Exception) {
                        error = e.message
                    }
                }
            }
        )
    }
}

/* ---------- DIALOG ---------- */

@Composable
private fun AssignLevelDialog(
    estudiantes: List<UserDto>,
    niveles: List<LevelDto>,
    onDismiss: () -> Unit,
    onConfirm: (Int, Int) -> Unit
) {

    var selectedStudentId by remember { mutableStateOf<Int?>(null) }
    var selectedLevelId by remember { mutableStateOf<Int?>(null) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Asignar nivel a estudiante") },
        text = {
            Column {
                Text("Estudiante")
                SimpleSelector(
                    items = estudiantes.map { it.id to "${it.name} ${it.apellido}" },
                    onSelect = { selectedStudentId = it }
                )

                Spacer(Modifier.height(12.dp))

                Text("Nivel")
                SimpleSelector(
                    items = niveles.map { it.id to it.name },
                    onSelect = { selectedLevelId = it }
                )
            }
        },
        confirmButton = {
            Button(
                enabled = selectedStudentId != null && selectedLevelId != null,
                onClick = {
                    onConfirm(selectedStudentId!!, selectedLevelId!!)
                }
            ) {
                Text("Asignar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancelar")
            }
        }
    )
}

/* ---------- SELECTOR SIMPLE ---------- */

@Composable
private fun SimpleSelector(
    items: List<Pair<Int, String>>,
    onSelect: (Int) -> Unit
) {
    var index by remember { mutableStateOf(0) }

    Column {
        Text(items.getOrNull(index)?.second ?: "")
        Spacer(Modifier.height(6.dp))

        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                enabled = index > 0,
                onClick = {
                    index--
                    onSelect(items[index].first)
                }
            ) { Text("◀") }

            Button(
                enabled = index < items.size - 1,
                onClick = {
                    index++
                    onSelect(items[index].first)
                }
            ) { Text("▶") }
        }
    }
}