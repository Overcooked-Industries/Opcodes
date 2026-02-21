package de.overcooked_industries.jar;

import de.overcooked_industries.util.StringUtils;
import de.overcooked_industries.visitors.OpCodePrinterClassVisitor;
import org.objectweb.asm.ClassReader;

import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.function.Predicate;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarProcessor {
    public static final StringBuilder outputText = new StringBuilder();


    public static void process(String url, Predicate<String> processingCondition) {
        try (var clientJar = JarDownloader.downloadIfAbsent(url, StringUtils.findAfterLast(url, "/"))) {
            clientJar.entries().asIterator().forEachRemaining(jarEntry -> {
                if (!processingCondition.test(jarEntry.getName())) return;
                String pretty = StringUtils.findAfterLast(jarEntry.getName(), "/");
                outputText.append("\n").append(pretty).append("\n");
                try (InputStream inputStream = clientJar.getInputStream(jarEntry)) {
                    var reader = new ClassReader(inputStream);
                    reader.accept(new OpCodePrinterClassVisitor(ASM9), 0);
                } catch (Exception _) {
                }
            });
        } catch (IOException _) {
        }

        try(FileWriter fw = new FileWriter("output.txt"))
        {
            fw.write(outputText.toString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
