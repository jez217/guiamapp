package com.example.guiamapp.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import com.example.guiamapp.data.repository.AdminRepository
import com.example.guiamapp.ui.admin.niveles.AdminNivelesScreen
import com.example.guiamapp.ui.admin.users.AdminUsersScreen
import com.example.guiamapp.ui.admin.cursos.AdminCursosScreen
import com.example.guiamapp.ui.admin.dashboard.AdminDashboardScreen

@Composable
fun AdminHomeScreen(
    repo: AdminRepository
) {
    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("Dashboard", "Usuarios", "Cursos", "Niveles")

    Scaffold(
        topBar = {
            TabRow(selectedTabIndex = tab) {
                tabs.forEachIndexed { i, t ->
                    Tab(
                        selected = tab == i,
                        onClick = { tab = i },
                        text = { Text(t) }
                    )
                }
            }
        }
    ) { paddingValues ->   // ✅ USAR ESTE PADDING

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues) // ✅ AQUÍ SE APLICA
        ) {
            when (tab) {
                0 -> AdminDashboardScreen(repo, modifier = Modifier.fillMaxSize())
                1 -> AdminUsersScreen(repo)
                2 -> AdminCursosScreen(repo)
                3 -> AdminNivelesScreen(repo)
            }
        }
    }
}
