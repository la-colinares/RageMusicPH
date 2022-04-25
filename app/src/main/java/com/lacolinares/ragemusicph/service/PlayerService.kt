package com.lacolinares.ragemusicph.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.lacolinares.ragemusicph.R
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerService : Service() {

    private val serviceBinder = ServiceBinder()

    lateinit var player: ExoPlayer
    lateinit var notificationManager: PlayerNotificationManager

    inner class ServiceBinder : Binder() {
        val playerService: PlayerService
            get() = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        player = ExoPlayer.Builder(applicationContext).build()

        val audioAttributes = AudioAttributes.Builder()
            .setUsage(C.USAGE_MEDIA)
            .setContentType(C.CONTENT_TYPE_MUSIC)
            .build()
        player.setAudioAttributes(audioAttributes, true)

        //notification manager
        val channelId = resources.getString(R.string.app_name) + " Radio Channel "
        val notificationId = 18175

        notificationManager = PlayerNotificationManager.Builder(this, notificationId, channelId)
            .setNotificationListener(notificationListener)
            .setMediaDescriptionAdapter(descriptionAdapter())
            .setSmallIconResourceId(R.drawable.ic_rage_music_ph_notif)
            .setChannelDescriptionResourceId(R.string.app_name)
            .setPauseActionIconResourceId(R.drawable.ic_baseline_pause_24)
            .setPlayActionIconResourceId(R.drawable.ic_baseline_play_arrow_24)
            .setStopActionIconResourceId(R.drawable.ic_baseline_clear_24)
            .setChannelNameResourceId(R.string.app_name)
            .build()

        notificationManager.apply {
            setPlayer(player)
            setPriority(NotificationCompat.PRIORITY_MAX)
            setUseRewindAction(false)
            setUseFastForwardAction(false)
            setUseStopAction(true)
        }
    }

    override fun onDestroy() {
        if (player.isPlaying) player.stop()
        notificationManager.setPlayer(null)
        player.release()
        stopForeground(true)
        stopSelf()
        super.onDestroy()
    }

    private val notificationListener = object: PlayerNotificationManager.NotificationListener{
        override fun onNotificationCancelled(notificationId: Int, dismissedByUser: Boolean) {
            super.onNotificationCancelled(notificationId, dismissedByUser)
            stopForeground(true)
            if (player.isPlaying) player.pause()
        }

        override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            startForeground(notificationId, notification)
        }
    }

    private fun descriptionAdapter():  PlayerNotificationManager.MediaDescriptionAdapter {
        val title = MutableStateFlow("Rage Music PH Pinoy Rap Hits")
        player.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                if (mediaMetadata.title != null) {
                    title.value = mediaMetadata.title.toString()
                }
            }
        })

        return object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return title.value
            }
            override fun createCurrentContentIntent(player: Player): PendingIntent? = null
            override fun getCurrentContentText(player: Player): CharSequence? = null
            override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? = null
        }
    }
}