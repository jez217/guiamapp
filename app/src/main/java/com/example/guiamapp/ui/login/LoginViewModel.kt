package com.example.guiamapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guiamapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading = _loading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error = _error.asStateFlow()

    /**
     * Llama al backend con correo+clave, guarda JWT/role en DataStore,
     * y devuelve el role para redirigir.
     */
    fun login(
        correo: String,
        clave: String,
        onSuccess: (String) -> Unit // role
    ) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            // Validación mínima de UI
            val email = correo.trim()
            val pass = clave.trim()
            if (email.isEmpty() || pass.isEmpty()) {
                _loading.value = false
                _error.value = "Ingrese correo y contraseña"
                return@launch
            }

            try {
                val role: String? = repository.login(email, pass)
                _loading.value = false

                if (role != null) {
                    onSuccess(role)
                } else {
                    _error.value = "Credenciales incorrectas"
                }
            } catch (e: Exception) {
                _loading.value = false
                _error.value = e.message ?: "Error inesperado"
            }
        }
    }
}