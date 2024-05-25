package io.github.cvn.mappingsdownloader;

import io.github.cvn.mappingsdownloader.libs.MappingsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappingsDownloader {
    public JavaPlugin plugin;
    public FileConfiguration config;

    private String minecraftVersion;
    private boolean tried = false;

    public MappingsDownloader(JavaPlugin plugin, FileConfiguration config) {
        this.plugin = plugin;
        this.config = config;

        minecraftVersion = getMinecraftVersion();
    }

    /**
     * Try to download the mappings for the current server version.
     * @return The downloaded file
     */
    public @Nullable File tryDownload() {
        MappingsManager mappingsManager = new MappingsManager(plugin, config, minecraftVersion);

        try {
            return mappingsManager.downloadCorrectMapping();
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to download mappings for minecraft " + minecraftVersion + " !");

            if(tried) return null;

            minecraftVersion = getServerMinecraftVersion();
            plugin.getLogger().info("Trying with automatic for " + minecraftVersion + "...");

            tried = true;

            config.set("minecraft-version", "");

            return tryDownload();
        }
    }

    /**
     * Returns the cached version, the config version, or the version from {@link #getServerMinecraftVersion()}
     * @return Minecraft version
     */
    public String getMinecraftVersion() {
        if (minecraftVersion != null) {
            return minecraftVersion;
        }

        if(Objects.equals(config.getString("minecraft-version"), "")) {
            return minecraftVersion = getServerMinecraftVersion();
        }

        return minecraftVersion = config.getString("minecraft-version", getServerMinecraftVersion());
    }

    /**
     * Returns the actual running Minecraft version, e.g. 1.20 or 1.16.5
     *
     * @return Minecraft version
     */
    public String getServerMinecraftVersion() {
        String bukkitGetVersionOutput = Bukkit.getVersion();
        Matcher matcher = Pattern.compile("\\(MC: (?<version>\\d+\\.\\d+(\\.\\d+)?)\\)").matcher(bukkitGetVersionOutput);
        if (matcher.find()) {
            return minecraftVersion = matcher.group("version");
        } else {
            throw new RuntimeException("Could not determine Minecraft version from Bukkit.getVersion(): " + bukkitGetVersionOutput);
        }
    }
}