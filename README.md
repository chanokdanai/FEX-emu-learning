# FEX Steam Launcher

An Android application that integrates Steam with FEX-Emu to run x86/x86_64 Steam games on ARM Android devices.

## Features

### 1. Steam Integration
- **Steam Account Connection**: Login with your Steam ID and API key
- **Game Library Sync**: Automatically sync your owned games from Steam
- **Game Downloads**: Download Steam games directly to your Android device
- **Steam Cloud Saves**: Sync game saves with Steam Cloud

### 2. FEX-Emu Integration
- **Game Execution**: Run x86/x86_64 games on ARM devices using FEX-Emu
- **Configurable Settings**: Enable/disable JIT compilation and thunking
- **LSFG-VK Graphics**: Frame generation and upscaling using Lossless Scaling for Vulkan
- **Linux Rootfs Support**: Download and configure a Linux distribution for game execution

### 3. User Interface
- **Game Library**: Browse your Steam games with playtime information
- **Game Details**: View detailed information and manage individual games
- **Settings**: Configure FEX-Emu and rootfs settings
- **Download Manager**: Background downloads with progress notifications

## Project Structure

```
app/
├── src/main/
│   ├── java/com/fexemu/steamlauncher/
│   │   ├── ui/                    # Activities and UI components
│   │   │   ├── MainActivity.kt    # Main game library screen
│   │   │   ├── SteamLoginActivity.kt  # Steam login screen
│   │   │   ├── GameDetailActivity.kt  # Game details screen
│   │   │   ├── SettingsActivity.kt    # Settings screen
│   │   │   └── GamesAdapter.kt        # RecyclerView adapter
│   │   ├── steam/                 # Steam API integration
│   │   │   ├── SteamApi.kt        # Retrofit API interface
│   │   │   ├── SteamApiClient.kt  # API client singleton
│   │   │   ├── SteamManager.kt    # Steam authentication & game management
│   │   │   ├── DownloadService.kt # Background download service
│   │   │   └── models/            # Data models
│   │   ├── cloud/                 # Steam Cloud integration
│   │   │   ├── CloudSaveManager.kt    # Cloud save management
│   │   │   └── CloudSyncService.kt    # Background sync service
│   │   └── fex/                   # FEX-Emu integration
│   │       ├── FexEmuManager.kt   # FEX-Emu configuration
│   │       └── GameLauncher.kt    # Game launching logic
│   ├── res/                       # Android resources
│   │   ├── layout/                # XML layouts
│   │   ├── values/                # Strings, colors, themes
│   │   └── menu/                  # Menu resources
│   └── AndroidManifest.xml        # App manifest
├── build.gradle                   # App build configuration
└── proguard-rules.pro            # ProGuard rules
```

## Setup Instructions

### Prerequisites

1. **Android Development Environment**
   - Android Studio Arctic Fox or newer
   - Android SDK API 26 or higher
   - Kotlin 1.9.0 or newer

2. **Steam API Key**
   - Get your Steam API key from: https://steamcommunity.com/dev/apikey
   - You'll need your Steam ID (find it at: https://steamid.io/)

### Building the App

1. Clone the repository:
   ```bash
   git clone https://github.com/chanokdanai/FEX-emu-learning.git
   cd FEX-emu-learning
   ```

2. Open the project in Android Studio

3. Sync Gradle files and build the project

4. Run on an ARM Android device or emulator (API 26+)

### First-Time Setup

1. **Login to Steam**
   - Launch the app
   - You'll see the enhanced Steam login screen with Steam branding
   - Enter your Steam ID
   - Enter your Steam API Key
   - Click "Login"

2. **Sync Game Library**
   - The app will automatically fetch your owned games
   - Pull down to refresh or tap the refresh button

3. **Configure FEX-Emu** (Optional)
   - Go to Settings from the menu
   - Configure JIT and thunking options
   - Enable LSFG-VK for frame generation and better graphics
   - Adjust frame generation multiplier (2x, 3x, or 4x)
   - Download a Linux rootfs if needed

## Usage

### Downloading Games

1. Select a game from your library
2. Tap "Download" on the game details screen
3. Monitor download progress in the notification area
4. Once downloaded, the "Play" button will be enabled

### Playing Games

1. Open a downloaded game
2. Tap "Play" to launch with FEX-Emu
3. The game will run in the configured Linux environment

### Syncing Cloud Saves

1. Open a game's detail page
2. Tap "Sync Saves" to sync with Steam Cloud
3. Saves will be downloaded/uploaded automatically

## Steam API Integration

The app uses the following Steam Web APIs:

- **IPlayerService/GetOwnedGames**: Fetch user's game library
- **ISteamRemoteStorage**: Manage cloud save files

### API Authentication

Authentication is handled via:
- Steam ID (unique user identifier)
- Steam Web API Key (developer key)

## FEX-Emu Integration

FEX-Emu is an x86/x86_64 emulator for ARM devices. This app integrates FEX-Emu to:

- Translate x86/x86_64 instructions to ARM
- Provide a Linux environment for Steam games
- Enable JIT compilation for better performance

### Configuration Options

- **Enable JIT**: Just-in-time compilation for improved performance
- **Enable Thunking**: Native library thunking for better compatibility
- **Rootfs**: Linux distribution root filesystem for game execution

## Technical Details

### Architecture

- **Language**: Kotlin
- **UI Framework**: Android Views with Material Design
- **Networking**: Retrofit + OkHttp
- **Async Operations**: Kotlin Coroutines
- **Storage**: SharedPreferences for settings, External storage for games

### Permissions

The app requires:
- `INTERNET`: For Steam API communication
- `ACCESS_NETWORK_STATE`: Check network connectivity
- `READ/WRITE_EXTERNAL_STORAGE`: Store games and saves
- `FOREGROUND_SERVICE`: Background downloads and syncing
- `POST_NOTIFICATIONS`: Download progress notifications

## Development

### Adding New Features

1. **New Steam API Endpoints**: Add to `SteamApi.kt`
2. **New UI Screens**: Create Activity in `ui/` package
3. **FEX-Emu Features**: Extend `FexEmuManager.kt`

### Testing

The app can be tested on:
- Physical ARM Android devices (API 26+)
- Android emulators with ARM system images

## Limitations

- Currently requires manual Steam API key entry (Steam doesn't provide OAuth for mobile)
- Game downloads require actual game files (not implemented in this version)
- FEX-Emu binary must be provided separately
- Some games may not be compatible with FEX-Emu

## Future Enhancements

- [x] Enhanced Steam login screen with branding
- [x] LSFG-VK frame generation support
- [ ] Steam Guard authentication support
- [ ] Better game compatibility checking
- [ ] Integrated FEX-Emu binary
- [ ] Performance profiling and optimization
- [ ] Controller support
- [ ] Screenshots and achievements sync
- [ ] Friends list integration

## Contributing

Contributions are welcome! Please:

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is for educational purposes as part of learning FEX-Emu translation and programming.

## Acknowledgments

- **FEX-Emu Team**: For the amazing x86/x86_64 emulator
- **Steam**: For providing the Web API
- **Android Community**: For libraries and tools used in this project

## Support

For issues and questions:
- Open an issue on GitHub
- Check FEX-Emu documentation: https://fex-emu.com/

## Disclaimer

This is an unofficial application and is not affiliated with, endorsed by, or connected to Valve Corporation or Steam. Steam and the Steam logo are trademarks of Valve Corporation.
