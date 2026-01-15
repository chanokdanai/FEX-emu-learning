# Example Configurations

## FEX-Emu Configuration

Example FEX-Emu config that can be placed at:
`/sdcard/Android/data/com.fexemu.steamlauncher/files/.fex-emu/Config.json`

```json
{
  "Core": {
    "Threads": 8,
    "JIT": true,
    "UnifiedMemory": true,
    "MemorySize": "8G"
  },
  "CPU": {
    "Cores": 8,
    "Model": "ARMv8.2"
  },
  "Logging": {
    "LogLevel": "INFO",
    "LogToFile": true,
    "LogFile": "/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/fex.log"
  },
  "Thunking": {
    "Enabled": true,
    "LibraryPath": "/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/thunks"
  },
  "RootFS": {
    "Path": "/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/rootfs"
  }
}
```

## Per-Game Configuration

Example game-specific configuration for better compatibility:

### High-Performance Game
```json
{
  "gameId": 730,
  "gameName": "Counter-Strike: Global Offensive",
  "fexConfig": {
    "jit": true,
    "thunking": true,
    "cpuCores": 8,
    "memoryLimit": "4G"
  },
  "launchOptions": [
    "-novid",
    "-high",
    "-threads 8"
  ],
  "environment": {
    "WINE_CPU_TOPOLOGY": "8:8",
    "MESA_GL_VERSION_OVERRIDE": "4.6"
  }
}
```

### Compatibility Mode Game
```json
{
  "gameId": 12345,
  "gameName": "Older Game",
  "fexConfig": {
    "jit": false,
    "thunking": false,
    "cpuCores": 4,
    "memoryLimit": "2G"
  },
  "launchOptions": [
    "-windowed",
    "-safe"
  ],
  "environment": {
    "WINE_CPU_TOPOLOGY": "4:4"
  }
}
```

## Rootfs Download Sources

### Ubuntu ARM64 RootFS
```bash
# Ubuntu 22.04 ARM64 Base
https://cdimage.ubuntu.com/ubuntu-base/releases/22.04/release/ubuntu-base-22.04-base-arm64.tar.gz

# Extract command:
tar -xzf ubuntu-base-22.04-base-arm64.tar.gz -C /path/to/rootfs/
```

### Debian ARM64 RootFS
```bash
# Debian Bookworm ARM64
https://github.com/FEX-Emu/RootFS/releases/download/Debian-Bookworm/debian-bookworm-rootfs.tar.gz

# Extract command:
tar -xzf debian-bookworm-rootfs.tar.gz -C /path/to/rootfs/
```

## Steam API Configuration

### API Call Examples

#### Get Owned Games
```bash
curl "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?\
key=YOUR_API_KEY&\
steamid=YOUR_STEAM_ID&\
include_appinfo=1&\
include_played_free_games=1&\
format=json"
```

#### Get Cloud Files
```bash
curl "https://api.steampowered.com/ISteamRemoteStorage/EnumerateUserPublishedFiles/v0001/?\
key=YOUR_API_KEY&\
steamid=YOUR_STEAM_ID&\
appid=GAME_APP_ID&\
format=json"
```

## Android Manifest Permissions

Required permissions explained:

```xml
<!-- Internet access for Steam API -->
<uses-permission android:name="android.permission.INTERNET" />

<!-- Check network connectivity -->
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />

<!-- Storage for games and saves (Android 12 and below) -->
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE" 
    android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />

<!-- Manage external storage (Android 11+) -->
<uses-permission android:name="android.permission.MANAGE_EXTERNAL_STORAGE" />

<!-- Background downloads and sync -->
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />

<!-- Notifications for download progress -->
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

## Gradle Build Configuration

### Recommended Build Settings

```gradle
android {
    compileSdk 34
    
    defaultConfig {
        minSdk 26
        targetSdk 34
        
        // Recommended for games
        ndk {
            abiFilters 'arm64-v8a'
        }
    }
    
    buildTypes {
        release {
            minifyEnabled true
            shrinkResources true
            proguardFiles getDefaultProguardFile('proguard-android-optimize.txt')
        }
        
        debug {
            applicationIdSuffix ".debug"
            debuggable true
        }
    }
}
```

## Environment Variables for FEX-Emu

Common environment variables for better compatibility:

```bash
# FEX-Emu specific
export FEX_ROOTFS="/path/to/rootfs"
export FEX_ENABLE_JIT=1
export FEX_CPU_CORES=8

