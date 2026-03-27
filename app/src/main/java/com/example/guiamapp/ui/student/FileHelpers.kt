package com.example.guiamapp.ui.student

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.core.content.FileProvider
import com.example.guiamapp.BuildConfig
import java.io.File

object FileHelpers {

    enum class FileKind { IMAGE, VIDEO, PDF, OTHER }

    fun detectKind(filePath: String?): FileKind {
        if (filePath.isNullOrBlank()) return FileKind.OTHER
        return when {
            filePath.endsWith(".jpg", true) || filePath.endsWith(".jpeg", true)
                    || filePath.endsWith(".png", true) || filePath.endsWith(".webp", true) -> FileKind.IMAGE
            filePath.endsWith(".mp4", true) || filePath.endsWith(".mkv", true) || filePath.endsWith(".webm", true) -> FileKind.VIDEO
            filePath.endsWith(".pdf", true) -> FileKind.PDF
            else -> FileKind.OTHER
        }
    }

    fun absoluteUrl(relativePath: String?): String? {
        if (relativePath.isNullOrBlank()) return null
        return BuildConfig.BASE_URL.trimEnd('/') + "/" + relativePath.trimStart('/')
    }

    fun cacheFileIfExists(context: Context, fileName: String): File? {
        val dir = File(context.getExternalFilesDir(null), "downloads")
        val f = File(dir, fileName)
        return if (f.exists()) f else null
    }

    fun shareFile(context: Context, file: File, mime: String = "*/*") {
        val uri: Uri = FileProvider.getUriForFile(
            context,
            "${context.packageName}.fileprovider",
            file
        )
        val intent = Intent(Intent.ACTION_SEND).apply {
            type = mime
            putExtra(Intent.EXTRA_STREAM, uri)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        context.startActivity(Intent.createChooser(intent, "Compartir archivo"))
    }

    fun guessMime(name: String): String =
        when {
            name.endsWith(".pdf", true) -> "application/pdf"
            name.endsWith(".jpg", true) || name.endsWith(".jpeg", true) -> "image/jpeg"
            name.endsWith(".png", true) -> "image/png"
            name.endsWith(".mp4", true) -> "video/mp4"
            else -> "*/*"
        }
}