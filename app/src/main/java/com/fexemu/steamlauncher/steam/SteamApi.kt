package com.fexemu.steamlauncher.steam

import com.fexemu.steamlauncher.steam.models.*
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Steam Web API interface
 * Documentation: https://partner.steamgames.com/doc/webapi
 */
interface SteamApi {
    
    /**
     * Get list of owned games for a Steam user
     * API: IPlayerService/GetOwnedGames
     */
    @GET("IPlayerService/GetOwnedGames/v0001/")
    suspend fun getOwnedGames(
        @Query("key") apiKey: String,
        @Query("steamid") steamId: String,
        @Query("include_appinfo") includeAppInfo: Int = 1,
        @Query("include_played_free_games") includeFreegames: Int = 1,
        @Query("format") format: String = "json"
    ): Response<OwnedGamesResponse>
    
    /**
     * Get cloud save files for a game
     * API: ISteamRemoteStorage/GetUGCFileDetails
     */
    @GET("ISteamRemoteStorage/EnumerateUserPublishedFiles/v0001/")
    suspend fun getCloudFiles(
        @Query("key") apiKey: String,
        @Query("steamid") steamId: String,
        @Query("appid") appId: Long,
        @Query("format") format: String = "json"
    ): Response<CloudSaveResponse>
}
