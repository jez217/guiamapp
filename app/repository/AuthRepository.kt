package com.example.guiamapp.data.repository

import com.example.guiamapp.data.local.TokenStore
import com.example.guiamapp.data.remote.api.AuthApi
import com.example.guiamapp.data.remote.api.LoginRequest

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) {

    // ✅ DEVUELVE STRING? (ROL)
    suspend fun login(
        correo: String,
        clave: String
    ): String? {

        val response = authApi.loginUser(
            LoginRequest(correo, clave)
        )

        return if (
            response.success &&
            response.token != null &&
            response.role != null
        ) {
            tokenStore.save(response.token, response.role)
            response.role              // ✅ String
        } else {
            null
        }
    }
}
