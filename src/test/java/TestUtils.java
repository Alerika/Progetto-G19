import it.unipv.model.MovieSchedule;
import it.unipv.utils.ApplicationException;
import junit.framework.TestCase;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@RunWith(JUnit4.class)
public class TestUtils extends TestCase {

    @Test
    public void testIfDateIsPassed() {
        assertTrue(checkIfDateIsPassed("02/04/2019"));
    }

    @Test
    public void testMovieScheduleDateAndTimeComparator() {
        MovieSchedule m1 = new MovieSchedule();
        MovieSchedule m2 = new MovieSchedule();
        m1.setDate("03/04/2019");
        m1.setTime("22:00");
        m2.setDate("03/04/2019");
        m2.setTime("21:00");
        assertEquals(1, movieScheduleDateAndTimeComparator(m1,m2));
    }

    private int movieScheduleDateAndTimeComparator(MovieSchedule m1, MovieSchedule m2){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal1 = Calendar.getInstance();
        Calendar cal2 = Calendar.getInstance();
        try {
            cal1.setTime(sdf.parse(m1.getDate() + " " + m1.getTime()));
            cal2.setTime(sdf.parse(m2.getDate() + " " + m2.getTime()));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        return cal1.compareTo(cal2);
    }

    @Test public void testDateFormatter() {
        System.out.println(formatDate("2019-04-30"));
    }

    @Test public void testTimeFormatter() {
        System.out.println(formatTime("13:14"));
    }

    private String formatDate(String toFormat) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat sdf1 = new SimpleDateFormat("dd/MM/yyyy");
            return sdf1.format(sdf.parse(toFormat));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
    }

    private String formatTime(String toFormat) {
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

    @Test
    public void checkDateUtils() {
        assertTrue(checkIfICanAddThisSchedule("17/05/2019 17:00", 60, 30, "17/05/2019 15:29", 60));
        assertTrue(checkIfICanAddThisSchedule("17/05/2019 17:00", 60, 30, "17/05/2019 18:31", 60));

    }


    private boolean checkIfICanAddThisSchedule(String existingScheduleDate, int existingMovieDuration, int pause, String incomingScheduleDate, int incomingMovieDuration) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar realIncomingScheduleDate = Calendar.getInstance();
        try {
            realIncomingScheduleDate.setTime(sdf.parse(incomingScheduleDate));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return realIncomingScheduleDate.before(getTimeOccupiedBySchedule(existingScheduleDate, incomingMovieDuration, pause, false))
            || realIncomingScheduleDate.after(getTimeOccupiedBySchedule(existingScheduleDate, existingMovieDuration, pause, true));
    }

    private Calendar getTimeOccupiedBySchedule(String existingScheduleDate, int movieDuration, int pause, boolean isItToAdd) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(sdf.parse((existingScheduleDate)));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        if(isItToAdd) { result.add(Calendar.MINUTE, movieDuration+pause); }
        if(!isItToAdd) { result.add(Calendar.MINUTE, -(movieDuration+pause)); }
        return result;
    }


    private boolean checkIfDateIsPassed(String toCheck){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateToCheck;
        try {
            dateToCheck = sdf.parse(toCheck);
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        return dateToCheck.before(new Date());
    }

    @Test
    public void testGetterActualSeatsList() {
        List<String> listaPostiOccupati = new ArrayList<>();
        listaPostiOccupati.add("A1-A2-A3");
        listaPostiOccupati.add("B4-B8");
        listaPostiOccupati.add("B9-C2-C3");
        listaPostiOccupati.add("D1-D2-E3-E4");

        List<String> res = getActualOccupiedSeatsList(listaPostiOccupati);

        for(String s : res) {
            System.out.println(s);
        }
    }

    private List<String> getActualOccupiedSeatsList(List<String> listaPostiOccupati) {
        List<String> res = new ArrayList<>();
        for(String s : listaPostiOccupati) {
            String[] supp = s.split("-");
            res.addAll(Arrays.asList(supp));
        }
        return res;
    }

    @Test
    public void userCodeTest() {
        System.out.println(RandomStringUtils.random(5, "0123456789abcdefghijklmnopqrstuvzxy").toUpperCase());
    }
}
