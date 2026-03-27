package com.example.guiamapp.data.remote.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ProfesorApi {

/**
 * GET /api/profesor/cursos
 */
@GET("/api/profesor/cursos")
suspend fun getCursos(): List<ProfesorCursoDto>

    /**
     * Niveles de un curso
     * GET /api/profesor/curso/{cursoId}/levels
     */
    @GET("/api/profesor/curso/{cursoId}/levels")
    suspend fun getNiveles(
        @Path("cursoId") cursoId: Int
    ): List<ProfesorNivelDto>

    /**
     * Contenido (archivos) de un nivel
     * GET /api/profesor/nivel/{folderLevelId}
     */
    @GET("/api/profesor/nivel/{folderLevelId}")
    suspend fun getContenidoNivel(
        @Path("folderLevelId") folderLevelId: Int
    ): ProfesorContenidoDto

    /**
     * Subir archivo (IMG / PDF / VIDEO)
     * POST multipart /api/profesor/upload
     */
    @Multipart
    @POST("/api/profesor/upload")
    suspend fun uploadFile(
        @Part file: MultipartBody.Part,
        @Part("folderLevelId") folderLevelId: RequestBody
    ): GenericResponse

    /**
     * Eliminar archivo
     * DELETE /api/profesor/file/{fileId}
     */
    @DELETE("/api/profesor/file/{fileId}")
    suspend fun deleteFile(
        @Path("fileId") fileId: Int
    ): GenericResponse
}