package com.example.guiamapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.rememberNavController
import com.example.guiamapp.data.local.TokenStore
import com.example.guiamapp.data.remote.ApiClient
import com.example.guiamapp.data.remote.api.AdminApi
import com.example.guiamapp.data.remote.api.AuthApi
import com.example.guiamapp.data.remote.api.StudentApi
import com.example.guiamapp.data.repository.AdminRepository
import com.example.guiamapp.data.repository.AuthRepository
import com.example.guiamapp.data.repository.StudentRepository
import com.example.guiamapp.ui.common.DummyScreen
import com.example.guiamapp.ui.login.LoginScreen
import com.example.guiamapp.ui.login.LoginViewModel
import com.example.guiamapp.ui.navigation.AppNavGraph
import com.example.guiamapp.ui.navigation.Routes
import com.example.guiamapp.ui.student.StudentContenidoScreen
import com.example.guiamapp.ui.student.StudentHomeScreen
import com.example.guiamapp.ui.student.StudentNivelesScreen
import com.example.guiamapp.ui.theme.GuiamAppTheme
import retrofit2.create

class MainActivity : ComponentActivity() {

    private fun provideLoginViewModel(
        authRepo: AuthRepository
    ): LoginViewModel {
        return ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(authRepo) as T
                }
            }
        )[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // 🔧 Instancias compartidas para toda la Activity (sin DI frameworks)
        val tokenStore = TokenStore(applicationContext)
        val retrofit = ApiClient.create(tokenStore)

        // APIs
        val authApi = retrofit.create<AuthApi>()
        val studentApi = retrofit.create<StudentApi>()
        val adminApi = retrofit.create<AdminApi>()

        // Repos
        val authRepo = AuthRepository(authApi, tokenStore)
        val studentRepo = StudentRepository(studentApi)
        val adminRepo = AdminRepository(adminApi)

        // VM
        val loginVm = provideLoginViewModel(authRepo)

        setContent {
            GuiamAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()

                    AppNavGraph(
                        nav = nav,
                        loginContent = {
                            LoginScreen(
                                viewModel = loginVm,
                                onNavigate = { role ->
                                    when (role) {
                                        "Admin" -> nav.navigate(Routes.AdminHome) {
                                            popUpTo(Routes.Login) { inclusive = true }
                                        }
                                        "Profesor" -> nav.navigate(Routes.ProfesorHome) {
                                            popUpTo(Routes.Login) { inclusive = true }
                                        }
                                        "Student" -> nav.navigate(Routes.StudentHome) {
                                            popUpTo(Routes.Login) { inclusive = true }
                                        }
                                        else -> {
                                            // Podrías mostrar un mensaje o telemetría
                                        }
                                    }
                                }
                            )
                        },
                        profesorContent = { DummyScreen("Panel Profesor") },

                        adminContent = { com.example.guiamapp.ui.admin.AdminHomeScreen(adminRepo) },

                        // Student
                        studentHome = {
                            StudentHomeScreen(
                                repository = studentRepo,
                                onVerNiveles = { cursoId ->
                                    nav.navigate("student_niveles/$cursoId")
                                }
                            )
                        },
                        studentNiveles = { cursoId ->
                            StudentNivelesScreen(
                                repository = studentRepo,
                                cursoId = cursoId,
                                onOpenNivel = { folderLevelId ->
                                    nav.navigate("student_contenido/$folderLevelId")
                                }
                            )
                        },
// Dentro de AppNavGraph(...)
                        studentContenido = { folderLevelId ->
                            StudentContenidoScreen(
                                repository = studentRepo,
                                folderLevelId = folderLevelId,
                                navController = nav
                            )
                        }
                    )
                }
            }
        }
    }
}