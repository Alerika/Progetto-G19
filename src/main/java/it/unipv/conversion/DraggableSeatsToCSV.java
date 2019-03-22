package it.unipv.conversion;

import au.com.bytecode.opencsv.CSVWriter;
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

    public void createCSVFromDraggableSeatsList(List<MyDraggableSeat> seats, String pathCSV, boolean isItToAppend) {
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
            CloseableUtils.close(writer, file);
        }
    }

}
