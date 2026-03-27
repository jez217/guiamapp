package com.example.guiamapp.data.repository

import com.example.guiamapp.data.local.TokenStore
import com.example.guiamapp.data.remote.api.AuthApi
import com.example.guiamapp.data.remote.api.LoginRequest

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) {
    suspend fun login(correo: String, clave: String): String? {
        val resp = authApi.loginUser(LoginRequest(correo, clave))
        if (resp.success && resp.token != null && resp.role != null) {
            tokenStore.save(resp.token, resp.role) // <-- aquí guardas JWT + role
            return resp.role
        }
        return null
    }
}