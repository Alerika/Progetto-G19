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
    public void test() {
        assertTrue(checkIfTimeIsBetweenPreviousTimeAndGap("00:00", 130, 30, "00:00"));
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


    private Calendar gapCalculator(String myTime, int movieTime, int cleanTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar cal = Calendar.getInstance();
        try {
            cal.setTime(sdf.parse((myTime)));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }
        cal.add(Calendar.MINUTE, movieTime);
        cal.add(Calendar.MINUTE, cleanTime);
        System.out.println(cal.getTime());
        return cal;
    }

    private boolean checkIfTimeIsBetweenPreviousTimeAndGap(String previousTime, int movieTime, int cleanTime, String myTime) {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Calendar previousActualTime = Calendar.getInstance();
        Calendar gapActualTime = gapCalculator(previousTime, movieTime, cleanTime);
        Calendar myActualTime = Calendar.getInstance();
        try {
            previousActualTime.setTime(sdf.parse((previousTime)));
            myActualTime.setTime(sdf.parse(myTime));
        } catch (ParseException e) {
            throw new ApplicationException(e);
        }

        System.out.println(previousActualTime.getTime() + " < " + myActualTime.getTime() + " < " + gapActualTime.getTime());
        return myActualTime.after(previousActualTime) && myActualTime.before(gapActualTime);
    }
}
