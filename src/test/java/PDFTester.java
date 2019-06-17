import com.itextpdf.text.pdf.BaseFont;
import it.unipv.gui.user.Prenotation;
import org.junit.Test;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;
import java.io.*;
import java.nio.file.FileSystems;
import static org.thymeleaf.templatemode.TemplateMode.HTML;

public class PDFTester {

    private static final String OUTPUT_FILE = "test.pdf";
    private static final String UTF_8 = "UTF-8";

    @Test
    public void generatePdf() throws Exception {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(UTF_8);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);
        Prenotation prenotation = new Prenotation("Andrea", "Avengers: Endgame","blabla1","26/05/2019", "11:36", "Sala 1", "D5-D6", "20€");

        Context context = new Context();
        context.setVariable("prenotation", prenotation);

        String renderedHtmlContent = templateEngine.process("template/template", context);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("css/BebasNeueRegular.ttf", "UTF-8", BaseFont.EMBEDDED);

        String baseUrl = FileSystems
                .getDefault()
                .getPath("src", "main", "resources", "images", "font")
                .toUri()
                .toURL()
                .toString();
        renderer.setDocumentFromString(convertToXhtml(renderedHtmlContent), baseUrl);
        renderer.layout();

        OutputStream outputStream = new FileOutputStream(OUTPUT_FILE);
        renderer.createPDF(outputStream);
        outputStream.close();
    }

    private String convertToXhtml(String html) throws UnsupportedEncodingException {
        Tidy tidy = new Tidy();
        tidy.setInputEncoding(UTF_8);
        tidy.setOutputEncoding(UTF_8);
        tidy.setXHTML(true);
        ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(UTF_8));
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        tidy.parseDOM(inputStream, outputStream);
        return outputStream.toString(UTF_8);
    }
}