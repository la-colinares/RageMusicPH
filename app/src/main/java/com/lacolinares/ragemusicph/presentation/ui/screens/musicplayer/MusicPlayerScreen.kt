package com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.media3.exoplayer.ExoPlayer
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.extensions.openPlayStoreApp
import com.lacolinares.ragemusicph.presentation.MainViewModel
import com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.components.BottomContent
import com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.components.MidContent
import com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.components.TopContent
import com.lacolinares.ragemusicph.presentation.ui.theme.Background
import com.lacolinares.ragemusicph.presentation.ui.theme.Red


@Composable
fun MusicPlayerScreen(
    headerTitle: String,
    description: String,
    logo: Int,
    activeMusic: ActiveMusic,
    exoPlayer: ExoPlayer,
    viewModel: MainViewModel,
) {
    val context = LocalContext.current
    val foregroundServiceStopped = viewModel.foregroundServiceStopped.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 48.dp)
        ) {
            TopContent(headerTitle = headerTitle, description, logo)
            MidContent(
                playEqualizer = activeMusic.isPlaying,
                musicTitle = activeMusic.title,
                musicArtist = activeMusic.artist,
                onShareApp = {
                    context.openPlayStoreApp()
                }
            )
            if (activeMusic.title.isNotEmpty()) {
                BottomContent(
                    isPlay = activeMusic.isPlaying,
                    onPlay = {
                        if (foregroundServiceStopped.value) {
                            viewModel.setForegroundServiceStopped(false)
                        } else {
                            exoPlayer.play()
                        }
                    },
                    onPause = {
                        exoPlayer.pause()
                    }
                )
            } else {
                Loader()
            }
        }
    }
}

@Composable
fun Loader() {
    Space(height = 24)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Red)
    }
}

