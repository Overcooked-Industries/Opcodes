package de.kilip;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.jar.JarFile;

public class JarDownloader {
    public static void downloadJar(String url, String file_name) {
        try (HttpClient client = HttpClient.newHttpClient()) {
            var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                Path filePath = Paths.get(file_name);
                Files.write(filePath, response.body());
            }

        } catch (Exception _) {
        }
    }

    public static JarFile downloadIfAbsent(String url, String file_name) throws IOException {
        if (!Files.exists(Path.of(file_name))){
            JarDownloader.downloadJar(url, file_name);
        }
        return new JarFile(file_name);
    }

}
