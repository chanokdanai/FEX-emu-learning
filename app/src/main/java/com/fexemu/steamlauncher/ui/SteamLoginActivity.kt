package com.fexemu.steamlauncher.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.fexemu.steamlauncher.R
import com.fexemu.steamlauncher.databinding.ActivitySteamLoginBinding
import com.fexemu.steamlauncher.steam.SteamManager
import com.fexemu.steamlauncher.steam.models.SteamAuth

/**
 * Activity for Steam login
 */
class SteamLoginActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySteamLoginBinding
    private lateinit var steamManager: SteamManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySteamLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        steamManager = SteamManager(this)
        
        // Check if already logged in
        if (steamManager.isAuthenticated()) {
            navigateToMain()
            return
        }
        
        setupClickListeners()
    }
    
    private fun setupClickListeners() {
        binding.loginButton.setOnClickListener {
            performLogin()
        }
        
        binding.getApiKeyLink.setOnClickListener {
            openSteamApiKeyPage()
        }
    }
    
    private fun performLogin() {
        val steamId = binding.steamIdInput.text.toString().trim()
        val apiKey = binding.apiKeyInput.text.toString().trim()
        
        // Validate input
        if (steamId.isEmpty() || apiKey.isEmpty()) {
            Toast.makeText(this, R.string.error_login, Toast.LENGTH_SHORT).show()
            return
        }
        
        // Validate Steam ID format (should be 17-digit number)
        if (!steamId.matches(Regex("^\\d{17}$"))) {
            Toast.makeText(this, "Invalid Steam ID format. Should be a 17-digit number.", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Validate API key format (should be 32-character hex string)
        if (!apiKey.matches(Regex("^[A-Fa-f0-9]{32}$"))) {
            Toast.makeText(this, "Invalid API Key format. Should be a 32-character hexadecimal string.", Toast.LENGTH_SHORT).show()
            return
        }
        
        // Save authentication
        val auth = SteamAuth(
            steamId = steamId,
            apiKey = apiKey,
            username = null,
            avatarUrl = null
        )
        
        steamManager.saveAuth(auth)
        navigateToMain()
    }
    
    private fun navigateToMain() {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
    
    private fun openSteamApiKeyPage() {
        val intent = Intent(Intent.ACTION_VIEW).apply {
            data = Uri.parse("https://steamcommunity.com/dev/apikey")
        }
        startActivity(intent)
    }
}
