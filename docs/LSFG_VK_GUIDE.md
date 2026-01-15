# LSFG-VK Integration Guide

## What is LSFG-VK?

LSFG-VK (Lossless Scaling Frame Generation for Vulkan) is an open-source project that brings advanced frame generation and upscaling to Linux using the Vulkan graphics API. It works similarly to technologies like NVIDIA DLSS 3 Frame Generation or AMD FSR 3, but supports a wider range of GPUs.

## How It Works

LSFG-VK intercepts rendered frames from Vulkan-based games and:
1. Analyzes consecutive frames
2. Generates interpolated frames between actual game frames
3. Injects these frames for smoother output
4. Applies upscaling algorithms for better visual quality

## Features in This App

### Frame Generation
- **1x Multiplier**: Frame generation disabled (native frame rate)
- **2x Multiplier**: Doubles the perceived frame rate by adding 1 interpolated frame
- **3x Multiplier**: Triples frame rate with 2 interpolated frames
- **4x Multiplier**: Quadruples frame rate with 3 interpolated frames

### Benefits
- **Higher Frame Rates**: Games locked at 30 FPS can appear to run at 60, 90, or 120 FPS
- **Smoother Gameplay**: Reduced perceived stuttering and judder
- **Better Visual Quality**: Upscaling algorithms improve image quality
- **Hardware Agnostic**: Works on any Vulkan-capable GPU

## System Requirements

### Minimum Requirements
- ARM Android device with Vulkan support
- FEX-Emu configured and working
- Vulkan-based games or DirectX games with DXVK

### Recommended
- Device with strong GPU for best results
- Games that support Vulkan natively
- At least 4GB RAM

## Configuration

### Enabling LSFG-VK

1. Open the app and go to **Settings**
2. Scroll to **Graphics Enhancement** section
3. Toggle **Enable LSFG-VK Frame Generation**
4. Adjust the **Frame Generation** multiplier slider:
   - **1x**: Frame generation disabled (native rendering)
   - **2x**: Double the frame rate
   - **3x**: Triple the frame rate
   - **4x**: Quadruple the frame rate
5. Settings are saved automatically

### Environment Variables

When enabled, the following environment variables are set for game launches:

```bash
ENABLE_LSFG=1
LSFG_MULTIPLIER=2  # Or 3, 4 based on your setting
```

## Compatibility

### Compatible Games
✅ Vulkan-based Linux games
✅ DirectX 11/12 games with DXVK translation
✅ Some emulators with Vulkan backend
✅ Games using Proton/Wine with Vulkan

### Not Compatible
❌ Native OpenGL games (unless using Zink)
❌ Native 32-bit Linux games
❌ Games with incompatible anti-cheat systems

## Performance Considerations

### Impact on Performance

**Pros:**
- Visually smoother gameplay
- Can make 30 FPS games feel like 60+ FPS
- Better perceived responsiveness

**Cons:**
- Slight increase in input latency
- Requires additional GPU resources
- May cause artifacts in fast-moving scenes

### Optimization Tips

1. **Start with 2x**: Begin with the 2x multiplier (or disable with 1x) and increase if performance allows
2. **Monitor GPU Usage**: High GPU usage may cause overheating on mobile devices
3. **Game-Specific Testing**: Not all games benefit equally from frame generation
4. **Disable for Fast-Paced Games**: Competitive games may suffer from input latency

## Troubleshooting

### Common Issues

**LSFG-VK Not Working**
- Verify Vulkan is supported on your device
- Check that the game uses Vulkan or DXVK
- Ensure FEX-Emu is properly configured

**Visual Artifacts**
- Lower the multiplier (4x → 3x → 2x)
- Try different games to see if issue is game-specific
- Update your device's GPU drivers

**Performance Degradation**
- Frame generation requires GPU power
- Lower the multiplier setting
- Close background apps
- Consider cooling solutions for sustained gaming

**High Input Latency**
- Disable LSFG-VK for competitive games
- Use lower multiplier (2x instead of 4x)
- Some latency is inherent to frame generation

## Best Use Cases

### Ideal Scenarios
- **Single-player story games**: Where smoothness matters more than latency
- **Cinematic experiences**: Enhanced visual quality for immersive games
- **Locked frame rate games**: Breaking through 30/60 FPS caps
- **Older games**: Making classics feel more modern

### Not Recommended For
- **Competitive multiplayer**: Input latency can affect competitive performance
- **Rhythm games**: Timing-critical games may feel off
- **Games already hitting high frame rates**: Diminishing returns

## Advanced Configuration

### LSFG-VK Additional Options

While the app provides basic configuration, LSFG-VK supports additional options that advanced users can set via rootfs configuration:

```bash
# Example advanced environment variables
LSFG_UPSCALER=FSR        # FSR, NIS, LS1, xBR, Anime4K
LSFG_SHARPNESS=0.5       # 0.0 to 1.0
LSFG_QUALITY=balanced    # performance, balanced, quality
```

**Note**: These advanced options are not exposed in the app UI but can be configured manually in the FEX-Emu rootfs environment.

## Resources

### External Links
- [LSFG-VK GitHub Repository](https://github.com/PancakeTAS/lsfg-vk)
- [LSFG-VK Wiki](https://github.com/PancakeTAS/lsfg-vk/wiki)
- [Vulkan Documentation](https://www.vulkan.org/)
- [DXVK Project](https://github.com/doitsujin/dxvk)

### Related Reading
- FEX-Emu Integration Guide: See `FEX_EMU_GUIDE.md`
- Performance Optimization: See `IMPLEMENTATION_NOTES.md`

## Credits

LSFG-VK is an open-source project maintained by the community. This integration in the FEX Steam Launcher is for educational purposes and to enhance the gaming experience on ARM Android devices.

## Disclaimer

Frame generation technology adds interpolated frames and may not represent actual game performance. Input latency may be increased. Use responsibly and adjust settings based on your preferences and hardware capabilities.
