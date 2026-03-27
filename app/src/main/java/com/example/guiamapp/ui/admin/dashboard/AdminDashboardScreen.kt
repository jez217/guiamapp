package com.example.guiamapp.ui.admin.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.guiamapp.data.repository.AdminRepository

@Composable
fun AdminDashboardScreen(
    repo: AdminRepository,
    modifier: Modifier = Modifier
) {
    var loading by remember { mutableStateOf(true) }
    var error by remember { mutableStateOf<String?>(null) }
    var totalUsers by remember { mutableStateOf(0) }
    var totalCursos by remember { mutableStateOf(0) }
    var totalLevels by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        try {
            val d = repo.getDashboard()
            totalUsers = d.totalUsers
            totalCursos = d.totalCursos
            totalLevels = d.totalLevels
        } catch (e: Exception) {
            error = e.message
        } finally {
            loading = false
        }
    }

    Column(modifier.padding(16.dp)) {
        Text("Dashboard", style = MaterialTheme.typography.headlineMedium)
        Spacer(Modifier.height(12.dp))
        if (loading) { CircularProgressIndicator(); return@Column }
        error?.let { Text("Error: $it", color = MaterialTheme.colorScheme.error); return@Column }

        Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
            Column(Modifier.padding(16.dp)) {
                Text("Usuarios: $totalUsers")
                Text("Cursos: $totalCursos")
                Text("Niveles: $totalLevels")
            }
        }
    }
}