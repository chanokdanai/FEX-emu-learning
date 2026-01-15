# User Guide - FEX Steam Launcher

Welcome to FEX Steam Launcher! This guide will help you get started with running your Steam games on Android using FEX-Emu.

## Table of Contents
1. [First-Time Setup](#first-time-setup)
2. [Logging in to Steam](#logging-in-to-steam)
3. [Browsing Your Game Library](#browsing-your-game-library)
4. [Downloading Games](#downloading-games)
5. [Playing Games](#playing-games)
6. [Managing Cloud Saves](#managing-cloud-saves)
7. [Configuring FEX-Emu](#configuring-fex-emu)
8. [Troubleshooting](#troubleshooting)

## First-Time Setup

### Requirements
- Android device with ARM64 processor
- Android 8.0 (API 26) or higher
- At least 4GB of free storage space
- Active internet connection
- Steam account with owned games

### Installation
1. Download the APK from the releases page
2. Enable "Install from Unknown Sources" in your Android settings
3. Install the APK
4. Grant required permissions when prompted

## Logging in to Steam

### Getting Your Steam Credentials

1. **Find Your Steam ID:**
   - Visit https://steamid.io/
   - Enter your Steam profile URL
   - Copy your 64-bit Steam ID (steamID64)

2. **Get Steam API Key:**
   - Visit https://steamcommunity.com/dev/apikey
   - Log in to your Steam account
   - Register for an API key (use any domain name)
   - Copy your API key

### Login Process

1. Launch FEX Steam Launcher
2. On the login screen, enter:
   - Your Steam ID in the first field
   - Your API Key in the second field
3. Tap "Login"
4. You'll be redirected to your game library

**Security Note:** Your credentials are stored locally on your device and only used to communicate with Steam's official API.

## Browsing Your Game Library

### Main Screen

After logging in, you'll see your game library with:
- Game icons and names
- Total playtime for each game
- Download status

### Refreshing Your Library

To sync your library with Steam:
- Pull down on the screen, OR
- Tap the refresh button (floating action button)

This will fetch your latest game list from Steam.

## Downloading Games

### Starting a Download

1. Tap on any game in your library
2. On the game detail screen, tap "Download"
3. The download will start in the background

### Monitoring Progress

- A notification will show download progress
- The game detail screen shows a progress bar
- Downloads continue even if you close the app

### Download Location

Games are downloaded to:
```
/sdcard/Android/data/com.fexemu.steamlauncher/files/games/[gameId]/
```

**Note:** In the current version, you need to manually provide game files as Steam doesn't provide direct download links through their Web API.

## Playing Games

### Requirements Before Playing

1. **FEX-Emu Configuration:** Set up in Settings
2. **Linux Rootfs:** Download and configure a rootfs
3. **Game Downloaded:** Game files must be present

### Launching a Game

1. Open the game from your library
2. Ensure the game is downloaded (progress = 100%)
3. Tap "Play"
4. FEX-Emu will launch the game

### During Gameplay

- The game runs in a separate process
- You can switch back to the app
- To exit, close the game from Android's recent apps

## Managing Cloud Saves

### What are Cloud Saves?

Steam Cloud automatically stores your game progress, settings, and saves in the cloud.

### Syncing Saves

1. Open a game's detail page
2. Tap "Sync Saves"
3. The app will:
   - Download cloud saves to your device
   - Upload local saves to Steam Cloud
   - Resolve any conflicts (newest wins)

### When to Sync

- Before playing on a new device
- After finishing a gaming session
- To backup your progress

### Save Location

Local saves are stored at:
```
/sdcard/Android/data/com.fexemu.steamlauncher/files/saves/[gameId]/
```

## Configuring FEX-Emu

### Accessing Settings

1. Tap the menu icon (three dots) in the top right
2. Select "Settings"

### FEX-Emu Options

**Enable JIT:**
- ✅ Recommended: Better performance
- ❌ Disable for compatibility issues

**Enable Thunking:**
- ✅ Recommended: Better native library support
- ❌ Disable if games crash on start

### Setting Up Rootfs

A rootfs (root filesystem) is a Linux distribution that games run in.

**Method 1: Download**
1. Find a compatible ARM64 Linux rootfs (Ubuntu, Debian)
2. Enter the download URL in Settings
3. Tap "Download Rootfs"
4. Wait for download and extraction

**Method 2: Manual**
1. Download an ARM64 Linux rootfs archive
2. Extract to: `/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/rootfs/`
3. Ensure it contains standard Linux directories (bin, lib, usr, etc.)

## Troubleshooting

### Can't Log In

**Problem:** Login fails or shows error message

**Solutions:**
- Verify your Steam ID is correct (64-bit format)
- Check your API key is valid
- Ensure you have internet connection
- Try regenerating your API key

### Game Won't Download

**Problem:** Download doesn't start or fails

**Solutions:**
- Check available storage space
- Verify internet connection
- Check file permissions
- In current version, you may need to manually add game files

### Game Won't Launch

**Problem:** "Play" button doesn't work or game crashes

**Solutions:**
- Ensure FEX-Emu is configured (check Settings)
- Verify rootfs is set up correctly
- Check that game executable exists
- Enable logging to see error messages
- Try disabling JIT or thunking in Settings

### Poor Performance

**Problem:** Game runs slowly or lags

**Solutions:**
- Enable JIT compilation in Settings
- Close other apps
- Try a lighter Linux rootfs
- Check if your device meets requirements
- Some games may not be optimized for emulation

### Cloud Sync Fails

**Problem:** Saves won't sync with Steam Cloud

**Solutions:**
- Check internet connection
- Verify API key is valid
- Ensure the game supports Steam Cloud
- Try syncing again later

### Storage Issues

**Problem:** Running out of space

**Solutions:**
- Delete unused games
- Clear app cache
- Move to device with more storage
- Check if external SD card is available

## Advanced Tips

### Performance Optimization

1. **Use internal storage** for better read/write speeds
2. **Close background apps** to free up RAM
3. **Adjust game settings** to lower graphics quality
4. **Keep device cool** to prevent thermal throttling

### Game Compatibility

Not all games work perfectly:
- ✅ Best: Older, simpler games
- ⚠️ May work: Modern games with lower requirements
- ❌ Unlikely: Games with anti-cheat or heavy DRM

### Backup Your Data

Regularly backup:
- Your game saves (automatically with Cloud Sync)
- Downloaded game files
- FEX-Emu configuration

## Getting Help

### Resources

- Check the troubleshooting section above
- Read the documentation in the `docs/` folder
- Visit FEX-Emu documentation: https://wiki.fex-emu.org/
- Steam API docs: https://partner.steamgames.com/doc/webapi

### Reporting Issues

When reporting problems, include:
- Device model and Android version
- Game name and ID
- Steps to reproduce
- Error messages or logs
- Screenshots if applicable

## FAQ

**Q: Is this official?**
A: No, this is an unofficial community project.

**Q: Can I get banned?**
A: Using Steam's official Web API shouldn't result in bans, but use at your own risk.

**Q: Does this download games automatically?**
A: The current version requires manual game file management as Steam doesn't provide direct downloads through the Web API.

**Q: What games are compatible?**
A: Any x86/x86_64 game that FEX-Emu supports. Compatibility varies.

**Q: Is it safe?**
A: The app uses official Steam APIs and stores data locally. Always download from trusted sources.

**Q: Can I play multiplayer?**
A: Potentially, but performance may vary. Anti-cheat systems often don't work.

## Contact & Support

For more information:
- GitHub: https://github.com/chanokdanai/FEX-emu-learning
- FEX-Emu: https://fex-emu.com/

---

**Disclaimer:** This app is not affiliated with, endorsed by, or connected to Valve Corporation or Steam.
