package com.fexemu.steamlauncher.ui

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.fexemu.steamlauncher.R
import com.fexemu.steamlauncher.databinding.ActivityMainBinding
import com.fexemu.steamlauncher.steam.SteamManager
import com.fexemu.steamlauncher.steam.models.SteamGame
import kotlinx.coroutines.launch

/**
 * Main activity showing game library
 */
class MainActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivityMainBinding
    private lateinit var steamManager: SteamManager
    private lateinit var gamesAdapter: GamesAdapter
    private val games = mutableListOf<SteamGame>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        setSupportActionBar(binding.toolbar)
        
        steamManager = SteamManager(this)
        
        // Check authentication
        if (!steamManager.isAuthenticated()) {
            navigateToLogin()
            return
        }
        
        setupRecyclerView()
        setupSwipeRefresh()
        setupFab()
        
        // Load games
        loadGames()
    }
    
    private fun setupRecyclerView() {
        gamesAdapter = GamesAdapter(games) { game ->
            openGameDetails(game)
        }
        
        binding.gamesRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = gamesAdapter
        }
    }
    
    private fun setupSwipeRefresh() {
        binding.swipeRefresh.setOnRefreshListener {
            loadGames()
        }
    }
    
    private fun setupFab() {
        binding.fabRefresh.setOnClickListener {
            loadGames()
        }
    }
    
    private fun loadGames() {
        binding.swipeRefresh.isRefreshing = true
        
        lifecycleScope.launch {
            val result = steamManager.fetchOwnedGames()
            
            binding.swipeRefresh.isRefreshing = false
            
            result.onSuccess { fetchedGames ->
                games.clear()
                games.addAll(fetchedGames)
                gamesAdapter.notifyDataSetChanged()
                
                binding.emptyView.visibility = 
                    if (games.isEmpty()) View.VISIBLE else View.GONE
            }.onFailure { error ->
                Toast.makeText(
                    this@MainActivity,
                    getString(R.string.error_network) + ": ${error.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    private fun openGameDetails(game: SteamGame) {
        val intent = Intent(this, GameDetailActivity::class.java).apply {
            putExtra(GameDetailActivity.EXTRA_GAME_ID, game.appId)
            putExtra(GameDetailActivity.EXTRA_GAME_NAME, game.name)
            putExtra(GameDetailActivity.EXTRA_GAME_PLAYTIME, game.playtimeForever)
        }
        startActivity(intent)
    }
    
    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }
    
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                startActivity(Intent(this, SettingsActivity::class.java))
                true
            }
            R.id.action_logout -> {
                steamManager.logout()
                navigateToLogin()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
    
    private fun navigateToLogin() {
        startActivity(Intent(this, SteamLoginActivity::class.java))
        finish()
    }
}
