package com.example.guiamapp.data.repository

import com.example.guiamapp.data.remote.api.ProfesorApi
import com.example.guiamapp.data.remote.api.ProfesorContenidoDto
import com.example.guiamapp.data.remote.api.ProfesorCursoDto
import com.example.guiamapp.data.remote.api.ProfesorNivelDto
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ProfesorRepository(
    private val api: ProfesorApi
) {

    /**
     * Cursos asignados al profesor
     * GET /api/profesor/cursos
     */
    suspend fun getCursos(): List<ProfesorCursoDto> =
        api.getCursos()

    /**
     * Niveles de un curso
     * GET /api/profesor/curso/{cursoId}/levels
     */
    suspend fun getNiveles(cursoId: Int): List<ProfesorNivelDto> =
        api.getNiveles(cursoId)

    /**
     * Contenido de un nivel (archivos)
     * GET /api/profesor/nivel/{folderLevelId}
     */
    suspend fun getContenido(folderLevelId: Int): ProfesorContenidoDto =
        api.getContenidoNivel(folderLevelId)

    /**
     * Subir archivo (IMG / PDF / VIDEO)
     * POST /api/profesor/upload (multipart)
     */
    suspend fun uploadFile(
        file: MultipartBody.Part,
        folderLevelId: RequestBody
    ) = api.uploadFile(file, folderLevelId)

    /**
     * Eliminar archivo
     * DELETE /api/profesor/file/{fileId}
     */
    suspend fun deleteFile(fileId: Int) =
        api.deleteFile(fileId)
}