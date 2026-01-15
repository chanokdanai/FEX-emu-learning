package com.fexemu.steamlauncher.fex

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

/**
 * Manages game launching with FEX-Emu
 */
class GameLauncher(private val context: Context) {
    
    private val fexEmuManager = FexEmuManager(context)
    
    /**
     * Launch a Steam game using FEX-Emu
     */
    suspend fun launchGame(
        gameId: Long, 
        gameName: String,
        onLog: (String) -> Unit = {}
    ): Result<Process?> = withContext(Dispatchers.IO) {
        try {
            onLog("Preparing to launch $gameName...")
            
            // Check if FEX-Emu is configured
            if (!fexEmuManager.isConfigured()) {
                onLog("Initializing FEX-Emu...")
                if (!fexEmuManager.initialize()) {
                    return@withContext Result.failure(
                        Exception("Failed to initialize FEX-Emu")
                    )
                }
            }
            
            // Get game installation path
            val gamePath = File(
                context.getExternalFilesDir(null),
                "games/$gameId"
            )
            
            if (!gamePath.exists()) {
                return@withContext Result.failure(
                    Exception("Game not downloaded")
                )
            }
            
            // Find game executable
            val gameExecutable = findGameExecutable(gamePath)
                ?: return@withContext Result.failure(
                    Exception("Game executable not found")
                )
            
            onLog("Launching game with FEX-Emu...")
            onLog("Executable: ${gameExecutable.absolutePath}")
            
            // Launch with FEX-Emu
            val process = fexEmuManager.launchGame(
                gameId,
                gameExecutable.absolutePath
            )
            
            onLog("Game launched successfully!")
            Result.success(process)
            
        } catch (e: Exception) {
            onLog("Error: ${e.message}")
            Result.failure(e)
        }
    }
    
    /**
     * Find the main executable in the game directory
     */
    private fun findGameExecutable(gameDir: File): File? {
        // Look for common executable patterns
        val executableExtensions = listOf("exe", "x86_64", "")
        
        gameDir.walk().forEach { file ->
            if (file.isFile && file.canExecute()) {
                return file
            }
            
            // Check for .exe files (Windows games)
            if (file.extension.lowercase() == "exe") {
                return file
            }
        }
        
        return null
    }
    
    /**
     * Stop a running game
     */
    fun stopGame(process: Process?): Boolean {
        return try {
            process?.destroy()
            true
        } catch (e: Exception) {
            false
        }
    }
}
