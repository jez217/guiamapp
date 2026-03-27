package com.example.guiamapp.data.repository

import com.example.guiamapp.data.remote.api.StudentApi
import com.example.guiamapp.data.remote.api.StudentContenidoResponse
import com.example.guiamapp.data.remote.api.StudentCursosResponse
import com.example.guiamapp.data.remote.api.StudentNivelesResponse

class StudentRepository(
    private val api: StudentApi
) {
    suspend fun getCursoActual(): StudentCursosResponse =
        api.getCursos()

    suspend fun getNiveles(cursoId: Int): StudentNivelesResponse =
        api.getNiveles(cursoId)

    suspend fun getContenidoNivel(folderLevelId: Int): StudentContenidoResponse =
        api.getContenidoNivel(folderLevelId)
}