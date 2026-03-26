package com.example.guiamapp.data.remote.api

data class LoginRequest(
    val correo: String,
    val clave: String
)

data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val role: String?
)