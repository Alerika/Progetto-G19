package it.unipv.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

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
}
