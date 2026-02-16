import de.kilip.JarDownloader;
import de.kilip.OpCodes;

void main() throws IOException {
    String url = "https://piston-data.mojang.com/v1/objects/ada715d3943e7584f04aca8ec44f5d3cd767353a/client.jar";
    String filename = "client.jar";
    var clientJar = JarDownloader.downloadIfAbsent(url, filename);
    clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
        try(InputStream inputStream = clientJar.getInputStream(jarEntry)) {
            String name = Arrays.stream(jarEntry.getName().split("/")).toList().getLast();
            IO.println(name);
            byte[] bytes = inputStream.readAllBytes();
            for(byte currentByte : bytes)
            {
                IO.println(OpCodes.opcodes[currentByte]);
            }


        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    });
}


