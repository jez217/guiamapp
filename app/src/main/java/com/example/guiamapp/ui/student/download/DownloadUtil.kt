package com.example.guiamapp.ui.student.download

import android.content.Context
import androidx.work.*
import java.util.concurrent.TimeUnit

object DownloadUtil {

    fun enqueueDownload(
        context: Context,
        url: String,
        fileName: String,
        mime: String? = null
    ) {
        val data = workDataOf(
            FileDownloadWorker.KEY_URL to url,
            FileDownloadWorker.KEY_FILE_NAME to fileName,
            FileDownloadWorker.KEY_MIME to (mime ?: "*/*")
        )

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val request = OneTimeWorkRequestBuilder<FileDownloadWorker>()
            .setInputData(data)
            .setConstraints(constraints)
            .setBackoffCriteria(
                BackoffPolicy.LINEAR,
                10,
                TimeUnit.SECONDS
            )
            .build()

        WorkManager.getInstance(context).enqueue(request)
    }
}