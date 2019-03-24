package it.unipv.utils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

public class ApplicationUtils {

    public static void removeFileFromPath(String path) {
        try {
            Files.deleteIfExists(Paths.get(path));
        } catch (IOException e) {
            throw new ApplicationException("Si è verificato un errore durante l'eliminazione del file " + e.getMessage());
        }
    }

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

}
