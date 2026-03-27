package com.example.guiamapp.ui.login


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guiamapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository   // ✅ AQUÍ ESTABA EL ERROR
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun login(
        correo: String,
        clave: String,
        onNavigate: (String) -> Unit
    ) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val role: String? = repository.login(correo, clave)

                if (role != null) {
                    onNavigate(role)   // ✅ AHORA SÍ NAVEGA
                } else {
                    _error.value = "Credenciales inválidas"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Error inesperado"
            } finally {
                _loading.value = false
            }
        }
    }
}
