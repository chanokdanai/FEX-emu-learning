# FEX-Emu Integration Guide

## What is FEX-Emu?

FEX-Emu (Fast x86 Emulation) is a usermode x86/x86_64 emulator for ARM64 Linux systems. It allows running x86 applications and games on ARM devices.

## How It Works

1. **Instruction Translation**: Converts x86/x86_64 instructions to ARM64
2. **JIT Compilation**: Just-in-time compilation for performance
3. **Thunking**: Native library calls for better compatibility
4. **Rootfs**: Uses a Linux distribution root filesystem for execution

## Integration in This App

### FexEmuManager.kt

Manages FEX-Emu configuration and environment:

```kotlin
// Initialize FEX-Emu environment
fexEmuManager.initialize()

// Get configuration
val config = fexEmuManager.getConfig()

// Launch game with FEX-Emu
fexEmuManager.launchGame(gameId, gamePath)
```

### GameLauncher.kt

Handles game execution:

```kotlin
// Launch a game
gameLauncher.launchGame(gameId, gameName) { log ->
    // Handle log messages
}
```

## Configuration Options

### 1. JIT Compilation
- **Enable**: Better performance through just-in-time compilation
- **Disable**: Slower but more compatible

### 2. Thunking
- **Enable**: Use native libraries when possible
- **Disable**: Emulate all library calls

### 3. LSFG-VK Frame Generation
- **Enable**: Activate Lossless Scaling Frame Generation for Vulkan
- **Disable**: Use standard rendering
- **Multiplier**: Choose frame generation multiplier (1x-4x)
  - 2x: Doubles frame rate with interpolated frames
  - 3x: Triples frame rate
  - 4x: Quadruples frame rate

**Note**: LSFG-VK requires:
- Vulkan-compatible games or DXVK translation layer
- Device with Vulkan support
- May increase input latency slightly

### 4. CPU Cores
- Automatically detected from device
- Can be configured for performance tuning

### 5. Rootfs
- Linux distribution root filesystem
- Required for running games
- Common choices: Ubuntu, Debian

## Setting Up Rootfs

### Option 1: Download Pre-built
1. Go to Settings in the app
2. Enter a rootfs download URL
3. Tap "Download Rootfs"
4. Wait for download and extraction

### Option 2: Manual Setup
1. Download a compatible rootfs (ARM64 Linux distro)
2. Extract to: `/sdcard/Android/data/com.fexemu.steamlauncher/files/fex-emu/rootfs/`
3. Ensure proper permissions

## File Structure

```
/sdcard/Android/data/com.fexemu.steamlauncher/files/
├── fex-emu/
│   ├── rootfs/              # Linux distribution root filesystem
│   │   ├── bin/
│   │   ├── lib/
│   │   ├── usr/
│   │   └── ...
│   └── config/              # FEX-Emu configuration files
├── games/
│   ├── [gameId]/            # Each game has its own directory
│   │   ├── game.exe         # Game executable
│   │   └── ...
└── saves/
    └── [gameId]/            # Cloud saves per game
```

## Game Execution Flow

1. **Preparation**
   - Check FEX-Emu is configured
   - Verify rootfs exists
   - Find game executable

2. **Launch**
   - Build FEX-Emu command with parameters
   - Set up environment variables
   - Execute through FEX-Emu

3. **Runtime**
   - FEX-Emu translates x86 instructions
   - Game runs in Linux environment
   - Output is captured for logging

## Performance Optimization

### Tips for Better Performance

1. **Enable JIT**: Provides significant speed boost
2. **Use Thunking**: Reduces overhead for library calls
3. **Allocate More CPU Cores**: Better multithreading
4. **Use Fast Storage**: Install on internal storage
5. **Close Background Apps**: Free up system resources

## Compatibility

### Supported Game Types
- ✅ x86 Linux games
- ✅ x86_64 Linux games  
- ✅ Windows games with Wine/Proton
- ⚠️ Games with anti-cheat may not work
- ⚠️ Some DRM systems unsupported

### Known Limitations
- Performance lower than native ARM
- Some games require specific configurations
- OpenGL/Vulkan support depends on device

## Debugging

### Enable Logging
```kotlin
gameLauncher.launchGame(gameId, gameName) { log ->
    Log.d("FEXEmu", log)
    // Or display in UI
}
```

### Common Issues

**Game won't launch**
- Check rootfs is properly set up
- Verify game executable exists
- Check file permissions

**Poor performance**
- Enable JIT compilation
- Close other apps
- Check CPU/GPU usage

**Graphics issues**
- Update device drivers
- Try different graphics settings in game
- Check OpenGL/Vulkan support

## Advanced Configuration

### LSFG-VK Environment Variables
```kotlin
// LSFG-VK is automatically configured when enabled in settings
// The following environment variables are set:
val env = mapOf(
    "ENABLE_LSFG" to "1",
    "LSFG_MULTIPLIER" to "2"  // Or 3, 4 based on settings
)
```

### FEX-Emu Environment Variables
```kotlin
// Can be added to FexEmuManager
val env = mapOf(
    "FEX_ROOTFS" to rootfsPath,
    "FEX_ENABLE_JIT" to "1",
    "FEX_CPU_CORES" to cpuCores.toString()
)
```

### Custom FEX Config
Create `.fex-emu/Config.json` in app data directory

## Resources

- [FEX-Emu Official Site](https://fex-emu.com/)
- [FEX-Emu GitHub](https://github.com/FEX-Emu/FEX)
- [FEX-Emu Documentation](https://wiki.fex-emu.org/)

## Future Improvements

- [ ] Embedded FEX-Emu binary
- [ ] Automatic rootfs selection
- [ ] Per-game configurations
- [ ] Performance profiles
- [x] Graphics API translation (LSFG-VK)
- [ ] Integration with DXVK for DirectX games
