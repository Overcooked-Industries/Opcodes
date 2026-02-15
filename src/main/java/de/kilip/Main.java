import de.kilip.HttpDownloader;

void main() throws IOException {
    String serverUrl = "https://piston-data.mojang.com/v1/objects/d3b86d840aa59a7512b6a028d40762cf6e492eeb/server.jar";
    if(!HttpDownloader.downloadJar(serverUrl, "server.jar")) return;
    var serverJar = new JarFile("server.jar");
    serverJar.entries().asIterator().forEachRemaining(jarEntry -> IO.println(jarEntry.getName()));
}


