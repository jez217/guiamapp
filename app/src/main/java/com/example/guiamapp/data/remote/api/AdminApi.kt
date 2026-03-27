package com.example.guiamapp.data.remote.api

import retrofit2.http.*

interface AdminApi {

    // Dashboard
    @GET("/api/admin/dashboard")
    suspend fun getDashboard(): AdminDashboardResponse

    // Roles / Levels / Cursos
    @GET("/api/admin/roles")
    suspend fun getRoles(): List<RoleDto>

    @GET("/api/admin/levels")
    suspend fun getLevels(): List<LevelDto>

    @GET("/api/admin/cursos")
    suspend fun getCursos(): List<CursoDto>

    // Users
    @GET("/api/admin/users")
    suspend fun getUsers(): List<UserDto>

    @GET("/api/admin/users/{id}")
    suspend fun getUserById(@Path("id") id: Int): UserDto

    @POST("/api/admin/users")
    suspend fun createUser(@Body body: CreateUserRequest): GenericResponse

    @PUT("/api/admin/users")
    suspend fun updateUser(@Body body: UpdateUserRequest): GenericResponse

    @DELETE("/api/admin/users/{id}")
    suspend fun deleteUser(@Path("id") id: Int): GenericResponse

    // Asignar nivel a estudiante
    @POST("/api/admin/users/student/{userId}/levels/{levelId}")
    suspend fun assignLevelToStudent(
        @Path("userId") userId: Int,
        @Path("levelId") levelId: Int
    ): GenericResponse
}