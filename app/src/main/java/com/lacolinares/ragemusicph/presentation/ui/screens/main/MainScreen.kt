package com.lacolinares.ragemusicph.presentation.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.BottomContent
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.MidContent
import com.lacolinares.ragemusicph.presentation.ui.screens.main.components.TopContent
import com.lacolinares.ragemusicph.presentation.ui.theme.Background

@Composable
fun MainScreen() {
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
                playEqualizer = false,
                musicTitle = "Bagong Pilipinas",
                musicArtist = "Andrew E.",
                onShareApp = {
                    //TODO: share app url link
                }
            )
            BottomContent(
                isPlay = false,
                onPlay = {
                    //TODO: play the music
                },
                onPause = {
                    //TODO: pause the music
                }
            )
        }
    }
}