# Graphics
export MESA_GL_VERSION_OVERRIDE="4.6"
export MESA_GLSL_VERSION_OVERRIDE="460"

# Wine/Proton (if using Windows games)
export WINE_CPU_TOPOLOGY="8:8"
export WINEPREFIX="/path/to/wine/prefix"
export WINEDEBUG="-all"

# Performance
export __GL_THREADED_OPTIMIZATIONS=1
export RADV_PERFTEST=aco
```

## Game Launch Scripts

### Basic Launch Script
```bash
#!/bin/bash
GAME_DIR="/sdcard/Android/data/com.fexemu.steamlauncher/files/games/$1"
ROOTFS="/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/rootfs"

fex-emu \
  --rootfs "$ROOTFS" \
  --jit \
  -- "$GAME_DIR/game.exe"
```

### Advanced Launch Script with Wine
```bash
#!/bin/bash
GAME_ID="$1"
GAME_DIR="/sdcard/Android/data/com.fexemu.steamlauncher/files/games/$GAME_ID"
ROOTFS="/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/rootfs"
WINEPREFIX="/sdcard/Android/data/com.fexemu.steamlauncher/files/wine/$GAME_ID"

export WINEPREFIX
export WINEDEBUG="-all"

fex-emu \
  --rootfs "$ROOTFS" \
  --jit \
  --env WINEPREFIX="$WINEPREFIX" \
  -- wine "$GAME_DIR/game.exe"
```

## Debugging Configuration

### Enable Debug Logging

In Kotlin code:
```kotlin
// SteamApiClient.kt
private val loggingInterceptor = HttpLoggingInterceptor().apply {
    level = if (BuildConfig.DEBUG) {
        HttpLoggingInterceptor.Level.BODY
    } else {
        HttpLoggingInterceptor.Level.NONE
    }
}
```

### Logcat Filters
```bash
# View all app logs
adb logcat | grep "com.fexemu.steamlauncher"

# View only errors
adb logcat *:E | grep "com.fexemu.steamlauncher"

# View FEX-Emu logs
adb logcat | grep "FEXEmu"

# View Steam API logs
adb logcat | grep "SteamApi"
```

## Performance Profiling

### Monitor CPU Usage
```bash
adb shell top | grep com.fexemu.steamlauncher
```

### Monitor Memory
```bash
adb shell dumpsys meminfo com.fexemu.steamlauncher
```

### Network Traffic
```bash
adb shell dumpsys package com.fexemu.steamlauncher | grep "Network"
```

## Testing Configurations

### Test Steam API Connection
```bash
# Replace with your credentials
API_KEY="YOUR_KEY"
STEAM_ID="YOUR_ID"

curl -v "https://api.steampowered.com/IPlayerService/GetOwnedGames/v0001/?key=$API_KEY&steamid=$STEAM_ID&format=json"
```

### Test File Permissions
```bash
adb shell run-as com.fexemu.steamlauncher ls -la /data/data/com.fexemu.steamlauncher/
adb shell ls -la /sdcard/Android/data/com.fexemu.steamlauncher/files/
```

### Test FEX-Emu Installation
```bash
# Check if FEX-Emu binary is accessible
adb shell "which fex-emu"

# Test basic execution
adb shell "fex-emu --version"
```

## Recommended Device Settings

For optimal performance:

1. **Developer Options:**
   - Force GPU rendering: ON
   - Disable hardware overlays: ON
   - Force 4x MSAA: OFF (for performance)

2. **Battery:**
   - Performance mode
   - Disable battery optimization for the app

3. **Storage:**
   - Use internal storage for games
   - Keep at least 2GB free space

4. **Network:**
   - Wi-Fi preferred for downloads
   - Disable data saver

## Common Game Configurations

### Source Engine Games (Half-Life, Portal, etc.)
```bash
-novid -console -high +mat_queue_mode 2
```

### Unity Games
```bash
-screen-fullscreen 0 -screen-width 1280 -screen-height 720
```

### Unreal Engine Games
```bash
-dx11 -USEALLAVAILABLECORES
```

These examples provide templates for configuring various aspects of the FEX Steam Launcher application.
