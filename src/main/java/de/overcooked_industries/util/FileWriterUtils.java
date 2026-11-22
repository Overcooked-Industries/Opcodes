package de.overcooked_industries.util;

import java.io.FileWriter;
import java.io.IOException;

public class FileWriterUtils {
    private FileWriterUtils() {}

    //Writes to file and when failing, fails loudly
    public static void writeToFile(String fileName, String text) {
        try(FileWriter fw = new FileWriter(fileName))
        {
            fw.write(text);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
