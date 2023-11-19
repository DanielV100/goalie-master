package dhbw.on.webdev.service.helper;

import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;

/**
 * Class contains methods for transforming xml to pdf. Apache-FO? is used therefor.
 * @see <a href="https://xmlgraphics.apache.org/fop/">Apache FOP</a>
 * @author daniel
 */
@ApplicationScoped
public class PdfService {
    /**
     * Method takes the xml and transforms it with the stylesheet.xsl to a pdf.
     * @param xmlData, which is wel-formed and the entity as xml
     * @return byte[] of the pdf
     * @throws Exception
     */
    public byte[] convertXmlToPdf(String xmlData) throws Exception {
        Log.info("Starting XML to PDF conversion");
        if (xmlData == null || xmlData.isEmpty()) {
            throw new IllegalArgumentException("XML data cannot be null or empty.");
        }
        FopFactory fopFactory = FopFactory.newInstance(new File(".").toURI());
        FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(new StreamSource(getClass().getClassLoader().getResourceAsStream("stylesheet.xsl")));
            StreamSource src = new StreamSource(new ByteArrayInputStream(xmlData.getBytes()));
            transformer.transform(src, new SAXResult(fop.getDefaultHandler()));
            return out.toByteArray();
        } catch (Exception e) {
            throw new Exception("Error converting XML to PDF", e);
        }
    }
}
