package de.overcooked_industries.jar;

import de.overcooked_industries.util.FileWriterUtils;
import de.overcooked_industries.util.StringUtils;
import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.BiFunction;
import java.util.function.Predicate;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import static org.objectweb.asm.Opcodes.ASM9;

public class JarProcessor {
    public static final StringBuilder outputText = new StringBuilder();


    public static <T extends ClassVisitor> String process(String url, Predicate<String> processingCondition, BiFunction<Integer, String, T> cv, boolean writeToFile) throws IOException {
        var jar = JarDownloader.downloadIfAbsent(url, StringUtils.findAfterLast(url, "/"));
        jar.entries().asIterator().forEachRemaining(jarEntry -> {
            if (!processingCondition.test(jarEntry.getName())) return;
            processEntry(jar, jarEntry, cv);
        });

        jar.close();

        if(writeToFile) FileWriterUtils.writeToFile("output.txt", outputText.toString());
        return outputText.toString();
    }

    private static <T extends ClassVisitor> void processEntry(JarFile jar, JarEntry jarEntry, BiFunction<Integer, String, T> cv) {
        try {
            InputStream inputStream = jar.getInputStream(jarEntry);
            ClassReader reader = new ClassReader(inputStream);
            reader.accept(cv.apply(ASM9, reader.getClassName()), 0);
            inputStream.close();
        }
        catch (IOException _) {}
    }
}
