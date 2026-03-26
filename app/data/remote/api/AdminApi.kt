package com.example.guiamapp.data.remote.api

import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

data class LoginRequest(val correo: String, val clave: String)
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val userId: Int?,
    val name: String?,
    val role: String?
)

interface AdminApi {
    @POST("/api/admin/login")
    suspend fun login(@Body body: LoginRequest): LoginResponse

    @POST("/api/admin/logout")
    suspend fun logout(): Map<String, Any?>
}