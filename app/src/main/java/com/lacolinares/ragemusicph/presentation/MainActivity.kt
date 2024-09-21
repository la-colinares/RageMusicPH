package com.lacolinares.ragemusicph.presentation

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.lifecycle.lifecycleScope
import androidx.media3.common.util.UnstableApi
import com.lacolinares.ragemusicph.navigation.NavGraph
import com.lacolinares.ragemusicph.presentation.ui.theme.RageMusicPHTheme
import com.lacolinares.ragemusicph.service.PlayerService
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        setContent {
            val isBackPressed = remember { mutableStateOf(false) }
            BackHandler (!isBackPressed.value){
                isBackPressed.value = true
                val isHome = viewModel.isHome.value
                if (isHome){
                    Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show()
                    lifecycleScope.launch {
                        delay(2000L)
                        isBackPressed.value = false
                    }
                }else{
                    finish()
                }
            }
            RageMusicPHTheme {
                NavGraph(viewModel)
            }
        }
    }

    @UnstableApi
    override fun onDestroy() {
        super.onDestroy()
        stopService(Intent(this, PlayerService::class.java))
    }
}