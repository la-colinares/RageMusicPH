package com.lacolinares.ragemusicph.presentation.ui.screens.main.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.lacolinares.ragemusicph.R
import com.lacolinares.ragemusicph.custom.Space
import com.lacolinares.ragemusicph.presentation.ui.theme.orbitronFamily

@Composable
fun TopContent(headerTitle: String) {
    Header(headerTitle)
    Space(48)
    Logo()
    Space(24)
}

@Composable
private fun Logo() {
    Image(
        painter = painterResource(id = R.drawable.ic_rage_music_ph),
        contentDescription = "logo",
        modifier = Modifier
            .fillMaxWidth()
            .height(192.dp),
        alignment = Alignment.Center
    )
}

@Composable
private fun Header(title: String = "") {
    Text(
        text = title,
        color = Color.White,
        fontFamily = orbitronFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 32.sp,
        modifier = Modifier.fillMaxWidth()
    )
}