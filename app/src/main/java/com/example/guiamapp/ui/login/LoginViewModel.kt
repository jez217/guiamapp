package com.example.guiamapp.ui.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.guiamapp.data.repository.AuthRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
class LoginViewModel(
    private val repository: AuthRepository
) : ViewModel() {

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    // 🔥 NUEVO: estado de navegación
    private val _role = MutableStateFlow<String?>(null)
    val role: StateFlow<String?> = _role

    fun login(correo: String, clave: String) {
        viewModelScope.launch {
            _loading.value = true
            _error.value = null

            try {
                val result = repository.login(correo, clave)

                if (result != null) {
                    _role.value = result   // 🔥 SOLO SETEA ESTADO
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

    fun clearNavigation() {
        _role.value = null
    }
}