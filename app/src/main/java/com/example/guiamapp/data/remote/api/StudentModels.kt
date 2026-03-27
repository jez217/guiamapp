package com.example.guiamapp.data.remote.api

// /api/student/cursos
data class StudentCursosResponse(
    val cursoId: Int?,
    val cursoNombre: String?,
    val niveles: List<StudentNivelDto>? = emptyList() // usado a veces en la misma respuesta
)

data class StudentNivelDto(
    val id: Int,                  // Id_Folders_level (o ID del nivel)
    val name: String,             // Name
    val id_level_reference: Int?  // nivel numérico permitido
)

// /api/student/curso/{cursoId}/niveles
data class StudentNivelesResponse(
    val cursoId: Int?,
    val cursoNombre: String?,
    val niveles: List<StudentNivelDto>
)

// /api/student/nivel/{folderLevelId}
data class StudentContenidoResponse(
    val nivel: Int?,
    val folderLevelId: Int,
    val folderNombre: String?,
    val folder: Any?, // si tu backend retorna un objeto carpeta; puedes tiparlo luego
    val files: List<StudentFileDto>?
)

data class StudentFileDto(
    val id: Int,
    val name: String,
    val filePath: String? // Ruta relativa: "uploads/VIDEO/xxxxx.mp4" etc.
)