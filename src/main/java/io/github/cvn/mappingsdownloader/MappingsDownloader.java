package io.github.cvn.mappingsdownloader;

import io.github.cvn.mappingsdownloader.libs.MappingsManager;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MappingsDownloader {
    public JavaPlugin plugin;
    public FileConfiguration config;

    private String minecraftVersion;
    private boolean tried = false;

    public MappingsDownloader(FileConfiguration config, JavaPlugin plugin) {
        this.plugin = plugin;
        this.config = config;

        minecraftVersion = getMinecraftVersion();

        tryDownload();
    }

    private void tryDownload() {
        MappingsManager mappingsManager = new MappingsManager(plugin, config, minecraftVersion);

        try {
            mappingsManager.downloadCorrectMapping();
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to download mappings for minecraft " + minecraftVersion + " !");

            if(tried) return;

            minecraftVersion = getServerMinecraftVersion();
            plugin.getLogger().info("Trying with automatic for " + minecraftVersion + "...");

            tryDownload();

            tried = true;

            config.set("minecraft-version", "");
        }
    }

    private String getMinecraftVersion() {
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
    private String getServerMinecraftVersion() {
        String bukkitGetVersionOutput = Bukkit.getVersion();
        Matcher matcher = Pattern.compile("\\(MC: (?<version>\\d+\\.\\d+(\\.\\d+)?)\\)").matcher(bukkitGetVersionOutput);
        if (matcher.find()) {
            return minecraftVersion = matcher.group("version");
        } else {
            throw new RuntimeException("Could not determine Minecraft version from Bukkit.getVersion(): " + bukkitGetVersionOutput);
        }
    }
}