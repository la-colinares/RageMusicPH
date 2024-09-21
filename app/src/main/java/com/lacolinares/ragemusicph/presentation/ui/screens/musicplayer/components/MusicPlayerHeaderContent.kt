package com.lacolinares.ragemusicph.presentation.ui.screens.musicplayer.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.presentation.ui.theme.SubTitleColor
import com.lacolinares.ragemusicph.presentation.ui.theme.orbitronFamily

@Composable
internal fun MusicPlayerHeaderContent(headerTitle: String, description: String, logo: Int = R.drawable.ic_rage_music_ph) {
    Header(title = headerTitle, textAlignment = TextAlign.Center, textSize = 24.sp)
    Space(4)
    Header(title = description, textAlignment = TextAlign.Center, textSize = 14.sp, textColor = SubTitleColor)
    Space(48)
    Logo(logo)
    Space(24)
}

@Composable
private fun Logo(logo: Int) {
    Image(
        painter = painterResource(id = logo),
        contentDescription = "logo",
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp),
        alignment = Alignment.Center
    )
}

@Composable
private fun Header(
    title: String = "",
    textAlignment: TextAlign = TextAlign.Left,
    textSize: TextUnit = 32.sp,
    textColor: Color = Color.White
) {
    Text(
        text = title,
        color = textColor,
        fontFamily = orbitronFamily,
        fontWeight = FontWeight.Medium,
        fontSize = textSize,
        textAlign = textAlignment,
        modifier = Modifier.fillMaxWidth()
    )
}