package com.example.guiamapp.data.remote.api

import retrofit2.http.GET
import retrofit2.http.Path

interface StudentApi {

    // Curso del estudiante según JWT (y datos mínimos)
    @GET("/api/student/cursos")
    suspend fun getCursos(): StudentCursosResponse

    // Niveles disponibles para un curso (filtrado por claim "app:level")
    @GET("/api/student/curso/{cursoId}/niveles")
    suspend fun getNiveles(@Path("cursoId") cursoId: Int): StudentNivelesResponse

    // Contenido del nivel: subcarpetas / archivos
    @GET("/api/student/nivel/{folderLevelId}")
    suspend fun getContenidoNivel(@Path("folderLevelId") folderLevelId: Int): StudentContenidoResponse
}