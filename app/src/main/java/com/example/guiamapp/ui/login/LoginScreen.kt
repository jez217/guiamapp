package com.example.guiamapp.ui.login

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigate: (String) -> Unit
) {
    val loading by viewModel.loading.collectAsState()
    val error by viewModel.error.collectAsState()
    val role by viewModel.role.collectAsState() // 🔥 NUEVO

    var correo by remember { mutableStateOf("") }
    var clave by remember { mutableStateOf("") }

    // 🔥 AQUÍ ESTÁ LA MAGIA (NO dentro del botón)
    LaunchedEffect(role) {
        role?.let {
            onNavigate(it)
            viewModel.clearNavigation()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Iniciar Sesión",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(16.dp))

        OutlinedTextField(
            value = correo,
            onValueChange = { correo = it },
            label = { Text("Correo") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(8.dp))

        OutlinedTextField(
            value = clave,
            onValueChange = { clave = it },
            label = { Text("Contraseña") },
            visualTransformation = PasswordVisualTransformation(),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                viewModel.login(correo, clave) // 🔥 YA NO PASAS onNavigate
            },
            enabled = !loading,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(if (loading) "Ingresando..." else "Ingresar")
        }

        error?.let {
            Spacer(Modifier.height(12.dp))
            Text(text = it, color = MaterialTheme.colorScheme.error)
        }
    }
}