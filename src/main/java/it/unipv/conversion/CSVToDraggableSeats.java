package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.common.MyDraggableSeat;
import it.unipv.gui.common.SeatTYPE;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Questa classe è utilizzata per leggere da un file CSV le info
 *     riguardanti i posti a sedere di una determinata sala.
 *  Ho utilizzato la libreria "opencsv", la cui dipendenza
 *     è possibile trovarla all'interno del pom.
 */
public class CSVToDraggableSeats {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToDraggableSeats() { }

    /**
     * Metodo utilizzato per recuperare la lista di posti a sedere inizializzati
     *    da un determinato file .csv
     * @param path -> percorso dov'è possibile trovare il file .csv da cui trarre le informazioni
     * @return -> lista di posti a sedere correttamente inizializzati con le informazioni salvate nel file
     */
    public static List<MyDraggableSeat> getMyDraggableSeatListFromCSV(String path) {
        CSVReader reader = null;
        List<MyDraggableSeat> myDraggableSeats = new ArrayList<>();
        try {
            //Controllo se il file esiste: in caso negativo non faccio niente e ritorno una lista vuota
            File f = new File(path);
            if(f.exists()) {
                reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
                String[] line;
                while((line = reader.readNext()) != null) {
                    MyDraggableSeat myDraggableSeat = setMyDraggableSeat(line);
                    myDraggableSeats.add(myDraggableSeat);
                }
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return myDraggableSeats;
    }

    /**
     * Metodo per riempire il singolo posto a sedere con le informazioni della singola riga del .csv
     * @param line -> array di stringhe che contiene le informazioni di una singola riga
     * @return -> posto a sedere inizializzato con le varie informazioni:
     *               line[0] -> nome del posto a sedere
     *               line[1] -> coordinata x del posto a sedere
     *               line[2] -> coordinata y del posto a sedere
     *               line[3] -> tipologia del posto a sedere
     */
    private static MyDraggableSeat setMyDraggableSeat(String[] line) {
        MyDraggableSeat res;
        switch (line[3]) {
            case "NORMALE":
                res = new MyDraggableSeat(Integer.parseInt(line[1]), Integer.parseInt(line[2]), SeatTYPE.NORMALE);
                break;

            case "VIP":
                res = new MyDraggableSeat(Integer.parseInt(line[1]), Integer.parseInt(line[2]), SeatTYPE.VIP);
                break;

            case "DISABILE":
                res = new MyDraggableSeat(Integer.parseInt(line[1]), Integer.parseInt(line[2]), SeatTYPE.DISABILE);
                break;

            case "OCCUPATO":
                res = new MyDraggableSeat(Integer.parseInt(line[1]), Integer.parseInt(line[2]), SeatTYPE.OCCUPATO);
                break;

                default:
                    throw new ApplicationException("Tipo " + line[3] + " non riconosciuto!");
        }
        res.setText(line[0]);
        return res;
    }
}
