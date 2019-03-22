package it.unipv.conversion;

import com.opencsv.CSVWriter;
import it.unipv.gui.common.MyDraggableSeat;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 *  Questa classe viene utilizzata per salvare le info riguardanti
 *     i posti a sedere all'interno di un file .csv.
 *  Ho utilizzato la libreria "opencsv", la cui dipendenza
 *     è possibile trovarla all'interno del pom.
 */
public class DraggableSeatsToCSV {

    public DraggableSeatsToCSV() {}

    public static void createCSVFromDraggableSeatsList(List<MyDraggableSeat> seats, String pathCSV, boolean isItToAppend) {
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

            for(MyDraggableSeat seat : seats) {
                List<String> csvRow = new ArrayList<>();
                csvRow.add(seat.getText());
                csvRow.add(""+seat.getX());
                csvRow.add(""+seat.getY());
                String[] csvData = new String[csvRow.size()];
                writer.writeNext(csvRow.toArray(csvData));
            }
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

}
