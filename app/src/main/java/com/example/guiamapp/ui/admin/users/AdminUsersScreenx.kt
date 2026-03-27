package com.example.guiamapp.ui.admin.users

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.remote.api.CreateUserRequest
import com.example.guiamapp.data.remote.api.UpdateUserRequest
import com.example.guiamapp.data.remote.api.UserDto
import com.example.guiamapp.data.repository.AdminRepository
import kotlinx.coroutines.launch

@Composable
fun AdminUsersScreen(repo: AdminRepository) {

    val scope = rememberCoroutineScope()

    var users by remember { mutableStateOf<List<UserDto>>(emptyList()) }
    var error by remember { mutableStateOf<String?>(null) }
    var showCreate by remember { mutableStateOf(false) }
    var editUser by remember { mutableStateOf<UserDto?>(null) }

    LaunchedEffect(Unit) {
        try {
            users = repo.getUsers()
        } catch (e: Exception) {
            error = e.message
        }
    }

    Column(Modifier.padding(16.dp)) {
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Usuarios", style = MaterialTheme.typography.headlineSmall)
            Button(onClick = { showCreate = true }) {
                Text("Crear")
            }
        }

        Spacer(Modifier.height(8.dp))

        error?.let {
            Text(it, color = MaterialTheme.colorScheme.error)
        }

        LazyColumn {
            items(users) { user ->
                Card(
                    Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${user.name} ${user.apellido}")
                        Text(user.correo)

                        Spacer(Modifier.height(6.dp))

                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Button(onClick = { editUser = user }) {
                                Text("Editar")
                            }

                            Button(
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                                ),
                                onClick = {
                                    scope.launch {
                                        try {
                                            repo.deleteUser(user.id)
                                            users = repo.getUsers()
                                        } catch (e: Exception) {
                                            error = e.message
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

    if (showCreate) {
        UserFormDialog(
            title = "Crear usuario",
            onDismiss = { showCreate = false },
            onSubmit = { name, apellido, correo, clave, rol, status ->
                scope.launch {
                    try {
                        repo.createUser(
                            CreateUserRequest(
                                name, apellido, correo, clave, rol, status
                            )
                        )
                        users = repo.getUsers()
                        showCreate = false
                    } catch (e: Exception) {
                        error = e.message
                    }
                }
            }
        )
    }

    editUser?.let { u ->
        UserFormDialog(
            title = "Editar usuario",
            initialName = u.name,
            initialApellido = u.apellido,
            initialCorreo = u.correo,
            hidePassword = true,
            onDismiss = { editUser = null },
            onSubmit = { name, apellido, correo, _, rol, status ->
                scope.launch {
                    try {
                        repo.updateUser(
                            UpdateUserRequest(
                                idUser = u.id,
                                name = name,
                                apellido = apellido,
                                correo = correo,
                                idRol = rol,
                                idStatus = status
                            )
                        )
                        users = repo.getUsers()
                        editUser = null
                    } catch (e: Exception) {
                        error = e.message
                    }
                }
            }
        )
    }
}

/* ---------- FORM ---------- */

@Composable
private fun UserFormDialog(
    title: String,
    initialName: String = "",
    initialApellido: String = "",
    initialCorreo: String = "",
    hidePassword: Boolean = false,
    onDismiss: () -> Unit,
    onSubmit: (String, String, String, String, Int, Int) -> Unit
) {

    var name by remember { mutableStateOf(initialName) }
    var apellido by remember { mutableStateOf(initialApellido) }
    var correo by remember { mutableStateOf(initialCorreo) }
    var clave by remember { mutableStateOf("") }
    var rol by remember { mutableStateOf(3) }
    var status by remember { mutableStateOf(1) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(title) },
        text = {
            Column {
                OutlinedTextField(name, { name = it }, label = { Text("Nombre") })
                OutlinedTextField(apellido, { apellido = it }, label = { Text("Apellido") })
                OutlinedTextField(correo, { correo = it }, label = { Text("Correo") })
                if (!hidePassword) {
                    OutlinedTextField(clave, { clave = it }, label = { Text("Contraseña") })
                }
                OutlinedTextField(rol.toString(), { it.toIntOrNull()?.let { r -> rol = r } }, label = { Text("IdRol") })
                OutlinedTextField(status.toString(), { it.toIntOrNull()?.let { s -> status = s } }, label = { Text("Status") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSubmit(name.trim(), apellido.trim(), correo.trim(), clave.trim(), rol, status)
            }) {
                Text("Guardar")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancelar") }
        }
    )
}