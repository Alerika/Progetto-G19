package it.unipv.conversion;

import com.itextpdf.text.pdf.BaseFont;
import it.unipv.gui.prenotation.Prenotation;
import it.unipv.utils.ApplicationException;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;
import org.thymeleaf.templateresolver.ClassLoaderTemplateResolver;
import org.w3c.tidy.Tidy;
import org.xhtmlrenderer.pdf.ITextRenderer;

import java.io.*;
import java.nio.file.FileSystems;

import static org.thymeleaf.templatemode.TemplateMode.HTML;

public class PrenotationToPDF {

    public static void generatePDF(String outputfilePath, String encoding, Prenotation prenotation) throws Exception {
        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
        templateResolver.setPrefix("/");
        templateResolver.setSuffix(".html");
        templateResolver.setTemplateMode(HTML);
        templateResolver.setCharacterEncoding(encoding);

        TemplateEngine templateEngine = new TemplateEngine();
        templateEngine.setTemplateResolver(templateResolver);

        Context context = new Context();
        context.setVariable("prenotation", prenotation);

        ITextRenderer renderer = new ITextRenderer();
        renderer.getFontResolver().addFont("font/BebasNeueRegular.ttf", encoding, BaseFont.EMBEDDED);

        String baseUrl = FileSystems
                .getDefault()
                .getPath("src", "main", "resources", "images", "font")
                .toUri()
                .toURL()
                .toString();
        renderer.setDocumentFromString(convertToXhtml(templateEngine.process("template/template", context), encoding), baseUrl);
        renderer.layout();

        OutputStream outputStream = new FileOutputStream(outputfilePath);
        renderer.createPDF(outputStream);
        outputStream.close();
    }

    private static String convertToXhtml(String html, String encoding) {
        try {
            Tidy tidy = new Tidy();
            tidy.setQuiet(true);
            tidy.setInputEncoding(encoding);
            tidy.setOutputEncoding(encoding);
            tidy.setXHTML(true);
            ByteArrayInputStream inputStream = new ByteArrayInputStream(html.getBytes(encoding));
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            tidy.parseDOM(inputStream, outputStream);
            return outputStream.toString(encoding);
        } catch (UnsupportedEncodingException e) {
            throw new ApplicationException(e);
        }
    }
}
