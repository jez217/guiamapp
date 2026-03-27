@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.example.guiamapp.data.media

import android.content.Context
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import java.io.File

@UnstableApi
object MediaCache {

    @Volatile
    private var simpleCache: Cache? = null

    fun get(context: Context): Cache {
        return simpleCache ?: synchronized(this) {
            simpleCache ?: buildCache(context).also { simpleCache = it }
        }
    }

    @androidx.annotation.OptIn(UnstableApi::class)
    private fun buildCache(context: Context): Cache {
        val cacheDir = File(context.filesDir, "media_cache")
        return SimpleCache(
            cacheDir,
            LeastRecentlyUsedCacheEvictor(256L * 1024L * 1024L)
        )
    }
}