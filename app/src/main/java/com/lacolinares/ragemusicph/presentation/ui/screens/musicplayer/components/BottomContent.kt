package com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.components

import android.content.Context
import android.media.AudioManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Slider
import androidx.compose.material3.SliderDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.presentation.ui.theme.Red

@Composable
fun BottomContent(
    isPlay: Boolean,
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
) {
    VolumeController()
    Space(height = 12)
    PlayPauseButton(
        isPlay = isPlay,
        onPlay = { onPlay.invoke() },
        onPause = { onPause.invoke() }
    )
}

@Composable
fun PlayPauseButton(
    isPlay: Boolean,
    onPlay: () -> Unit = {},
    onPause: () -> Unit = {},
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        FloatingActionButton(
            onClick = {
                if (isPlay) {
                    onPause.invoke()
                } else {
                    onPlay.invoke()
                }
            },
            contentColor = Red,
        ) {
            val icon = if (isPlay) R.drawable.ic_baseline_pause_24 else R.drawable.ic_baseline_play_arrow_24
            Image(
                painter = painterResource(id = icon),
                contentDescription = "Play Pause Icon",
            )
        }
    }
}

@Composable
private fun VolumeController() {
    val context = LocalContext.current
    val audioManager = context.getSystemService(Context.AUDIO_SERVICE) as AudioManager
    val currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
    val maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC).toFloat()
    var sliderPosition by remember { mutableStateOf(currentVolume.toFloat()) }
    Row(verticalAlignment = Alignment.CenterVertically) {
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_volume_mute_24),
            contentDescription = "mute",
            modifier = Modifier
                .wrapContentWidth(Alignment.Start)
                .weight(1f)
                .height(24.dp),
        )
        Slider(
            value = sliderPosition,
            valueRange = 0f..maxVolume,
            onValueChange = {
                sliderPosition = it
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, sliderPosition.toInt(), 0)
            },
            steps = 10,
            colors = SliderDefaults.colors(
                thumbColor = Red,
                activeTrackColor = Red,
                activeTickColor = Red,
                inactiveTrackColor = Color.White,
                inactiveTickColor = Color.White
            ),
            modifier = Modifier.weight(8f)
        )
        Image(
            painter = painterResource(id = R.drawable.ic_baseline_volume_up_24),
            contentDescription = "volume_up",
            modifier = Modifier
                .wrapContentWidth(Alignment.End)
                .weight(1f)
                .height(24.dp),
        )
    }
}
