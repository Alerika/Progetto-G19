package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.common.Movie;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVToMovieList {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToMovieList() { }

    public static List<Movie> getMovieListFromCSV(String path) {
        CSVReader reader = null;
        List<Movie> movies = new ArrayList<>();
        try {
            File f = new File(path);
            //Controllo se il file esiste: in caso negativo non faccio niente e ritorno una lista vuota
            if(f.exists()) {
                reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
                String[] line;
                while ((line = reader.readNext()) != null) {
                    movies.add(setFilm(line));
                }
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return movies;
    }

    private static Movie setFilm(String[] line) {
        Movie res = new Movie();
        res.setCodice(line[0]);
        res.setLocandinaPath(line[1]);
        res.setTitolo(line[2]);
        res.setRegia(line[3]);
        res.setCast(line[4]);
        res.setDurata(line[5]);
        res.setAnno(line[6]);
        res.setTrama(line[7]);
        return res;
    }
}
