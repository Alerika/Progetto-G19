package it.unipv.conversion;

import com.opencsv.CSVReader;
import it.unipv.gui.common.MovieSchedule;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.CloseableUtils;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVToMovieScheduleList {
    private static final char DELIMITATOR = ';';
    private static final char VUOTO = '\0';

    public CSVToMovieScheduleList() {}

    public static List<MovieSchedule> getMovieScheduleListFromCSV(String path) {
        CSVReader reader = null;
        List<MovieSchedule> movieSchedules = new ArrayList<>();
        try {
            //Controllo se il file esiste: in caso negativo non faccio niente e ritorno una lista vuota
            File f = new File(path);
            if(f.exists()) {
                reader = new CSVReader(new FileReader(path), DELIMITATOR, VUOTO);
                String[] line;
                while((line = reader.readNext()) != null) {
                    movieSchedules.add(setMovieSchedule(line));
                }
            }
        } catch (IOException ex) {
            throw new ApplicationException(ex);
        } finally {
            CloseableUtils.close(reader);
        }

        return movieSchedules;
    }

    private static MovieSchedule setMovieSchedule(String[] line) {
        MovieSchedule res = new MovieSchedule();
        res.setMovieCode(line[0]);
        res.setDate(line[1]);
        res.setTime(line[2]);
        res.setHallName(line[3]);
        return res;
    }
}
