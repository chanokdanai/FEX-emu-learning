package com.fexemu.steamlauncher.steam

import android.content.Context
import android.content.SharedPreferences
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.fexemu.steamlauncher.steam.models.SteamAuth
import com.fexemu.steamlauncher.steam.models.SteamGame
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Manages Steam authentication and game library
 */
class SteamManager(private val context: Context) {
    
    private val prefs: SharedPreferences by lazy {
        try {
            val masterKey = MasterKey.Builder(context)
                .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                .build()
            
            EncryptedSharedPreferences.create(
                context,
                "steam_prefs_encrypted",
                masterKey,
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
        } catch (e: Exception) {
            // Fallback to regular SharedPreferences if encryption fails
            context.getSharedPreferences("steam_prefs", Context.MODE_PRIVATE)
        }
    }
    
    private val PREF_STEAM_ID = "steam_id"
    private val PREF_API_KEY = "api_key"
    private val PREF_USERNAME = "username"
    private val PREF_AVATAR = "avatar_url"
    
    /**
     * Save Steam authentication credentials
     */
    fun saveAuth(auth: SteamAuth) {
        prefs.edit().apply {
            putString(PREF_STEAM_ID, auth.steamId)
            putString(PREF_API_KEY, auth.apiKey)
            putString(PREF_USERNAME, auth.username)
            putString(PREF_AVATAR, auth.avatarUrl)
            apply()
        }
    }
    
    /**
     * Get saved Steam authentication
     */
    fun getAuth(): SteamAuth? {
        val steamId = prefs.getString(PREF_STEAM_ID, null) ?: return null
        val apiKey = prefs.getString(PREF_API_KEY, null) ?: return null
        
        return SteamAuth(
            steamId = steamId,
            apiKey = apiKey,
            username = prefs.getString(PREF_USERNAME, null),
            avatarUrl = prefs.getString(PREF_AVATAR, null)
        )
    }
    
    /**
     * Check if user is authenticated
     */
    fun isAuthenticated(): Boolean = getAuth() != null
    
    /**
     * Clear authentication data
     */
    fun logout() {
        prefs.edit().clear().apply()
    }
    
    /**
     * Fetch owned games from Steam
     */
    suspend fun fetchOwnedGames(): Result<List<SteamGame>> = withContext(Dispatchers.IO) {
        try {
            val auth = getAuth() ?: return@withContext Result.failure(
                Exception("Not authenticated")
            )
            
            val response = SteamApiClient.api.getOwnedGames(
                apiKey = auth.apiKey,
                steamId = auth.steamId
            )
            
            if (response.isSuccessful) {
                val games = response.body()?.response?.games ?: emptyList()
                Result.success(games)
            } else {
                Result.failure(Exception("Failed to fetch games: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
