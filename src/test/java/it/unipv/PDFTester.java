package it.unipv;

import com.lowagie.text.DocumentException;
import it.unipv.conversion.PrenotationToPDF;
import it.unipv.model.Prenotation;
import it.unipv.utils.ApplicationException;
import it.unipv.utils.ApplicationUtils;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.io.File;
import java.io.IOException;

@RunWith(JUnit4.class)
public class PDFTester extends TestCase {

    private static final String PDFPATH = "test.pdf";
    private static final String ENCODING = "UTF-8";

    @Test
    public void test() {
        createTestPDF();
        assertTrue(new File("test.pdf").exists());
    }

    private void createTestPDF() {
        Prenotation prenotation = new Prenotation( "Andrea"
                                                 , "Avengers: Endgame"
                                                 , "blabla1"
                                                 , "26/05/2019"
                                                 , "11:36"
                                                 , "Sala 1"
                                                 , "D5-D6"
                                                 , "20€");
        try {
            PrenotationToPDF.generatePDF(PDFPATH, ENCODING, prenotation);
        } catch (IOException | DocumentException e) {
            throw new ApplicationException(e);
        }
    }
}
