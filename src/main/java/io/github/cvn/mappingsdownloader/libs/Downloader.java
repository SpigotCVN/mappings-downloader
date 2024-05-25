package io.github.cvn.mappingsdownloader.libs;

import java.io.*;
import java.net.URL;
import java.nio.file.Files;

public class Downloader {
    public static void download(URL downloadUrl, String path) throws IOException {
        File mappingFile = new File(path);

        try (InputStream inputStream = downloadUrl.openStream();
             OutputStream outputStream = Files.newOutputStream(mappingFile.toPath())) {

            byte[] buffer = new byte[1024];
            int bytesRead;

            while((bytesRead = inputStream.read(buffer)) != -1) {
                outputStream.write(buffer, 0, bytesRead);
            }
        }
    }
}