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

/**
 * Questa classe è utilizzata per recuperare la lista dei film dal file .csv
 *    salvato dentro la cartella film del progetto.
 * Ho utilizzato la libreria "opencsv", la cui dipendenza
 *    è possibile trovarla all'interno del pom.
 */
public class CSVToMovieList {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToMovieList() { }

    /**
     * Metodo utilizzato per recuperare la lista dei film dal file .csv.
     * @param path -> percorso del file .csv
     * @return -> lista dei film (Movie) salvati all'interno del file
     */
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

    /**
     * Metodo utilizzato per riempire il singolo film con le informazioni della singola riga del csv
     *    line[0] -> codice del film
     *    line[1] -> percorso della locandina
     *    line[2] -> titolo
     *    line[3] -> genere
     *    line[4] -> regia
     *    line[5] -> cast
     *    line[6] -> durata
     *    line[7] -> anno
     *    line[8] -> trama
     * @param line -> singola riga del .csv da cui trarre le informazioni
     * @return -> film inizializzato con le informazioni della singola riga
     */
    private static Movie setFilm(String[] line) {
        Movie res = new Movie();
        res.setCodice(line[0]);
        res.setLocandinaPath(line[1]);
        res.setTitolo(line[2]);
        res.setGenere(line[3]);
        res.setRegia(line[4]);
        res.setCast(line[5]);
        res.setDurata(line[6]);
        res.setAnno(line[7]);
        res.setTrama(line[8]);
        return res;
    }
}
