# Steam API Documentation

## Overview

This app integrates with the Steam Web API to provide game library management and cloud save synchronization.

## Authentication

The app uses Steam Web API key authentication:

1. **Get your Steam ID**: Visit https://steamid.io/ and enter your Steam profile URL
2. **Get API Key**: Visit https://steamcommunity.com/dev/apikey to generate your key

## Endpoints Used

### 1. IPlayerService/GetOwnedGames

**Purpose**: Retrieve list of games owned by a Steam user

**Endpoint**: `https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/`

**Parameters**:
- `key`: Your Steam Web API key
- `steamid`: 64-bit Steam ID
- `include_appinfo`: (1/0) Include game name and logo
- `include_played_free_games`: (1/0) Include free games
- `format`: Response format (json)

**Response Example**:
```json
{
  "response": {
    "game_count": 100,
    "games": [
      {
        "appid": 730,
        "name": "Counter-Strike: Global Offensive",
        "playtime_forever": 12000,
        "img_icon_url": "...",
        "img_logo_url": "..."
      }
    ]
  }
}
```

### 2. ISteamRemoteStorage/EnumerateUserPublishedFiles

**Purpose**: Get cloud save files for a game

**Endpoint**: `https://api.steampowered.com/ISteamRemoteStorage/EnumerateUserPublishedFiles/v0001/`

**Parameters**:
- `key`: Your Steam Web API key
- `steamid`: 64-bit Steam ID
- `appid`: Application ID
- `format`: Response format (json)

**Response Example**:
```json
{
  "response": {
    "total_count": 5,
    "files": [
      {
        "filename": "savegame.sav",
        "size": 1024,
        "timestamp": 1640000000,
        "sha": "abc123..."
      }
    ]
  }
}
```

## Implementation in App

### SteamApi.kt
Defines Retrofit interface for API calls

### SteamApiClient.kt
Singleton that provides configured Retrofit instance

### SteamManager.kt
High-level manager for Steam operations:
- Authentication management
- Game library fetching
- User session handling

## Rate Limiting

Steam API has rate limits:
- 100,000 calls per day per API key
- Recommended: Cache results and refresh periodically

## Error Handling

Common error codes:
- `401 Unauthorized`: Invalid or missing API key
- `403 Forbidden`: API key doesn't have permission
- `429 Too Many Requests`: Rate limit exceeded
- `500 Internal Server Error`: Steam API issue

## Best Practices

1. **Cache game library**: Don't fetch on every app launch
2. **Refresh periodically**: Use pull-to-refresh for manual updates
3. **Handle errors gracefully**: Show user-friendly messages
4. **Respect rate limits**: Implement exponential backoff

## Security

- Store API key securely (SharedPreferences in this implementation)
- Never commit API keys to version control
- Consider encrypting sensitive data
- Use HTTPS for all API calls

## References

- [Steam Web API Documentation](https://partner.steamgames.com/doc/webapi)
- [Steam Web API Terms](https://steamcommunity.com/dev/apiterms)
