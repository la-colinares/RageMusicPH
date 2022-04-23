package com.lacolinares.ragemusicph.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import com.lacolinares.ragemusicph.navigation.NavGraph
import com.lacolinares.ragemusicph.presentation.ui.theme.RageMusicPHTheme

@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RageMusicPHTheme {
                NavGraph()
            }
        }
    }
}