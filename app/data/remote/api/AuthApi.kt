package com.example.guiamapp.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

data class LoginRequest(
    val correo: String,
    val clave: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val role: String?
)

interface AuthApi {

    @POST("/api/user/login")
    suspend fun loginUser(
        @Body body: LoginRequest
    ): LoginResponse
}