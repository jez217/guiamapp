package com.example.guiamapp.ui.student.download

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.File

class FileDownloadWorker(
    appContext: Context,
    params: WorkerParameters
) : CoroutineWorker(appContext, params) {

    companion object {
        const val KEY_URL = "url"
        const val KEY_FILE_NAME = "fileName"
        const val KEY_MIME = "mime"
        const val CHANNEL_ID = "downloads"
        const val NOTIF_ID = 1001
    }

    private val client by lazy { OkHttpClient() }

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        val url = inputData.getString(KEY_URL) ?: return@withContext Result.failure()
        val fileName = inputData.getString(KEY_FILE_NAME) ?: "download.bin"

        createChannel()

        setForeground(createForegroundInfo(0, "Descargando $fileName"))

        val req = Request.Builder().url(url).build()
        val resp = client.newCall(req).execute()

        if (!resp.isSuccessful) {
            return@withContext Result.failure()
        }

        val body = resp.body ?: return@withContext Result.failure()
        val contentLength = body.contentLength()

        val dir = File(applicationContext.getExternalFilesDir(null), "downloads").apply { mkdirs() }
        val outFile = File(dir, fileName)

        body.byteStream().use { input ->
            outFile.outputStream().use { output ->
                val buffer = ByteArray(8 * 1024)
                var bytesRead: Int
                var totalRead = 0L
                while (true) {
                    bytesRead = input.read(buffer)
                    if (bytesRead == -1) break
                    output.write(buffer, 0, bytesRead)
                    totalRead += bytesRead
                    if (contentLength > 0) {
                        val progress = ((totalRead * 100) / contentLength).toInt()
                        setForeground(createForegroundInfo(progress, "Descargando $fileName"))
                    }
                }
                output.flush()
            }
        }

        // Notificación final
        val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        nm.notify(
            NOTIF_ID,
            NotificationCompat.Builder(applicationContext, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.stat_sys_download_done)
                .setContentTitle("Descarga completa")
                .setContentText(fileName)
                .setAutoCancel(true)
                .build()
        )

        Result.success(
            workDataOf(
                "path" to outFile.absolutePath,
                "fileName" to fileName
            )
        )
    }

    private fun createForegroundInfo(progress: Int, title: String): ForegroundInfo {
        val notification = NotificationCompat.Builder(applicationContext, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .setContentTitle(title)
            .setProgress(100, progress, progress == 0)
            .setOngoing(true)
            .build()

        return ForegroundInfo(NOTIF_ID, notification)
    }

    private fun createChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID, "Descargas", NotificationManager.IMPORTANCE_LOW
            )
            val nm = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            nm.createNotificationChannel(channel)
        }
    }
}