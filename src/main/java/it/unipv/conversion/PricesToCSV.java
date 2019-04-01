package it.unipv.conversion;

import com.opencsv.CSVWriter;
import it.unipv.gui.manager.Prices;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PricesToCSV {

    public PricesToCSV() {}

    public static void createCSVFromPrices(Prices prices, String pathCSV, boolean isItToAppend) {
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
            csvRow.add(""+prices.getBase());
            csvRow.add(""+prices.getVip());
            csvRow.add(""+prices.getThreed());
            csvRow.add(""+prices.getReduced());
            String[] csvData = new String[csvRow.size()];
            writer.writeNext(csvRow.toArray(csvData));

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
