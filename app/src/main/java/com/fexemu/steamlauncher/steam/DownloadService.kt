package com.fexemu.steamlauncher.steam

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import kotlinx.coroutines.*
import java.io.File
import java.io.FileOutputStream
import java.net.URL

/**
 * Service for downloading Steam games in the background
 */
class DownloadService : Service() {
    
    private val serviceScope = CoroutineScope(Dispatchers.IO + SupervisorJob())
    private val CHANNEL_ID = "download_channel"
    private val NOTIFICATION_ID = 1
    
    companion object {
        const val EXTRA_GAME_ID = "game_id"
        const val EXTRA_GAME_NAME = "game_name"
        const val EXTRA_DOWNLOAD_URL = "download_url"
        
        fun startDownload(context: Context, gameId: Long, gameName: String, downloadUrl: String) {
            val intent = Intent(context, DownloadService::class.java).apply {
                putExtra(EXTRA_GAME_ID, gameId)
                putExtra(EXTRA_GAME_NAME, gameName)
                putExtra(EXTRA_DOWNLOAD_URL, downloadUrl)
            }
            context.startForegroundService(intent)
        }
    }
    
    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }
    
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val gameId = intent?.getLongExtra(EXTRA_GAME_ID, -1) ?: -1
        val gameName = intent?.getStringExtra(EXTRA_GAME_NAME) ?: "Unknown Game"
        val downloadUrl = intent?.getStringExtra(EXTRA_DOWNLOAD_URL) ?: ""
        
        startForeground(NOTIFICATION_ID, createNotification(gameName, 0))
        
        if (downloadUrl.isNotEmpty()) {
            serviceScope.launch {
                downloadGame(gameId, gameName, downloadUrl)
            }
        }
        
        return START_NOT_STICKY
    }
    
    private suspend fun downloadGame(gameId: Long, gameName: String, downloadUrl: String) {
        withContext(Dispatchers.IO) {
            try {
                val gamesDir = File(getExternalFilesDir(null), "games/$gameId")
                gamesDir.mkdirs()
                
                val outputFile = File(gamesDir, "game.tar.gz")
                val connection = URL(downloadUrl).openConnection()
                val fileLength = connection.contentLength
                
                connection.getInputStream().use { input ->
                    FileOutputStream(outputFile).use { output ->
                        val buffer = ByteArray(4096)
                        var totalRead = 0L
                        var read: Int
                        
                        while (input.read(buffer).also { read = it } != -1) {
                            output.write(buffer, 0, read)
                            totalRead += read
                            
                            val progress = (totalRead * 100 / fileLength).toInt()
                            updateNotification(gameName, progress)
                        }
                    }
                }
                
                // TODO: Extract downloaded file
                // This requires implementing proper extraction logic for tar.gz files
                // Example: ProcessBuilder("tar", "-xzf", outputFile.path, "-C", gamesDir.path).start()
                
                stopSelf()
            } catch (e: Exception) {
                // Handle download error
                e.printStackTrace()
                stopSelf()
            }
        }
    }
    
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                "Game Downloads",
                NotificationManager.IMPORTANCE_LOW
            ).apply {
                description = "Shows download progress for Steam games"
            }
            
            val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
    
    private fun createNotification(gameName: String, progress: Int) =
        NotificationCompat.Builder(this, CHANNEL_ID)
            .setContentTitle("Downloading $gameName")
            .setContentText("$progress%")
            .setProgress(100, progress, false)
            .setSmallIcon(android.R.drawable.stat_sys_download)
            .build()
    
    private fun updateNotification(gameName: String, progress: Int) {
        val notification = createNotification(gameName, progress)
        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(NOTIFICATION_ID, notification)
    }
    
    override fun onBind(intent: Intent?): IBinder? = null
    
    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }
}
