# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.

# Keep FEX-emu native methods
-keepclasseswithmembernames class * {
    native <methods>;
}

# Keep Steam API models
-keep class com.fexemu.steamlauncher.steam.models.** { *; }
