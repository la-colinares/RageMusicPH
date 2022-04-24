package com.lacolinares.ragemusicph.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.lacolinares.ragemusicph.extensions.customComposable
import com.lacolinares.ragemusicph.presentation.ui.screens.SplashScreen
import com.lacolinares.ragemusicph.presentation.ui.screens.main.MainScreen


@ExperimentalAnimationApi
@Composable
fun NavGraph() {
    val navController = rememberAnimatedNavController()
    val context = LocalContext.current

    val url = "https://node-15.zeno.fm/yu9dryfs7x8uv.mp3?rj-ttl=5&rj-tok=AAABgFn8jHMAJlhQjlgTTNm8Iw"
    val exoPlayer = ExoPlayer.Builder(context).build()
    val mediaItem = MediaItem.fromUri(url)
    exoPlayer.setMediaItem(mediaItem)

    AnimatedNavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        customComposable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        customComposable(Screen.MainScreen.route) {
            val isAudioPlaying: MutableState<Boolean> = remember { mutableStateOf(false) }
            val audioTitle: MutableState<String> = remember { mutableStateOf("") }
            val audioArtist: MutableState<String> = remember { mutableStateOf("") }

            LaunchedEffect(Unit) {
                exoPlayer.prepare()
                exoPlayer.play()
                exoPlayer.addListener(object : Player.Listener {
                    override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                        super.onMediaMetadataChanged(mediaMetadata)
                        if (mediaMetadata.title != null) {
                            val data = mediaMetadata.title.toString().split("-")
                            audioTitle.value = data.last().trim()
                            audioArtist.value = data.first().trim()
                        }
                    }

                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        super.onIsPlayingChanged(isPlaying)
                        isAudioPlaying.value = isPlaying
                    }
                })
            }
            MainScreen(
                exoPlayer = exoPlayer,
                isAudioPlaying = isAudioPlaying.value,
                audioTitle = audioTitle.value,
                audioArtist = audioArtist.value,
            )
        }
    }
}