# Architecture Documentation

## System Architecture Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                         Android UI Layer                        │
│  ┌─────────────┐  ┌──────────────┐  ┌─────────────────────┐   │
│  │   Login     │  │   Game       │  │   Game Detail &     │   │
│  │   Activity  │─▶│   Library    │─▶│   Settings          │   │
│  └─────────────┘  └──────────────┘  └─────────────────────┘   │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                      Business Logic Layer                       │
│  ┌──────────────┐  ┌─────────────┐  ┌──────────────────────┐  │
│  │   Steam      │  │   Cloud     │  │   FEX-Emu            │  │
│  │   Manager    │  │   Manager   │  │   Manager            │  │
│  └──────┬───────┘  └──────┬──────┘  └──────────┬───────────┘  │
│         │                 │                     │               │
│  ┌──────▼───────┐  ┌──────▼──────┐  ┌──────────▼───────────┐  │
│  │  Download    │  │   Cloud     │  │   Game               │  │
│  │  Service     │  │   Sync Svc  │  │   Launcher           │  │
│  └──────────────┘  └─────────────┘  └──────────────────────┘  │
└────────────────────────────┬────────────────────────────────────┘
                             │
┌────────────────────────────▼────────────────────────────────────┐
│                         Data Layer                              │
│  ┌──────────────┐  ┌─────────────┐  ┌──────────────────────┐  │
│  │   Steam      │  │   Local     │  │   Shared             │  │
│  │   Web API    │  │   Storage   │  │   Preferences        │  │
│  │  (Retrofit)  │  │   (Files)   │  │   (Config)           │  │
│  └──────────────┘  └─────────────┘  └──────────────────────┘  │
└─────────────────────────────────────────────────────────────────┘
```

## Component Details

### UI Layer (Android Activities)

#### MainActivity
- **Purpose**: Main game library browser
- **Responsibilities**:
  - Display list of owned Steam games
  - Handle refresh actions
  - Navigate to game details
  - Show download progress

#### SteamLoginActivity  
- **Purpose**: Steam authentication
- **Responsibilities**:
  - Collect Steam ID and API Key
  - Validate credentials
  - Save authentication data
  - Redirect to main screen

#### GameDetailActivity
- **Purpose**: Individual game management
- **Responsibilities**:
  - Show game information
  - Start game downloads
  - Launch games with FEX-Emu
  - Trigger cloud save sync

#### SettingsActivity
- **Purpose**: App and FEX-Emu configuration
- **Responsibilities**:
  - Configure FEX-Emu options
  - Manage rootfs downloads
  - Adjust performance settings

### Business Logic Layer

#### SteamManager
```kotlin
class SteamManager {
    - saveAuth(): Store credentials
    - getAuth(): Retrieve credentials
    - isAuthenticated(): Check login status
    - fetchOwnedGames(): Get game library
    - logout(): Clear session
}
```

#### CloudSaveManager
```kotlin
class CloudSaveManager {
    - getCloudFiles(): Fetch cloud saves list
    - downloadCloudSave(): Download specific save
    - uploadCloudSave(): Upload local save
    - syncGameSaves(): Full bidirectional sync
}
```

#### FexEmuManager
```kotlin
class FexEmuManager {
    - initialize(): Set up FEX-Emu environment
    - isConfigured(): Check if ready
    - getConfig(): Get current settings
    - launchGame(): Execute game with FEX-Emu
    - setupRootfs(): Download and configure rootfs
}
```

#### GameLauncher
```kotlin
class GameLauncher {
    - launchGame(): Prepare and launch game
    - findGameExecutable(): Locate game binary
    - stopGame(): Terminate running game
}
```

### Background Services

#### DownloadService
- **Type**: Foreground Service
- **Purpose**: Background game downloads
- **Features**:
  - Persistent notifications
  - Progress tracking
  - Resume capability
  - Network error handling

#### CloudSyncService
- **Type**: Foreground Service  
- **Purpose**: Cloud save synchronization
- **Features**:
  - Background sync
  - Conflict resolution
  - Upload/download management
  - Status notifications

### Data Layer

#### Steam Web API (Retrofit)
```kotlin
interface SteamApi {
    suspend fun getOwnedGames(...)
    suspend fun getCloudFiles(...)
}
```

**Endpoints Used**:
- `IPlayerService/GetOwnedGames`
- `ISteamRemoteStorage/EnumerateUserPublishedFiles`

#### Local Storage Structure
```
/sdcard/Android/data/com.fexemu.steamlauncher/files/
├── fex-emu/
│   ├── rootfs/           # Linux root filesystem
│   └── config/           # FEX configuration
├── games/
│   └── [gameId]/         # Per-game installation
└── saves/
    └── [gameId]/         # Per-game cloud saves
```

#### SharedPreferences
```
steam_prefs:
  - steam_id: String
  - api_key: String  
  - username: String?
  - avatar_url: String?
```

## Data Flow Examples

### Game Library Sync

```
User pulls to refresh
    ↓
MainActivity.loadGames()
    ↓
SteamManager.fetchOwnedGames()
    ↓
SteamApiClient → GET /IPlayerService/GetOwnedGames
    ↓
Response → List<SteamGame>
    ↓
GamesAdapter updates RecyclerView
```

### Game Launch Sequence

```
User taps Play
    ↓
GameDetailActivity.playGame()
    ↓
GameLauncher.launchGame()
    ↓
1. Check FEX-Emu configured
2. Verify rootfs exists
3. Find game executable
    ↓
FexEmuManager.launchGame()
    ↓
Build FEX-Emu command:
  fex-emu --rootfs /path/to/rootfs --jit -- /path/to/game.exe
    ↓
Execute process
    ↓
Game running in FEX-Emu
```

### Cloud Save Sync Flow

```
User taps Sync Saves
    ↓
GameDetailActivity.syncSaves()
    ↓
CloudSyncService.startSync()
    ↓
CloudSaveManager.syncGameSaves()
    ↓
1. Get cloud files list from Steam
2. Compare timestamps with local
3. Download newer cloud files
4. Upload newer local files
    ↓
Sync complete notification
```

## Threading Model

### Main Thread
- UI rendering
- User input handling
- View updates

### IO Thread (Coroutines)
```kotlin
lifecycleScope.launch {
    withContext(Dispatchers.IO) {
        // Network calls
        // File operations
        // Database access
    }
    // UI updates on Main
}
```

### Background Services
- DownloadService: Separate process
- CloudSyncService: Separate process
- Both with foreground notifications

## Security Considerations

### API Key Storage
- Stored in SharedPreferences (plain text)
- TODO: Use EncryptedSharedPreferences

### Network Security
- HTTPS for all API calls
- Certificate validation
- No sensitive data in logs (production)

### File Permissions
- Scoped storage (Android 10+)
- Request permissions at runtime
- Secure file access patterns

## Performance Optimizations

### Network
- Retrofit with OkHttp connection pooling
- Response caching
- Request timeout configuration

### Storage
- Efficient file streaming
- Background downloads
- Progress callbacks

### Memory
- RecyclerView with ViewHolder pattern
- Image loading optimization
- Proper lifecycle management

## Future Enhancements

### Short Term
- [ ] Add Room database for offline game cache
- [ ] Implement proper error recovery
- [ ] Add retry logic for failed operations

### Medium Term
- [ ] MVVM architecture with ViewModels
- [ ] Dependency injection (Hilt)
- [ ] Better state management (StateFlow)

### Long Term
- [ ] Full offline mode support
- [ ] Advanced FEX-Emu configurations
- [ ] Per-game performance profiles
- [ ] Screenshot and video capture
