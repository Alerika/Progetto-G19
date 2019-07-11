package it.unipv.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class ApplicationUtils {

    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static boolean checkIfDateIsPassed(String toCheck){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateToCheck;
        try {
            dateToCheck = sdf.parse(toCheck);
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        return dateToCheck.before(new Date());
    }

    /** Metodo che formatta la data nel formato dd/MM/yyyy italiano. */
    public static String formatDate(String toFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            return sdf1.format(sdf.parse(toFormat));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
    }

    /** Metodo che formatta l'ora nel formato HH:mm. */
    public static String formatTime(String toFormat) {
        String[] time = toFormat.split(":");
        if(time.length>2) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss.SSS");
                SimpleDateFormat sdf1 = new SimpleDateFormat("HH:mm");
                return sdf1.format(sdf.parse(toFormat));
            } catch (ParseException e) {
                throw new ApplicationException(e);
            }
        } else {
            return toFormat;
        }
    }

    /**
     * Metodo che prende in input una lista di stringhe, i quali campi sono separati da *regex*
     *     e che ritorna una lista di stringhe contenente il singolo campo.
     * Esempio:
     *     Input -> A1-B1-D1 A2-B2-D2
     *     Output -> A1 B1 D1 A2 B2 D2 (in singole stringhe)
     * @param toSplit -> lista di stringhe i quali campi sono separati da *regex*
     * @param regex -> il separatore dei campi, può essere qualsiasi cosa, come - o ,
     * @return -> lista di stringhe contenente il singolo campo
     */
    public static List<String> splitter(List<String> toSplit, String regex) {
        List<String> res = new ArrayList<>();
        for(String s : toSplit) {
            String[] supp = s.split(regex);
            if(supp.length>0) {
                res.addAll(Arrays.asList(supp));
            } else {
                res.add(s);
            }
        }
        return res;
    }

    /**
     * Metodo che ritorna un codice univoco di maxChar a partire da una lista di chars
     * @param maxChar -> numero massimo di caratteri del codice
     * @param chars -> i caratteri che possono essere utilizzati
     * @return -> codice casuale di maxChar
     */
    public static String getRandomCode(int maxChar, String chars) {
        return RandomStringUtils.random(maxChar, chars).toUpperCase();
    }
}
