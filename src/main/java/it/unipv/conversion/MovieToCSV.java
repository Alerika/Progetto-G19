package it.unipv.conversion;

import com.opencsv.CSVWriter;
import it.unipv.gui.common.Movie;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/*
 * INFO:
 * Per un bug di java, a quanto pare, non chiude realmente lo stream.. devo richiamare la garbage collector
 * per poter poi liberare il processo che tiene attivo lo stream ed eventualmente poter cancellare il file
*/

/**
 * Questa classe viene utilizzata per salvare un film o una lista di film
 *    all'interno di un file .csv (per ora è sempre film/lista film.csv).
 * Ho utilizzato la libreria "opencsv", la cui dipendenza
 *    è possibile trovarla all'interno del pom.
 */
public class MovieToCSV {

    /**
     * @param movies -> lista di film da salvare all'interno del file .csv
     * @param pathCSV -> percorso dove trovare/creare il file .csv
     * @param isItToAppend -> se true entra in modalità di scrittura append, altrimenti no.
     */
    public static void createCSVFromMovieList(List<Movie> movies, String pathCSV, boolean isItToAppend) {
        CSVWriter writer = null;
        FileWriter file = null;
        try {
            if(isItToAppend) {
                file = new FileWriter(pathCSV, true);
            } else {
                file = new FileWriter(pathCSV, false);
            }
            file = new FileWriter(pathCSV,true);
            writer = new CSVWriter( file
                    ,';'
                    , CSVWriter.NO_QUOTE_CHARACTER
                    ,"\r\n");

            for(Movie m : movies) {
                List<String> csvRow = new ArrayList<>();
                writer.writeNext(csvRow.toArray(fullCsvRowWithInfoMovie(m, csvRow)));
            }


        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.flush(writer,file);
            CloseableUtils.close(writer, file);
            System.gc();
        }
    }

    /**
     * @param infoMovie -> film da salvare all'interno del file .csv
     * @param pathCSV -> percorso dove trovare/creare il file .csv
     * @param isItToAppend -> se true entra in modalità di scrittura append, altrimenti no.
     */
    public static void appendInfoMovieToCSV(Movie infoMovie, String pathCSV, boolean isItToAppend) {
        CSVWriter writer = null;
        FileWriter file = null;
        try {
            if(isItToAppend) {
                file = new FileWriter(pathCSV, true);
            } else {
                file = new FileWriter(pathCSV, false);
            }
            file = new FileWriter(pathCSV,true);
            writer = new CSVWriter( file
                    ,';'
                    , CSVWriter.NO_QUOTE_CHARACTER
                    ,"\r\n");

           List<String> csvRow = new ArrayList<>();
           writer.writeNext(csvRow.toArray(fullCsvRowWithInfoMovie(infoMovie, csvRow)));

        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.flush(writer,file);
            CloseableUtils.close(writer, file);
            System.gc();
        }
    }

    /**
     * Metodo utilizzato per riempire la lista di String che contiene
     *    i dati da salvare all'interno della singola riga del .csv.
     * @param movie -> il film da cui prendiamo le informazioni da salvare
     * @param csvRow -> la riga che contiene le informazioni da salvare
     * @return -> array di stringhe contenente le informazioni da salvare.
     */
    private static String[] fullCsvRowWithInfoMovie(Movie movie, List<String> csvRow) {
        csvRow.add(movie.getCodice());
        csvRow.add(stringWrapper(movie.getLocandinaPath()));
        csvRow.add(movie.getTitolo());
        csvRow.add(movie.getGenere());
        csvRow.add(movie.getRegia());
        csvRow.add(movie.getCast());
        csvRow.add(movie.getDurata());
        csvRow.add(movie.getAnno());
        csvRow.add(movie.getTrama());
        return new String[csvRow.size()];
    }

    /**
     * Questo metodo viene utilizzato perché all'interno del file
     *    il percorso della locandina del film viene salvata con
     *    l'uso del singolo backslash, che in java è il carattere
     *    di escape. Vado quindi a sostituire ogni singolo backslash
     *    con un doppio backslash.
     * @param oldChar -> la string contenente il singolo backslash
     * @return -> la stessa stringa ma con i doppi backslash
     */
    private static String stringWrapper(String oldChar) {
        return oldChar.replace("\\", "\\\\");
    }
}
