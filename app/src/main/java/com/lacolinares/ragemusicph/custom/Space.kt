package com.lacolinares.ragemusicph.custom

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun Space(height: Int = 1){
    Spacer(modifier = Modifier.height(height.dp))
}