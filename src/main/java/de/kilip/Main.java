import de.kilip.JarDownloader;
import de.kilip.OpCodes;

void main() throws IOException {
    String url = "https://piston-data.mojang.com/v1/objects/ada715d3943e7584f04aca8ec44f5d3cd767353a/client.jar";
    String filename = "client.jar";
    var clientJar = JarDownloader.downloadIfAbsent(url, filename);
    StringBuilder opcodes = new StringBuilder();
    clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
        String name = Arrays.stream(jarEntry.getName().split("/")).toList().getLast();
        if(!name.contains(".class")) return;

        try (InputStream inputStream = clientJar.getInputStream(jarEntry)) {
            byte[] bytes = inputStream.readAllBytes();
            for (byte currentByte : bytes) {
                opcodes.append(OpCodes.opcodes[~currentByte]+"\n");
            }
        } catch (Exception _) {
        }
    });
    System.out.println(opcodes);
}


