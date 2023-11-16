package dhbw.on.webdev.pdf;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.apache.pdfbox.io.IOUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.common.PDStream;
import org.apache.pdfbox.pdmodel.font.PDFont;
import org.apache.pdfbox.pdmodel.font.PDType1Font;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@ApplicationScoped
public class PDFHelper {
    private final PDFont headingFont = PDType1Font.HELVETICA_BOLD;
    private final float headingFontSize = 22;
    private final PDFont textFont = PDType1Font.HELVETICA;
    private final float textFontSize = 14;
    private float lastElementYPosition;
    private final float marginTop = 50;
    private final float marginLeft = 80;

    public byte[] createPdf() {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Adding a title
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 22);
                contentStream.newLineAtOffset(100, 700);
                contentStream.showText("Sample PDF Title");
                contentStream.endText();

                // Adding some text
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 650);
                contentStream.showText("This is some sample text in the PDF.");
                contentStream.endText();

                // Adding some text
                contentStream.beginText();
                contentStream.setFont(PDType1Font.HELVETICA, 12);
                contentStream.newLineAtOffset(100, 600);
                contentStream.showText("This is some sample text in the PDF.");
                contentStream.endText();

            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            document.save(out);
            return out.toByteArray();
        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
    }

    //Page Builder
    private PDPage createInitialPage(final PDDocument document, final String initPageTitle) {
        PDPage initialPage = new PDPage(PDRectangle.A4);
        try (PDPageContentStream contentStream = new PDPageContentStream(document, initialPage)) {
            // Adding a title
            contentStream.beginText();
            contentStream.setFont(headingFont, headingFontSize);
            contentStream.newLineAtOffset(marginLeft, 700);
            contentStream.showText(initPageTitle);
            contentStream.endText();

            // Adding some text
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 650);
            contentStream.showText("This is some sample text in the PDF.");
            contentStream.endText();

            // Adding some text
            contentStream.beginText();
            contentStream.setFont(PDType1Font.HELVETICA, 12);
            contentStream.newLineAtOffset(100, 600);
            contentStream.showText("This is some sample text in the PDF.");
            contentStream.endText();

        } catch (Exception e) {
            throw new RuntimeException("Error creating PDF", e);
        }
        return initialPage;
    }
    public byte[] convertXmlToPdf(String xmlData) throws Exception {
        // Configure FOP factory and user agent
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();

        // Setup output stream (PDF will be written here)
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            // Setup FOP for PDF output
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Load XSLT file
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(new File("src/main/java/dhbw/on/webdev/pdf/stylesheet.xsl")));

            // Set up input for transformation
            StreamSource src = new StreamSource(new ByteArrayInputStream(xmlData.getBytes()));

            // Perform transformation and FOP processing (XML -> XSL-FO -> PDF)
            transformer.transform(src, new SAXResult(fop.getDefaultHandler()));

        } finally {
            out.close();
        }

        return out.toByteArray();
    }
}
