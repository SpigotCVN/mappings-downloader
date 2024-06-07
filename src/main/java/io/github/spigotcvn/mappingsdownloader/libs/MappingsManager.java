package io.github.spigotcvn.mappingsdownloader.libs;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;

public class MappingsManager {
    private final JavaPlugin plugin;
    private final String minecraftVersion;
    private final String mappingsFolder;

    public MappingsManager(JavaPlugin plugin, String minecraftVersion, String mappingsFolder) {
        this.plugin = plugin;
        this.minecraftVersion = minecraftVersion;
        this.mappingsFolder = mappingsFolder;
    }

    public File downloadCorrectMapping() throws IOException {
        plugin.getLogger().info("Downloading mappings for " + minecraftVersion + "...");

        String fileName = minecraftVersion + ".tiny";
        URL downloadUrl = new URL("https://raw.githubusercontent.com/Cross-Version-NMS/CVN-mappings/main/mappings/" + fileName);

        Files.createDirectories(Paths.get(mappingsFolder));

        return download(downloadUrl, mappingsFolder + "/" + fileName);
    }

    public File download(URL downloadUrl, String path) throws IOException {
        File mappingFile = new File(path);

        try (InputStream inputStream = downloadUrl.openStream();
             OutputStream outputStream = Files.newOutputStream(mappingFile.toPath())) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }

        plugin.getLogger().info("Finished downloading mappings for " + minecraftVersion + " !");

        return mappingFile;
    }
}
