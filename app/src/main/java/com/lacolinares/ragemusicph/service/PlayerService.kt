package com.lacolinares.ragemusicph.service

import android.app.Notification
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.os.Binder
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import com.google.android.exoplayer2.C
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaMetadata
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.audio.AudioAttributes
import com.google.android.exoplayer2.source.DefaultMediaSourceFactory
import com.google.android.exoplayer2.ui.PlayerNotificationManager
import com.google.android.exoplayer2.upstream.DataSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource
import com.google.android.exoplayer2.util.Util
import com.lacolinares.ragemusicph.R
import kotlinx.coroutines.flow.MutableStateFlow

class PlayerService : Service() {

    private val serviceBinder = ServiceBinder()

    lateinit var player: ExoPlayer
    lateinit var notificationManager: PlayerNotificationManager

    fun interface OnStopForeground {
        fun onStop()
    }

    var onStopListener : OnStopForeground? = null

    inner class ServiceBinder : Binder() {
        val playerService: PlayerService
            get() = this@PlayerService
    }

    override fun onBind(intent: Intent): IBinder {
        return serviceBinder
    }

    override fun onCreate() {
        super.onCreate()
        val httpDataSourceFactory = DefaultHttpDataSource.Factory()
            .setUserAgent(Util.getUserAgent(applicationContext, "RageMusicStream"))
            .setAllowCrossProtocolRedirects(true)
        val dataSource : DataSource.Factory = DefaultDataSource.Factory(applicationContext, httpDataSourceFactory)
        val mediaSourceFactory = DefaultMediaSourceFactory(dataSource)

        player = ExoPlayer.Builder(applicationContext)
            .setMediaSourceFactory(mediaSourceFactory)
            .build()

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
            .setSmallIconResourceId(R.drawable.ic_baseline_music_note_24)
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
            onStopListener?.onStop()
            stopForeground(true)
            if (player.isPlaying) player.pause()
        }

        override fun onNotificationPosted(notificationId: Int, notification: Notification, ongoing: Boolean) {
            super.onNotificationPosted(notificationId, notification, ongoing)
            startForeground(notificationId, notification)
        }
    }

    private fun descriptionAdapter():  PlayerNotificationManager.MediaDescriptionAdapter {
        val title = MutableStateFlow("Rage Music PH")
        val artist = MutableStateFlow("Pinoy Rap Hits")
        player.addListener(object : Player.Listener {
            override fun onMediaMetadataChanged(mediaMetadata: MediaMetadata) {
                super.onMediaMetadataChanged(mediaMetadata)
                if (mediaMetadata.title != null) {
                    val data = mediaMetadata.title.toString().split("-")
                    title.value = data.last().trim()
                    artist.value = data.first().trim()
                }
            }
        })

        return object : PlayerNotificationManager.MediaDescriptionAdapter {
            override fun getCurrentContentTitle(player: Player): CharSequence {
                return title.value
            }
            override fun createCurrentContentIntent(player: Player): PendingIntent? = null
            override fun getCurrentContentText(player: Player): CharSequence {
                return artist.value
            }
            override fun getCurrentLargeIcon(player: Player, callback: PlayerNotificationManager.BitmapCallback): Bitmap? {
                val bitmapDrawable: BitmapDrawable = ContextCompat.getDrawable(applicationContext, R.drawable.ic_rage_music_large) as BitmapDrawable
                return bitmapDrawable.bitmap
            }
        }
    }
}