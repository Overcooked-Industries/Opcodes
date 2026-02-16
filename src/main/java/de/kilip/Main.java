import de.kilip.JarDownloader;
import de.kilip.OpCodePrinterClassVisitor;
import org.objectweb.asm.ClassReader;

import static org.objectweb.asm.Opcodes.ASM9;

void main() throws IOException {
    String url = "https://piston-data.mojang.com/v1/objects/ada715d3943e7584f04aca8ec44f5d3cd767353a/client.jar";
    String filename = "client.jar";
    var clientJar = JarDownloader.downloadIfAbsent(url, filename);
    clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
        String name = Arrays.stream(jarEntry.getName().split("/")).toList().getLast();
        if (!name.contains("ChatFormatting.class")) return;

        try (InputStream inputStream = clientJar.getInputStream(jarEntry)) {
            var reader = new ClassReader(inputStream);
            reader.accept(new OpCodePrinterClassVisitor(ASM9), 0);
        } catch (Exception _) {
        }
    });
}


