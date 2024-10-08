package com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.components

import android.graphics.Color
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.custom.EqualizerView
import com.lacolinares.ragemusicph.custom.Space

@Composable
internal fun MusicPlayerAudioContent(
    playEqualizer: Boolean = false,
    musicTitle: String,
    musicArtist: String,
    onShareApp: () -> Unit = {}
){
    Equalizer(isPlay = playEqualizer)
    Space(24)
    MusicInfo(title = musicTitle, artist = musicArtist)
    Space(24)
    if(musicTitle.isNotEmpty()){
        ShareApp() {
            onShareApp.invoke()
        }
    }
    Space(12)
}

@Composable
private fun ShareApp(onClick: () -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.Center){
        Image(
            painter = painterResource(id = R.drawable.ic_share_circle),
            contentDescription = "share",
            modifier = Modifier
                .height(32.dp)
                .clickable { onClick.invoke() },
            alignment = Alignment.Center,
        )
    }
}

@Composable
private fun MusicInfo(title: String = "N/A", artist: String = "N/A") {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = title,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = White,
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = artist,
            fontSize = 16.sp,
            fontWeight = FontWeight.Light,
            textAlign = TextAlign.Center,
            color = White
        )
    }
}

@Composable
private fun Equalizer(isPlay: Boolean = false) {
    AndroidView(
        modifier = Modifier
            .height(40.dp)
            .fillMaxWidth(),
        factory = { ctx ->
            EqualizerView(ctx).apply {
                setup(
                    barColor = Color.WHITE,
                    barWidth = 32,
                    barCount = 3,
                    marginStart = 2,
                    marginEnd = 2
                )
            }
        }, update = {
            if (isPlay) {
                it.animateBars()
            } else {
                it.stopBars()
            }
        }
    )
}

