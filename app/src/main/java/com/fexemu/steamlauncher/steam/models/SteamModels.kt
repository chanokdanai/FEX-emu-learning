package com.fexemu.steamlauncher.steam.models

import com.google.gson.annotations.SerializedName

/**
 * Steam Game data model
 */
data class SteamGame(
    @SerializedName("appid")
    val appId: Long,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("playtime_forever")
    val playtimeForever: Int = 0,
    
    @SerializedName("img_icon_url")
    val iconUrl: String? = null,
    
    @SerializedName("img_logo_url")
    val logoUrl: String? = null,
    
    @SerializedName("has_community_visible_stats")
    val hasCommunityVisibleStats: Boolean? = null,
    
    // Local fields
    var isDownloaded: Boolean = false,
    var downloadProgress: Float = 0f,
    var installPath: String? = null,
    var lastPlayed: Long = 0
)

/**
 * Response from Steam API for owned games
 */
data class OwnedGamesResponse(
    @SerializedName("response")
    val response: GamesResponse
)

data class GamesResponse(
    @SerializedName("game_count")
    val gameCount: Int,
    
    @SerializedName("games")
    val games: List<SteamGame>
)

/**
 * Steam user authentication data
 */
data class SteamAuth(
    val steamId: String,
    val apiKey: String,
    val username: String?,
    val avatarUrl: String?
)

/**
 * Steam Cloud file metadata
 */
data class CloudFile(
    @SerializedName("filename")
    val filename: String,
    
    @SerializedName("size")
    val size: Long,
    
    @SerializedName("timestamp")
    val timestamp: Long,
    
    @SerializedName("sha")
    val sha: String,
    
    var localPath: String? = null,
    var isSynced: Boolean = false
)

/**
 * Cloud save response
 */
data class CloudSaveResponse(
    @SerializedName("response")
    val response: CloudResponse
)

data class CloudResponse(
    @SerializedName("files")
    val files: List<CloudFile>,
    
    @SerializedName("total_count")
    val totalCount: Int
)
