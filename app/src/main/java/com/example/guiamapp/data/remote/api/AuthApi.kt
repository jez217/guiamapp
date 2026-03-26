package com.example.guiamapp.data.remote.api

import retrofit2.http.Body
import retrofit2.http.POST

/**
 * Login para usuarios NO admin
 * (Profesor y Student)
 */
interface AuthApi {

    @POST("/api/user/login")
    suspend fun loginUser(
        @Body body: LoginRequest
    ): LoginResponse

    /**
     * Login exclusivo para Admin
     */
    @POST("/api/admin/login")
    suspend fun loginAdmin(
        @Body body: LoginRequest
    ): LoginResponse
}