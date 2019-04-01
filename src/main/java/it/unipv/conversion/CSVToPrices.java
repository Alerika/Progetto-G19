package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.manager.Prices;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class CSVToPrices {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToPrices() {}

    public static Prices getPricesFromCSV(String path) {
        CSVReader reader = null;
        Prices prices = null;
        try {
            //Controllo se il file esiste: in caso negativo non faccio niente e ritorno una lista vuota
            File f = new File(path);
            if(f.exists()) {
                reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
                String[] line;
                while((line = reader.readNext()) != null) {
                    prices = setPrices(line);
                }
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return prices;
    }

    private static Prices setPrices(String[] line) {
        return new Prices( Double.parseDouble(line[0])
                         , Double.parseDouble(line[1])
                         , Double.parseDouble(line[2])
                         , Double.parseDouble(line[3]));
    }
}
