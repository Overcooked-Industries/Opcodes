package de.kilip;

import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarProcessor {
    public static void process(String url, Predicate<String> processingCondition)
    {
        try (var clientJar = JarDownloader.downloadIfAbsent(url, Arrays.stream(url.split("/")).toList().getLast())) {
            clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
                String name = Arrays.stream(jarEntry.getName().split("/")).toList().getLast();
                if (!processingCondition.test(name)) return;

                try (InputStream inputStream = clientJar.getInputStream(jarEntry)) {
                    var reader = new ClassReader(inputStream);
                    reader.accept(new OpCodePrinterClassVisitor(ASM9), 0);
                } catch (Exception _) {}
            });
        } catch (IOException _) {
        }
    }
}
