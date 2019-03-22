package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.common.MyDraggableSeat;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

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

    public static List<MyDraggableSeat> getMyDraggableSeatListFromCSV(String path) {
        CSVReader reader = null;
        List<MyDraggableSeat> myDraggableSeats = new ArrayList<>();
        try {
            reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
            String[] line;
            while((line = reader.readNext()) != null) {
                MyDraggableSeat myDraggableSeat = setMyDraggableSeat(line);
                myDraggableSeats.add(myDraggableSeat);
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return myDraggableSeats;
    }

    private static MyDraggableSeat setMyDraggableSeat(String[] line) {
        MyDraggableSeat res = new MyDraggableSeat(Integer.parseInt(line[1]),Integer.parseInt(line[2]));
        res.setText(line[0]);
        return res;
    }
}
