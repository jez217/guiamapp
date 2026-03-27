@file:OptIn(androidx.media3.common.util.UnstableApi::class)

package com.example.guiamapp.ui.student

import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.datasource.DefaultHttpDataSource
import androidx.media3.datasource.cache.CacheDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.guiamapp.data.media.MediaCache

@androidx.annotation.OptIn(UnstableApi::class)
@Composable
fun VideoPlayerScreen(
    videoUrl: String,
    modifier: Modifier = Modifier,
    autoPlay: Boolean = true
) {
    val context = androidx.compose.ui.platform.LocalContext.current

    val cache = remember { MediaCache.get(context) }

    val httpFactory = remember { DefaultHttpDataSource.Factory() }
    val upstreamFactory = remember { DefaultDataSource.Factory(context, httpFactory) }

    val cacheFactory = remember {
        CacheDataSource.Factory()
            .setCache(cache)
            .setUpstreamDataSourceFactory(upstreamFactory)
            .setFlags(CacheDataSource.FLAG_IGNORE_CACHE_ON_ERROR)
    }

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .setMediaSourceFactory(
                androidx.media3.exoplayer.source.DefaultMediaSourceFactory(cacheFactory)
            )
            .build().apply {
                setMediaItem(MediaItem.fromUri(videoUrl))
                prepare()
                playWhenReady = autoPlay
            }
    }

    DisposableEffect(Unit) {
        onDispose { exoPlayer.release() }
    }

    AndroidView(
        factory = {
            PlayerView(it).apply {
                player = exoPlayer
                layoutParams = android.view.ViewGroup.LayoutParams(
                    MATCH_PARENT,
                    MATCH_PARENT
                )
                setShowBuffering(PlayerView.SHOW_BUFFERING_WHEN_PLAYING)
                setControllerAutoShow(true)
                setControllerShowTimeoutMs(3000)
            }
        },
        modifier = modifier
    )
}