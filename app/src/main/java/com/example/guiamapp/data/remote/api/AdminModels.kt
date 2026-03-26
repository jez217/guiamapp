package com.example.guiamapp.data.remote.api

// ---------- RESPUESTAS GENERALES ----------

data class GenericResponse(
    val success: Boolean,
    val message: String?
)

// ---------- DASHBOARD ----------

data class AdminDashboardResponse(
    val totalUsers: Int,
    val totalCursos: Int,
    val totalLevels: Int
)

// ---------- ROLES ----------

data class RoleDto(
    val id: Int,
    val name: String
)

// ---------- LEVELS ----------

data class LevelDto(
    val id: Int,
    val name: String
)

// ---------- CURSOS ----------

data class CursoDto(
    val id: Int,
    val name: String
)

// ---------- USERS ----------

data class UserDto(
    val id: Int,
    val name: String,
    val apellido: String,
    val correo: String,
    val role: String,
    val status: Int,
    val levelId: Int?,
    val cursoId: Int?
)

// ---------- REQUESTS ----------

data class CreateUserRequest(
    val name: String,
    val apellido: String,
    val correo: String,
    val clave: String,
    val idRol: Int,
    val idStatus: Int
)

data class UpdateUserRequest(
    val idUser: Int,
    val name: String,
    val apellido: String,
    val correo: String,
    val idRol: Int,
    val idStatus: Int
)
