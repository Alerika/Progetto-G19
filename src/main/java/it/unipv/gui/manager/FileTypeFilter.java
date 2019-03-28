package it.unipv.gui.manager;

import java.io.File;
import javax.swing.filechooser.FileFilter;

/**
 * Classe utilizzata per impostare i filtri del JFileChoser presente nel form di creazione del film
 */
public class FileTypeFilter extends FileFilter {

    private String extension;
    private String description;

    public FileTypeFilter(String extension, String description) {
        this.extension = extension;
        this.description = description;
    }

    @Override
    public boolean accept(File file) {
        if (file.isDirectory()) {
            return true;
        }
        return file.getName().toLowerCase().endsWith(extension);
    }

    public String getDescription() {
        return description + String.format(" (*%s)", extension);
    }
}
