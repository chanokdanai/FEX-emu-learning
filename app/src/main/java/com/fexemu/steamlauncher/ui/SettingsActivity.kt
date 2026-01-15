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
        binding.switchEnableLsfgVk.isChecked = config.enableLsfgVk
        binding.seekbarLsfgMultiplier.progress = config.lsfgVkMultiplier - 1 // 0-based for seekbar
        updateLsfgMultiplierText(config.lsfgVkMultiplier)
    }
    
    private fun updateLsfgMultiplierText(multiplier: Int) {
        binding.tvLsfgMultiplier.text = getString(R.string.lsfg_multiplier_format, multiplier)
    }
    
    private fun setupClickListeners() {
        binding.btnDownloadRootfs.setOnClickListener {
            downloadRootfs()
        }
        
        // Save settings when switches are toggled
        binding.switchEnableJit.setOnCheckedChangeListener { _, isChecked ->
            saveSettings()
        }
        
        binding.switchEnableThunking.setOnCheckedChangeListener { _, isChecked ->
            saveSettings()
        }
        
        binding.switchEnableLsfgVk.setOnCheckedChangeListener { _, isChecked ->
            saveSettings()
        }
        
        // Update LSFG multiplier
        binding.seekbarLsfgMultiplier.setOnSeekBarChangeListener(
            object : android.widget.SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: android.widget.SeekBar?, progress: Int, fromUser: Boolean) {
                    val multiplier = progress + 1 // 1-based multiplier
                    updateLsfgMultiplierText(multiplier)
                }
                
                override fun onStartTrackingTouch(seekBar: android.widget.SeekBar?) {}
                
                override fun onStopTrackingTouch(seekBar: android.widget.SeekBar?) {
                    saveSettings()
                }
            }
        )
    }
    
    private fun saveSettings() {
        val config = fexEmuManager.getConfig().copy(
            enableJit = binding.switchEnableJit.isChecked,
            enableThunking = binding.switchEnableThunking.isChecked,
            enableLsfgVk = binding.switchEnableLsfgVk.isChecked,
            lsfgVkMultiplier = binding.seekbarLsfgMultiplier.progress + 1
        )
        fexEmuManager.saveConfig(config)
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
