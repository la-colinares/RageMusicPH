package com.lacolinares.ragemusicph.presentation.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.ExoPlayer
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.BottomContent
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.MidContent
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.TopContent
import com.lacolinares.ragemusicph.presentation.ui.theme.Background


@Composable
fun MainScreen(
    exoPlayer : ExoPlayer,
    isAudioPlaying: Boolean,
    audioTitle: String,
    audioArtist: String,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Background)
    ) {
        Column(modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 48.dp)
        ) {
            TopContent(headerTitle = "Rage Music PH")
            MidContent(
                playEqualizer = isAudioPlaying,
                musicTitle = audioTitle,
                musicArtist = audioArtist,
                onShareApp = {
                    //TODO: share app url link
                }
            )
            BottomContent(
                isPlay = isAudioPlaying,
                onPlay = {
                    exoPlayer.play()
                },
                onPause = {
                    exoPlayer.pause()
                }
            )
        }
    }
}

