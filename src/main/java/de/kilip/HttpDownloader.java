package de.kilip;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class HttpDownloader {
    public static boolean downloadJar(String url, String file_name)
    {
        try(HttpClient client = HttpClient.newHttpClient())
        {
            var request = HttpRequest.newBuilder().uri(URI.create(url)).build();
            HttpResponse<byte[]> response = client.send(request, HttpResponse.BodyHandlers.ofByteArray());

            if (response.statusCode() == 200) {
                Path filePath = Paths.get(file_name);
                Files.write(filePath, response.body());
                return true;
            }

        } catch (IOException | InterruptedException _) {
        }
        return false;
    }
}
