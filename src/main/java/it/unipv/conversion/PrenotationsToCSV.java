package it.unipv.conversion;

import com.opencsv.CSVWriter;
import it.unipv.gui.user.Prenotation;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PrenotationsToCSV {

    public static void createCSVFromPrenotationList(List<Prenotation> prenotations, String pathCSV, boolean isItToAppend) {
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

            for(Prenotation p : prenotations) {
                List<String> csvRow = new ArrayList<>();
                writer.writeNext(csvRow.toArray(fullCsvRowWithInfoPrenotation(p, csvRow)));
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
     * @param prenotation -> film da salvare all'interno del file .csv
     * @param pathCSV -> percorso dove trovare/creare il file .csv
     * @param isItToAppend -> se true entra in modalità di scrittura append, altrimenti no.
     */
    public static void appendInfoMovieToCSV(Prenotation prenotation, String pathCSV, boolean isItToAppend) {
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
            writer.writeNext(csvRow.toArray(fullCsvRowWithInfoPrenotation(prenotation, csvRow)));

        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.flush(writer,file);
            CloseableUtils.close(writer, file);
            System.gc();
        }
    }

    private static String[] fullCsvRowWithInfoPrenotation(Prenotation prenotation, List<String> csvRow) {
        csvRow.add(prenotation.getNomeUtente());
        csvRow.add(prenotation.getNomeFilm());
        csvRow.add(prenotation.getCodiceFilm());
        csvRow.add(prenotation.getGiornoFilm());
        csvRow.add(prenotation.getOraFilm());
        csvRow.add(prenotation.getSalaFilm());
        csvRow.add(prenotation.getPostiSelezionati());
        csvRow.add(prenotation.getCostoTotale());
        return new String[csvRow.size()];
    }
}
