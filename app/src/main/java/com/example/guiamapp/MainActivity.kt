package com.example.guiamapp
import com.example.guiamapp.ui.common.DummyScreen
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.guiamapp.data.local.TokenStore
import com.example.guiamapp.data.remote.ApiClient
import com.example.guiamapp.data.remote.api.AuthApi
import com.example.guiamapp.data.repository.AuthRepository
import com.example.guiamapp.ui.login.LoginScreen
import com.example.guiamapp.ui.login.LoginViewModel
import com.example.guiamapp.ui.navigation.AppNavGraph
import com.example.guiamapp.ui.navigation.Routes
import com.example.guiamapp.ui.theme.GuiamAppTheme
import retrofit2.create

class MainActivity : ComponentActivity() {

    private fun provideLoginViewModel(): LoginViewModel {
        val tokenStore = TokenStore(applicationContext)
        val retrofit = ApiClient.create(tokenStore)
        val authApi = retrofit.create<AuthApi>()
        val repo = AuthRepository(authApi, tokenStore)
        return ViewModelProvider(
            this,
            object : ViewModelProvider.Factory {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return LoginViewModel(repo) as T
                }
            }
        )[LoginViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val loginVm = provideLoginViewModel()

        setContent {
            GuiamAppTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val nav = rememberNavController()

                    // MainActivity.kt (dentro de setContent { ... })
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
                                    }
                                }
                            )
                        },
                        // ⬇️ Usa el DummyScreen mientras armamos las pantallas reales
                        adminContent = { DummyScreen("Panel Admin") },
                        profesorContent = { DummyScreen("Panel Profesor") },
                        studentContent = { DummyScreen("Panel Estudiante") }
                    )

                }
            }
        }
    }
}