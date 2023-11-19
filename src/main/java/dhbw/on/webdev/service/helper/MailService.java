package dhbw.on.webdev.service.helper;

import dhbw.on.webdev.model.entities.TrainingSession;
import dhbw.on.webdev.pdf.PDFHelper;
import dhbw.on.webdev.repository.TrainingSessionRepository;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.Mailer;
import io.quarkus.mailer.reactive.ReactiveMailer;
import io.smallrye.common.annotation.Blocking;
import io.smallrye.mutiny.Uni;
import jakarta.inject.Inject;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@Path("/mail")
public class MailService {

    @ConfigProperty(name = "mailer.service.subject")
    String mailServiceSubject;
    @Inject
    Mailer mailer;

    @Inject
    TrainingSessionRepository trainingSessionRepository;

    @Inject
    ServiceHelper serviceHelper;
    @Inject
    PDFHelper pdfHelper;

    @GET
    @Path ("test/{id}")
    @Blocking
    public void sendEmail(@PathParam("id") final long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id);
        byte[] pdfBytes = new byte[0];
        try {
            pdfBytes = pdfHelper.convertXmlToPdf(serviceHelper.convertJsonToXML(serviceHelper.convertEntityToJson(trainingSession)));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        mailer.send(
                Mail.withText("info@d-vollmer.de",
                        mailServiceSubject,
                        "A simple email sent from a Quarkus application.")
                        .addAttachment("trainingseinheit", pdfBytes, "application/pdf")
        );
    }
    @Inject
    ReactiveMailer reactiveMailer;

    @GET
    @Path("/reactive/{id}")
    public Uni<Void> sendEmailUsingReactiveMailer(@PathParam("id") final long id) {
        TrainingSession trainingSession = trainingSessionRepository.findById(id);
        try {
            byte[] pdfBytes = pdfHelper.convertXmlToPdf(serviceHelper.convertJsonToXML(serviceHelper.convertEntityToJson(trainingSession)));
            return reactiveMailer.send(
                    Mail.withText("info@d-vollmer.de",
                            mailServiceSubject,
                            "A simple email sent from a Quarkus application using the reactive API.")
                            .addAttachment("trainingseinheit", pdfBytes, "application/pdf")
                            );
        } catch (Exception exception) {
            System.out.println(exception);
            return null;
        }


    }


}
