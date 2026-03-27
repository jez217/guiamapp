package com.example.guiamapp.data.remote.api

data class ProfesorCursoDto(
    val id: Int,
    val name: String
)

data class ProfesorNivelDto(
    val id: Int,
    val name: String
)

data class ProfesorFileDto(
    val id: Int,
    val name: String,
    val filePath: String
)

data class ProfesorContenidoDto(
    val folderId: Int,
    val folderName: String,
    val files: List<ProfesorFileDto>
)