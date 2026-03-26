package com.example.guiamapp.data.remote.api

/**
 * Request para login
 * Se usa tanto para Admin como para User
 */
data class LoginRequest(
    val correo: String,
    val clave: String
)

/**
 * Response de login
 * Coincide con tu API .NET:
 * {
 *   success: boolean,
 *   token: string,
 *   role: string
 * }
 */
data class LoginResponse(
    val success: Boolean,
    val token: String?,
    val role: String?
)