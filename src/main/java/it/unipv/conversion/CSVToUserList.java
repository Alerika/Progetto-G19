package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.common.User;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Questa classe è utilizzata per recuperare la lista degli utenti dal file .csv
 *    salvato dentro la cartella utenti del progetto.
 * Ho utilizzato la libreria "opencsv", la cui dipendenza
 *    è possibile trovarla all'interno del pom.
 */
public class CSVToUserList {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToUserList() { }

    /**
     * Metodo utilizzato per leggere e recuperare la lista degli utenti dal file .csv
     * @param path -> percorso del file .csv da cui recuperare la lista utenti
     * @return -> lista di utenti (User) popolata dalle informazioni del file .csv
     */
    public static List<User> getUserListFromCSV(String path) {
        CSVReader reader = null;
        List<User> users = new ArrayList<>();
        try {
            File f = new File(path);
            //Controllo se il file esiste: in caso negativo non faccio niente e ritorno una lista vuota
            if(f.exists()) {
                reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
                String[] line;
                while ((line = reader.readNext()) != null) {
                    users.add(setUser(line));
                }
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return users;
    }

    //Inserisco all'interno del singolo utente le informazioni presenti sulla singola riga

    /**
     * Metodo per inserire all'interno del singolo utente le informazioni presenti sulla singola riga
     *    line[0] -> Username
     *    line[1] -> Password
     * @param line -> la singola riga del .csv
     * @return -> l'utente inizializzato con le informazioni presenti sulla riga
     */
    private static User setUser(String[] line) { return new User(line[0], line[1]); }
}
