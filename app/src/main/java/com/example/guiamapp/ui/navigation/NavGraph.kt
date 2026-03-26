package com.example.guiamapp.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

object Routes {
    const val Login = "login"
    const val AdminHome = "admin_home"
    const val ProfesorHome = "profesor_home"
    const val StudentHome = "student_home"
}

@Composable
fun AppNavGraph(
    nav: NavHostController,
    loginContent: @Composable () -> Unit,
    adminContent: @Composable () -> Unit,
    profesorContent: @Composable () -> Unit,
    studentContent: @Composable () -> Unit
) {
    NavHost(navController = nav, startDestination = Routes.Login) {
        composable(Routes.Login) { loginContent() }
        composable(Routes.AdminHome) { adminContent() }
        composable(Routes.ProfesorHome) { profesorContent() }
        composable(Routes.StudentHome) { studentContent() }
    }
}