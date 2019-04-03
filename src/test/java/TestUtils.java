import it.unipv.gui.manager.MovieSchedule;
import it.unipv.utils.ApplicationException;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

@RunWith(JUnit4.class)
public class TestUtils extends TestCase {

    @Test
    public void testIfMyTimeIsBetweenMovieTimeAndGap() {
        assertTrue(checkIfTimeIsBetweenPreviousTimeAndGap("26/04/2019 00:00", 130, 30, "26/04/2019 02:00", true));
        assertTrue(checkIfTimeIsBetweenPreviousTimeAndGap("26/04/2019 22:00", 130, 30, "26/04/2019 21:00", false));
        assertTrue(checkIfTimeIsBetweenPreviousTimeAndGap("25/04/2019 22:00", 130, 30, "26/04/2019 00:00", true));
        assertTrue(checkIfTimeIsBetweenPreviousTimeAndGap("26/04/2019 00:00", 130, 30, "25/04/2019 22:00", false));
    }

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

    private boolean checkIfDateIsPassed(String toCheck){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        Date dateToCheck;
        try {
            dateToCheck = sdf.parse(toCheck);
        } catch (ParseException e) {
            throw new ApplicationException("Parse fallito :(");
        }
        return dateToCheck.before(new Date());
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


    private Calendar gapCalculator(String myTime, int movieTime, int cleanTime, boolean toAddOrNot) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse((myTime)));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        if(toAddOrNot) {
            cal.add(Calendar.MINUTE, movieTime);
            cal.add(Calendar.MINUTE, cleanTime);
        } else {
            cal.add(Calendar.MINUTE, -movieTime);
            cal.add(Calendar.MINUTE, -cleanTime);
        }

        return cal;
    }

    private boolean checkIfTimeIsBetweenPreviousTimeAndGap(String previousTime, int movieTime, int cleanTime, String myTime, boolean toAddOrNot) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm");
        Calendar previousActualTime = Calendar.getInstance();
        Calendar gapActualTime = gapCalculator(previousTime, movieTime, cleanTime, toAddOrNot);
        Calendar myActualTime = Calendar.getInstance();
        try {
            previousActualTime.setTime(sdf.parse((previousTime)));
            myActualTime.setTime(sdf.parse(myTime));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        if(toAddOrNot) {
            return myActualTime.after(previousActualTime) && myActualTime.before(gapActualTime);
        } else {
            return myActualTime.before(previousActualTime) && myActualTime.after(gapActualTime);

        }
    }
}
