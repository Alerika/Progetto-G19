package it.unipv;

import it.unipv.model.Schedule;
import it.unipv.utils.ApplicationUtils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.assertNotEquals;

@RunWith(JUnit4.class)
public class CinemaUtilsTest extends TestCase {

    @Test
    public void testIfDateIsPassed() {
        assertTrue(ApplicationUtils.checkIfDateIsPassed("02/04/2019"));
    }

    @Test
    public void testMovieScheduleDateAndTimeComparator() {
        Schedule m1 = new Schedule();
        Schedule m2 = new Schedule();
        m1.setDate("03/04/2019");
        m1.setTime("22:00");
        m2.setDate("03/04/2019");
        m2.setTime("21:00");
        assertEquals(1, m1.compareTo(m2));
    }

    @Test
    public void testDateFormatter() {
        assertEquals("30/04/2019", ApplicationUtils.formatDate("2019-04-30"));
    }

    @Test
    public void testTimeFormatter() {
        assertEquals("22:30", ApplicationUtils.formatTime("22:30:23.11"));
    }

    @Test
    public void testGetterActualSeatsList() {
        //Aggiungo 13 posti a caso
        List<String> listaPostiOccupati = new ArrayList<>();
        listaPostiOccupati.add("A1-A2-A3");
        listaPostiOccupati.add("B4-B8");
        listaPostiOccupati.add("B9-C2-C3");
        listaPostiOccupati.add("D1-D2-E3-E4");
        listaPostiOccupati.add("Z1");

        List<String> res = ApplicationUtils.splitter(listaPostiOccupati, "-");

        for(String s : res) { System.out.println(s); }

        //Controllo se sono 12 posti realmente
        assertEquals(13, res.size());
    }

    @Test
    public void testGetterActualSeenGenres() {
        //Aggiungo 6 generi a caso separati da ,
        List<String> listaGeneri = new ArrayList<>();
        listaGeneri.add("Azione,Commedia");
        listaGeneri.add("Anime,Fantascienza,Horror");
        listaGeneri.add("Drammatico");

        List<String> res = ApplicationUtils.splitter(listaGeneri, ",");

        for(String s : res ) { System.out.println(s); }

        //Controllo se sono 6 posti realmente
        assertEquals(6, res.size());

    }

    @Test
    public void userCodeTest() {
        String codice1 = ApplicationUtils.getRandomCode(5, "0123456789abcdefghijklmnopqrstuvzxy");
        String codice2 = ApplicationUtils.getRandomCode(5, "0123456789abcdefghijklmnopqrstuvzxy");

        System.out.println(codice1 + "\n" + codice2);

        assertNotEquals(codice1, codice2);
    }

}
