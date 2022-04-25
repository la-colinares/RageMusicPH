package com.lacolinares.ragemusicph.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.*
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.lacolinares.ragemusicph.extensions.customComposable
import com.lacolinares.ragemusicph.presentation.ui.screens.SplashScreen
import com.lacolinares.ragemusicph.presentation.ui.screens.main.MainScreen


@ExperimentalAnimationApi
@Composable
fun NavGraph(exoPlayer: ExoPlayer) {
    val navController = rememberAnimatedNavController()

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