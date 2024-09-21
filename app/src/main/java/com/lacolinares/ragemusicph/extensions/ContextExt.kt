package com.lacolinares.ragemusicph.extensions

import android.content.*
import android.net.Uri
import android.os.IBinder
import androidx.activity.ComponentActivity
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import com.lacolinares.ragemusicph.service.PlayerService

inline fun <reified Activity : ComponentActivity> Context.getActivity(): Activity? {
    return when (this) {
        is Activity -> this
        else -> {
            var context = this
            while (context is ContextWrapper) {
                context = context.baseContext
                if (context is Activity) return context
            }
            null
        }
    }
}

fun Context.openPlayStoreApp() {
    val pkgName = this.packageName
    if (!pkgName.isNullOrEmpty()) {
        try {
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$pkgName")))
        } catch (e: ActivityNotFoundException) {
            startActivity(
                Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("https://play.google.com/store/apps/details?id=$pkgName")
                )
            )
        }
    }
}

@OptIn(UnstableApi::class)
fun Context.runMusicService(onStarted: (ExoPlayer) -> Unit, onStop: (ServiceConnection) -> Unit) {
    var playerService: PlayerService? = null
    val playServiceConnection = object : ServiceConnection {
        override fun onServiceConnected(p0: ComponentName?, p1: IBinder?) {
            val binder = p1 as PlayerService.ServiceBinder
            playerService = binder.playerService
            val exoPlayer = binder.playerService.player
            onStarted.invoke(exoPlayer)
        }
        override fun onServiceDisconnected(p0: ComponentName?) {}
    }

    playerService?.onStopListener = PlayerService.OnStopForeground {
        onStop.invoke(playServiceConnection)
    }

    Intent(this, PlayerService::class.java).also {
        bindService(it, playServiceConnection, Context.BIND_AUTO_CREATE)
    }
}