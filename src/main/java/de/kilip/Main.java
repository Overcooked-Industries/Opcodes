import de.kilip.JarDownloader;

void main() throws IOException {
    String url = "https://piston-data.mojang.com/v1/objects/ada715d3943e7584f04aca8ec44f5d3cd767353a/client.jar";
    String filename = "client.jar";
    var clientJar = JarDownloader.downloadIfAbsent(url, filename);
    clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
        if(!jarEntry.isDirectory() && jarEntry.getName().endsWith(".class")) {
            String name = Arrays.stream(jarEntry.getName().split("/")).toList().getLast();
            IO.println(name);
        }
    });
}


