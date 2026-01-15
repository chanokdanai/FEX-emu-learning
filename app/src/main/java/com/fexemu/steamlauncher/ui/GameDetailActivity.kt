package com.fexemu.steamlauncher.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fexemu.steamlauncher.R
import com.fexemu.steamlauncher.cloud.CloudSyncService
import com.fexemu.steamlauncher.databinding.ActivityGameDetailBinding
import com.fexemu.steamlauncher.fex.GameLauncher
import com.fexemu.steamlauncher.steam.DownloadService
import kotlinx.coroutines.launch

/**
 * Activity showing game details and actions
 */
class GameDetailActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityGameDetailBinding
    private lateinit var gameLauncher: GameLauncher
    
    private var gameId: Long = -1
    private var gameName: String = ""
    private var playtime: Int = 0
    
    companion object {
        const val EXTRA_GAME_ID = "game_id"
        const val EXTRA_GAME_NAME = "game_name"
        const val EXTRA_GAME_PLAYTIME = "game_playtime"
    }
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityGameDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        gameLauncher = GameLauncher(this)
        
        // Get game data from intent
        gameId = intent.getLongExtra(EXTRA_GAME_ID, -1)
        gameName = intent.getStringExtra(EXTRA_GAME_NAME) ?: ""
        playtime = intent.getIntExtra(EXTRA_GAME_PLAYTIME, 0)
        
        setupViews()
        setupClickListeners()
    }
    
    private fun setupViews() {
        supportActionBar?.title = gameName
        
        binding.gameName.text = gameName
        binding.gamePlaytime.text = getString(R.string.playtime, playtime)
    }
    
    private fun setupClickListeners() {
        binding.btnDownload.setOnClickListener {
            downloadGame()
        }
        
        binding.btnPlay.setOnClickListener {
            playGame()
        }
        
        binding.btnSyncSaves.setOnClickListener {
            syncSaves()
        }
    }
    
    private fun downloadGame() {
        // NOTE: This is a placeholder implementation.
        // In a real application, you would need to:
        // 1. Use Steam's content delivery system (CDN)
        // 2. Authenticate with Steam's download servers
        // 3. Handle Steam's depot system for game files
        // Steam Web API doesn't provide direct game download URLs
        
        val downloadUrl = "https://example.com/games/$gameId"
        
        DownloadService.startDownload(this, gameId, gameName, downloadUrl)
        
        Toast.makeText(
            this,
            getString(R.string.downloading),
            Toast.LENGTH_SHORT
        ).show()
        
        // Show progress
        binding.downloadProgress.visibility = View.VISIBLE
        binding.progressText.visibility = View.VISIBLE
        binding.btnDownload.visibility = View.GONE
        binding.btnPlay.visibility = View.VISIBLE
    }
    
    private fun playGame() {
        lifecycleScope.launch {
            val logs = mutableListOf<String>()
            
            val result = gameLauncher.launchGame(gameId, gameName) { log ->
                logs.add(log)
            }
            
            result.onSuccess {
                Toast.makeText(
                    this@GameDetailActivity,
                    "Game launched! Logs: ${logs.joinToString("\n")}",
                    Toast.LENGTH_LONG
                ).show()
            }.onFailure { error ->
                Toast.makeText(
                    this@GameDetailActivity,
                    getString(R.string.error_launch) + ": ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun syncSaves() {
        CloudSyncService.startSync(this, gameId)
        
        Toast.makeText(
            this,
            getString(R.string.syncing_saves),
            Toast.LENGTH_SHORT
        ).show()
    }
}
