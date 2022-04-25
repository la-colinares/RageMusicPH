package com.lacolinares.ragemusicph.presentation.ui.screens.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.android.exoplayer2.ExoPlayer
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.BottomContent
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.MidContent
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.TopContent
import com.lacolinares.ragemusicph.presentation.ui.theme.Background
import com.lacolinares.ragemusicph.presentation.ui.theme.Red


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
            if (audioTitle.isNotEmpty()){
                BottomContent(
                    isPlay = isAudioPlaying,
                    onPlay = {
                        exoPlayer.play()
                    },
                    onPause = {
                        exoPlayer.pause()
                    }
                )
            }else{
                Loader()
            }
        }
    }
}

@Composable fun Loader(){
    Space(height = 24)
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = Red)
    }
}

