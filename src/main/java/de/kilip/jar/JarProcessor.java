package de.kilip.jar;

import de.kilip.util.StringUtils;
import de.kilip.visitors.OpCodePrinterClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarProcessor {
    public static void process(String url, Predicate<String> processingCondition)
    {
        try (var clientJar = JarDownloader.downloadIfAbsent(url, StringUtils.findAfterLast(url,"/"))) {
            clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
                if (!processingCondition.test(jarEntry.getName())) return;
                String pretty = StringUtils.findAfterLast(jarEntry.getName(),"/");
                IO.println(pretty);
                try (InputStream inputStream = clientJar.getInputStream(jarEntry)) {
                    var reader = new ClassReader(inputStream);
                    reader.accept(new OpCodePrinterClassVisitor(ASM9), 0);
                }
                catch (Exception _) {}
            });
        } catch (IOException _) {
        }
    }
}
