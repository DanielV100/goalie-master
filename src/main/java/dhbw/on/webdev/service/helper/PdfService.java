package dhbw.on.webdev.service.helper;

import dhbw.on.webdev.model.entities.TrainingSession;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.MimeConstants;
import org.eclipse.microprofile.config.inject.ConfigProperty;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.sql.Timestamp;

/**
 * Class contains methods for transforming xml to pdf. Apache-FO? is used therefor.
 * @see <a href="https://xmlgraphics.apache.org/fop/">Apache FOP</a>
 * @author daniel
 */
@ApplicationScoped
public class PdfService {

    @ConfigProperty(name = "pdf.service.name")
    String pdfName;

    @Inject
    ServiceHelper serviceHelper;

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

    /**
     * Central method for getting training session converted to a byte[], which is a pdf.
     * @param trainingSession from db
     * @return byte[], which represents the pdf
     */
    public final byte[] convertTrainingSessionToPdf(final TrainingSession trainingSession) {
        if(trainingSession == null) {
            Log.error("Training session not found");
            throw new IllegalArgumentException();
        }
        try {
            return convertXmlToPdf(serviceHelper.convertJsonToXML(serviceHelper.convertEntityToJson(trainingSession)));
        } catch (Exception exception) {
            Log.error("Couldn't covert training session to pdf", exception);
            throw new NullPointerException();
        }
    }

    /**
     * Getting filename for pdf, which always starts with `trainingseinheit`.
     * Timestamp is added.
     * @return name for pdf
     */
    public final String getFilenameForPdf() {
        return pdfName + "_" + getCurrentTimestamp() + ".pdf";
    }

    /**
     * Gets current timestamp.
     * @return current Timestamp
     */
    private Timestamp getCurrentTimestamp() {
        return new Timestamp(System.currentTimeMillis());
    }
}
