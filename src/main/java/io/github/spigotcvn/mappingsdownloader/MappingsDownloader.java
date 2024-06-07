package io.github.spigotcvn.mappingsdownloader;

import io.github.spigotcvn.mappingsdownloader.libs.MappingsManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.IOException;

public class MappingsDownloader {
    private final JavaPlugin plugin;
    private final String mappingsFolder;

    private final String minecraftVersion;

    public MappingsDownloader(JavaPlugin plugin, String mappingsFolder, String minecraftVersion) {
        this.plugin = plugin;
        this.mappingsFolder = mappingsFolder;

        this.minecraftVersion = minecraftVersion;
    }

    /**
     * Try to download the mappings for the current server version.
     * @return The downloaded file
     */
    public @Nullable File tryDownload() {
        MappingsManager mappingsManager = new MappingsManager(plugin, minecraftVersion, mappingsFolder);

        try {
            return mappingsManager.downloadCorrectMapping();
        } catch (IOException e) {
            plugin.getLogger().severe("Failed to download mappings for minecraft " + minecraftVersion + " !");

            return null;
        }
    }
}