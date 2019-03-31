package it.unipv.conversion;

import com.opencsv.CSVWriter;
import it.unipv.gui.login.User;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Questa classe viene utilizzata per salvare le informazioni di un utente
 *    all'interno di un file .csv (per ora è sempre utenti/lista utenti.csv).
 * Ho utilizzato la libreria "opencsv", la cui dipendenza
 *    è possibile trovarla all'interno del pom.
 */
public class UserToCSV {

    /**
     * @param user -> utente le cui informazioni verranno salvate all'interno del file .csv
     * @param pathCSV -> percorso dove trovare il file .csv dove salvare le informazioni
     * @param isItToAppend -> se true entra in modalità di scrittura append, altrimenti no.
     */
    public static void appendUserToCSV(User user, String pathCSV, boolean isItToAppend) {
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
            writer.writeNext(csvRow.toArray(fullCsvRowWithUserInfo(user, csvRow)));

        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.flush(writer,file);
            CloseableUtils.close(writer, file);
            /* Per un bug di java, a quanto pare, non chiude realmente lo stream.. devo richiamare la garbage collector
             * per poter poi liberare il processo che tiene attivo lo stream ed eventualmente poter cancellare il file
             */
            System.gc();
        }
    }

    /**
     * Metodo utilizzato per salvare le informazioni di un utente all'interno di una singola riga del .csv
     * @param user -> utente da cui trarre le informazioni da salvare
     * @param csvRow -> lista di stringhe che rappresenta la singola riga del csv
     * @return -> array di stringhe con dimensione pari alla dimensione della singola riga
     */
    private static String[] fullCsvRowWithUserInfo(User user, List<String> csvRow) {
        csvRow.add(user.getName());
        csvRow.add(user.getPassword());
        return new String[csvRow.size()];
    }
}
