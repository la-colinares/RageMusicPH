package com.lacolinares.ragemusicph.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.lifecycleScope
import com.lacolinares.ragemusicph.navigation.NavGraph
import com.lacolinares.ragemusicph.presentation.ui.theme.RageMusicPHTheme
import com.lacolinares.ragemusicph.service.PlayerService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()
    private var isBackPressOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RageMusicPHTheme {
                NavGraph(viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PlayerService::class.java))
    }

    override fun onBackPressed() {
        val isHome = viewModel.isHome.value
        if (isHome){
            if (isBackPressOnce) {
                super.onBackPressed()
                return
            }
            Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show()
            isBackPressOnce = true
            lifecycleScope.launch {
                delay(2000L)
                isBackPressOnce = false
            }
        }else{
            super.onBackPressed()
        }
    }
}