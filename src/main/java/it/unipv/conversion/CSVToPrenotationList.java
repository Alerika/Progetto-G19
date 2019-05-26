package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.common.Movie;
import it.unipv.gui.common.MovieStatusTYPE;
import it.unipv.gui.common.MovieTYPE;
import it.unipv.gui.user.Prenotation;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVToPrenotationList {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToPrenotationList() { }

    public static List<Prenotation> getPrenotationListFromCSV(String path) {
        CSVReader reader = null;
        List<Prenotation> prenotations = new ArrayList<>();
        try {
            File f = new File(path);
            //Controllo se il file esiste: in caso negativo non faccio niente e ritorno una lista vuota
            if(f.exists()) {
                reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
                String[] line;
                while ((line = reader.readNext()) != null) {
                    prenotations.add(setPrenotation(line));
                }
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return prenotations;
    }

    private static Prenotation setPrenotation(String[] line) {
        return new Prenotation( line[0]
                              , line[1]
                              , line[2]
                              , line[3]
                              , line[4]
                              , line[5]
                              , line[6]
                              , line[7]);
    }
}
