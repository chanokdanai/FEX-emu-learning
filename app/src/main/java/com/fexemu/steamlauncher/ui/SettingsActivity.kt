package com.fexemu.steamlauncher.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.fexemu.steamlauncher.databinding.ActivitySettingsBinding
import com.fexemu.steamlauncher.fex.FexEmuManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * Settings activity for FEX-Emu configuration
 */
class SettingsActivity : AppCompatActivity() {
    
    private lateinit var binding: ActivitySettingsBinding
    private lateinit var fexEmuManager: FexEmuManager
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = "Settings"
        
        fexEmuManager = FexEmuManager(this)
        
        loadSettings()
        setupClickListeners()
    }
    
    private fun loadSettings() {
        val config = fexEmuManager.getConfig()
        binding.switchEnableJit.isChecked = config.enableJit
        binding.switchEnableThunking.isChecked = config.enableThunking
    }
    
    private fun setupClickListeners() {
        binding.btnDownloadRootfs.setOnClickListener {
            downloadRootfs()
        }
    }
    
    private fun downloadRootfs() {
        val url = binding.rootfsUrlInput.text.toString().trim()
        
        if (url.isEmpty()) {
            Toast.makeText(this, "Please enter a rootfs URL", Toast.LENGTH_SHORT).show()
            return
        }
        
        binding.rootfsProgress.visibility = View.VISIBLE
        binding.btnDownloadRootfs.isEnabled = false
        
        lifecycleScope.launch {
            val success = withContext(Dispatchers.IO) {
                fexEmuManager.setupRootfs(url) { progress ->
                    lifecycleScope.launch(Dispatchers.Main) {
                        binding.rootfsProgress.progress = progress
                    }
                }
            }
            
            binding.rootfsProgress.visibility = View.GONE
            binding.btnDownloadRootfs.isEnabled = true
            
            if (success) {
                Toast.makeText(
                    this@SettingsActivity,
                    "Rootfs downloaded successfully",
                    Toast.LENGTH_SHORT
                ).show()
            } else {
                Toast.makeText(
                    this@SettingsActivity,
                    "Failed to download rootfs",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
    
    override fun onSupportNavigateUp(): Boolean {
        finish()
        return true
    }
}
