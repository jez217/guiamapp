package com.example.guiamapp.data.repository

import com.example.guiamapp.data.local.TokenStore
import com.example.guiamapp.data.remote.api.AuthApi
import com.example.guiamapp.data.remote.api.LoginRequest

class AuthRepository(
    private val authApi: AuthApi,
    private val tokenStore: TokenStore
) {

    /**
     * Intenta login por /api/user/login.
     * Si el backend solo permite admin por /api/admin/login, puedes
     * decidir según correo o probar ambos en orden.
     */
    suspend fun login(correo: String, clave: String): String? {
        val resp = authApi.loginUser(LoginRequest(correo, clave))
        if (resp.success && resp.token != null && resp.role != null) {
            tokenStore.save(resp.token, resp.role)
            return resp.role
        }
        // Si quieres fallback a admin:
        // val admin = authApi.loginAdmin(LoginRequest(correo, clave))
        // if (admin.success && admin.token != null && admin.role != null) {
        //     tokenStore.save(admin.token, admin.role)
        //     return admin.role
        // }
        return null
    }
}