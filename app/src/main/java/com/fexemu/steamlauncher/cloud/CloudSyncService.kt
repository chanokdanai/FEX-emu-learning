package com.fexemu.steamlauncher.cloud

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*

/**
 * Background service for syncing cloud saves
 */
class CloudSyncService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val CHANNEL_ID = "cloud_sync_channel"
    private val NOTIFICATION_ID = 2
    
    private lateinit var cloudSaveManager: CloudSaveManager
    
    companion object {
        const val EXTRA_APP_ID = "app_id"
        const val ACTION_SYNC = "sync"
        
        fun startSync(context: Context, appId: Long) {
            val intent = Intent(context, CloudSyncService::class.java).apply {
                action = ACTION_SYNC
                putExtra(EXTRA_APP_ID, appId)
            }
            context.startForegroundService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        cloudSaveManager = CloudSaveManager(this)
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val appId = intent?.getLongExtra(EXTRA_APP_ID, -1) ?: -1
        
        startForeground(NOTIFICATION_ID, createNotification())
        
        if (appId != -1L) {
            serviceScope.launch {
                syncSaves(appId)
            }
        }
        
        return START_NOT_STICKY
    }
    
    private suspend fun syncSaves(appId: Long) {
        try {
            cloudSaveManager.syncGameSaves(appId)
            stopSelf()
        } catch (e: Exception) {
            e.printStackTrace()
            stopSelf()
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Cloud Save Sync",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Syncs game saves with Steam Cloud"
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification() =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Syncing Cloud Saves")
            .setContentText("Syncing game saves with Steam Cloud")
            .setSmallIcon(android.R.drawable.stat_sys_upload)
            .build()
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
