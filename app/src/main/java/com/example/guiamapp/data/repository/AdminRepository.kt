package com.example.guiamapp.data.repository

import com.example.guiamapp.data.remote.api.*

class AdminRepository(
    private val api: AdminApi
) {
    // Dashboard
    suspend fun getDashboard() = api.getDashboard()

    // Base data
    suspend fun getRoles() = api.getRoles()
    suspend fun getLevels() = api.getLevels()
    suspend fun getCursos() = api.getCursos()

    // Users
    suspend fun getUsers() = api.getUsers()
    suspend fun getUserById(id: Int) = api.getUserById(id)
    suspend fun createUser(body: CreateUserRequest) = api.createUser(body)
    suspend fun updateUser(body: UpdateUserRequest) = api.updateUser(body)
    suspend fun deleteUser(id: Int) = api.deleteUser(id)

    // Assign
    suspend fun assignLevel(userId: Int, levelId: Int) =
        api.assignLevelToStudent(userId, levelId)
}