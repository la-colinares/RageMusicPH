package com.lacolinares.ragemusicph.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.lifecycle.lifecycleScope
import com.google.android.exoplayer2.MediaItem
import com.lacolinares.ragemusicph.navigation.NavGraph
import com.lacolinares.ragemusicph.presentation.ui.screens.main.MainScreenViewModel
import com.lacolinares.ragemusicph.presentation.ui.theme.RageMusicPHTheme
import com.lacolinares.ragemusicph.service.PlayerService
import com.lacolinares.ragemusicph.service.PlayerService.ServiceBinder
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch


@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val url = "https://node-15.zeno.fm/yu9dryfs7x8uv.mp3?rj-ttl=5&rj-tok=AAABgFn8jHMAJlhQjlgTTNm8Iw"
    private var isBound = false

    private val viewModel : MainScreenViewModel by viewModels()

    private var isBackPressOnce = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        doBindService()

        lifecycleScope.launchWhenResumed {
            viewModel.rebindService.collect {
                if (it){
                    doBindService()
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        doUnbindService()
    }

    private fun doUnbindService(){
        if (isBound){
            unbindService(playServiceConnection)
            isBound = false
        }
    }

    private fun doBindService(){
        val playServiceIntent = Intent(this, PlayerService::class.java)
        bindService(playServiceIntent, playServiceConnection, Context.BIND_AUTO_CREATE)
    }

    private val playServiceConnection = object: ServiceConnection{
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as ServiceBinder
            val exoPlayer = binder.playerService.player
            val mediaItem = MediaItem.fromUri(url)
            exoPlayer.setMediaItem(mediaItem)
            isBound = true
            viewModel.setRebindService(false)

            binder.playerService.onStopListener = PlayerService.OnStopForeground {
                doUnbindService()
                viewModel.setForegroundServiceStopped(true)
            }

            this@MainActivity.setContent {
                RageMusicPHTheme {
                    NavGraph(exoPlayer, viewModel)
                }
            }
        }

        override fun onServiceDisconnected(p0: ComponentName?) {}
    }

    override fun onBackPressed() {
        if (isBackPressOnce){
            super.onBackPressed()
            return
        }
        Toast.makeText(this, "Press again to exit.", Toast.LENGTH_SHORT).show()
        isBackPressOnce = true
        lifecycleScope.launch {
            delay(2000L)
            isBackPressOnce = false
        }
    }
}