package it.unipv.utils;

import java.io.File;
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

    public static boolean renameFile(String oldFilePath, String newFilePath) {
        File fileToRename = new File(oldFilePath);
        if(fileToRename.exists()) {
            return fileToRename.renameTo(new File(newFilePath));
        }
        throw new ApplicationException("Impossibile rinominare il file " + oldFilePath);
    }

}
