package it.unipv.utils;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
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

    public static void saveSnapshot(String path, Component c, String format) {
        try {
            BufferedImage img = new BufferedImage(c.getWidth(), c.getHeight(), BufferedImage.TYPE_INT_RGB);
            Graphics2D graphs = img.createGraphics();
            graphs.setBackground(Color.WHITE);
            graphs.clearRect(0, 0, c.getWidth(), c.getHeight());
            c.paint(graphs);
            File outputfile = new File(path);
            ImageIO.write(img, format, outputfile);
        } catch (IOException e) {
            throw new ApplicationException("Errore durante il salvataggio dello snapshot", e);
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
