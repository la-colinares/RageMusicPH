package com.lacolinares.ragemusicph.presentation

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
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
import kotlinx.coroutines.flow.collect


@ExperimentalAnimationApi
class MainActivity : ComponentActivity() {

    private val url = "https://node-15.zeno.fm/yu9dryfs7x8uv.mp3?rj-ttl=5&rj-tok=AAABgFn8jHMAJlhQjlgTTNm8Iw"
    private var isBound = false

    private val viewModel : MainScreenViewModel by viewModels()

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
}