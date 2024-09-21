package com.lacolinares.ragemusicph.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.lacolinares.ragemusicph.extensions.customComposable
import com.lacolinares.ragemusicph.extensions.getActivity
import com.lacolinares.ragemusicph.extensions.runMusicService
import com.lacolinares.ragemusicph.presentation.MainActivity
import com.lacolinares.ragemusicph.presentation.MainViewModel
import com.lacolinares.ragemusicph.presentation.ui.screens.main.MainScreen
import com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.ActiveMusic
import com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.MusicPlayerScreen
import com.lacolinares.ragemusicph.presentation.ui.screens.splash.SplashScreen

@ExperimentalAnimationApi
@Composable
fun NavGraph(viewModel: MainViewModel) {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route
    ) {
        customComposable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }
        customComposable(Screen.MainScreen.route) {
            MainScreen(mainNavController = navController, viewModel = viewModel)
        }
        customComposable(Screen.MusicPlayerScreen.route) {
            val selectedCategory = viewModel.selectedMusicCategory.collectAsStateWithLifecycle().value
            val isAudioPlaying: MutableState<Boolean> = remember { mutableStateOf(false) }
            val audioTitle: MutableState<String> = remember { mutableStateOf("") }
            val audioArtist: MutableState<String> = remember { mutableStateOf("") }

            val exoPlayer: MutableState<ExoPlayer?> = remember { mutableStateOf(null) }

            val context = LocalContext.current
            val activity = context.getActivity<MainActivity>()
            LaunchedEffect(true) {
                context.runMusicService(
                    onStarted = { player ->
                       val mediaItem = MediaItem.fromUri(selectedCategory.url)
                        player.apply {
                            setMediaItem(mediaItem)
                            prepare()
                            playWhenReady = true
                            addListener(object : Player.Listener {
                                override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                                    super.onMediaMetadataChanged(mediaMetadata)
                                    if (mediaMetadata.title != null) {
                                        val data = mediaMetadata.title.toString().split("-")
                                        audioTitle.value = data.last().trim()
                                        audioArtist.value = data.first().trim()
                                    }else{
                                        audioTitle.value = selectedCategory.title
                                        audioArtist.value = selectedCategory.description
                                    }
                                }

                                override fun onIsPlayingChanged(isPlaying: Boolean) {
                                    super.onIsPlayingChanged(isPlaying)
                                    isAudioPlaying.value = isPlaying
                                }
                            })
                        }
                        exoPlayer.value = player
                    },
                    onStop = {
                        activity?.unbindService(it)
                    }
                )
            }
            exoPlayer.value?.let { player ->
                val activeMusic = ActiveMusic(title = audioTitle.value, artist = audioArtist.value, isPlaying = isAudioPlaying.value)
                MusicPlayerScreen(
                    headerTitle = selectedCategory.title,
                    description = selectedCategory.description,
                    logo = selectedCategory.logo,
                    activeMusic = activeMusic,
                    exoPlayer = player,
                    viewModel = viewModel,
                )
            }
        }
    }
}