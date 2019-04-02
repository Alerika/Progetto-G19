package it.unipv.conversion;

import it.unipv.utils.DataReferences;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 *  Questa classe viene utilizzata per salvare all'interno di una
 *     lista di stringhe i nomi dei file, rigorosamente .csv,
 *     contenuti all'interno della cartella "piantine"
 */
public class FileToHallList {

    public FileToHallList() {}

    public static List<String> getHallList() {
        List<String> hallNames = new ArrayList<>();
        File folder = new File(DataReferences.PIANTINAFOLDERPATH);

        for(File f : Objects.requireNonNull(folder.listFiles())) {
            if(f.isFile()) {
                if(f.getName().endsWith(".csv")) {
                    hallNames.add(FilenameUtils.removeExtension(f.getName()));
                }
            }
        }
        return hallNames;
    }
}
