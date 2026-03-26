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

    fun login(correo: String, clave: String, onSuccess: (String) -> Unit) {


        println("LOGIN CLICK → correo=$correo clave=$clave")
        viewModelScope.launch {
            _loading.value = true
            _error.value = null
            val role = repository.login(correo, clave)
            _loading.value = false
            if (role != null) onSuccess(role) else _error.value = "Credenciales incorrectas"
        }
    }
}