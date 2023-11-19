package dhbw.on.webdev.service.helper;

import io.quarkus.logging.Log;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.smallrye.common.annotation.Blocking;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;

/***
 * Class for doing all mail related stuff.
 * @see <a href="https://quarkus.io/guides/mailer">Quarkus Mailer</a>
 * @author daniel
 */
@ApplicationScoped
public class MailService {

    /**** CONFIG PROPERTIES ****/
    @ConfigProperty(name = "mailer.service.subject")
    String mailServiceSubject;
    @ConfigProperty(name = "mailer.service.text")
    String mailServiceText;
    @ConfigProperty(name = "mailer.service.content.type")
    String mailServiceContentType;

    /**** CDI ****/
    @Inject
    Mailer mailer;
    @Inject
    PdfService pdfService;

    /**
     * This method send a mail with the training session attached to the passed mail-address.
     * Therefor a Gmail-account with dedicated access was set up
     * @see <a href="https://support.google.com/accounts/answer/185833?hl=en">Gmail app passwords</a>
     * @param pdfBytes, which represent the pdf
     * @param mail address of the client
     * @throws Exception if error while sending occurs
     */
    @Blocking
    public void sendMail(final byte[] pdfBytes, final String mail) throws Exception {
        try {
            Log.info("Trying to send mail");
            mailer.send(generateMailToSend(pdfBytes, mail));
        } catch (Exception exception) {
            Log.error("Couldn't send mail " + exception);
            throw new Exception(exception);
        }
    }

    /**
     * Method for generating a mail with attached pdf.
     * @param pdfBytes pdf to attach
     * @param mail to send to
     * @return Mail t send
     */
    private Mail generateMailToSend(final byte[] pdfBytes, final String mail) {
        Log.info("Start to generate mail...");
        Mail mailToSend = new Mail();
        mailToSend.addTo(mail);
        mailToSend.setSubject(mailServiceSubject);
        mailToSend.setText(mailServiceText);
        mailToSend.addAttachment(pdfService.getFilenameForPdf(),pdfBytes, mailServiceContentType);
        Log.info("Generated mail");
        return mailToSend;
    }
}
