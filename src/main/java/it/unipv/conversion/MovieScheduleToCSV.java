package it.unipv.conversion;

import com.opencsv.CSVWriter;
import it.unipv.gui.manager.MovieSchedule;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MovieScheduleToCSV {

    public MovieScheduleToCSV() {}

    public static void createCSVFromMovieScheduleList(List<MovieSchedule> movieSchedules, String pathCSV, boolean isItToAppend) {
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

            for(MovieSchedule m : movieSchedules) {
                List<String> csvRow = new ArrayList<>();
                writer.writeNext(csvRow.toArray(fullCsvRowWithInfoMovieSchedules(m, csvRow)));
            }


        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.flush(writer,file);
            CloseableUtils.close(writer, file);
            System.gc();
        }
    }

    public static void createCSVFromMovieSchedule(MovieSchedule movieSchedule, String pathCSV, boolean isItToAppend) {
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
            writer.writeNext(csvRow.toArray(fullCsvRowWithInfoMovieSchedules(movieSchedule, csvRow)));

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

    private static String[] fullCsvRowWithInfoMovieSchedules(MovieSchedule movieSchedule, List<String> csvRow) {
        csvRow.add(""+movieSchedule.getMovieCode());
        csvRow.add(""+movieSchedule.getDate());
        csvRow.add(""+movieSchedule.getTime());
        csvRow.add(""+movieSchedule.getHallName());
        return new String[csvRow.size()];
    }
}
