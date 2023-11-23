package dhbw.on.webdev.controller._training_session;

import dhbw.on.webdev.model.entities.TrainingSession;
import dhbw.on.webdev.service._training_session.TrainingSessionService;
import io.quarkus.logging.Log;
import io.smallrye.common.annotation.Blocking;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import java.util.List;

/**
 * REST-Endpoint-Handlers for training session entity.
 * @author daniel
 */
@Path("/training_session")
@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
@RolesAllowed("user")
public class TrainingSessionResource {
    /**** CDI ****/
    @Inject
    TrainingSessionService trainingSessionService;

    /**** GET-REQUEST-HANDLERS ****/
    @GET
    @Path("/get/specific")
    @Produces(MediaType.APPLICATION_JSON)
    public final List<TrainingSession> getAllTrainingSessionsFromCurrentUser() {
        return trainingSessionService.getAllTrainingSessionsFromCurrentUser();
    }
    @GET
    @Path("/download/{id}")
    @Produces("application/pdf")
    public final Response getTrainingSessionAsPdf(@PathParam("id") final long trainingSessionId) {
        if(trainingSessionId > 0) {
            return trainingSessionService.getTrainingSessionAsPdf(trainingSessionId);
        }
        return Response.status(400).build();
    }

    /**
     * Request handler for generating a training session as pdf, attach it to mail and send mail to
     * passed mail.
     * @param trainingSessionId of training session, which should be attached
     * @param mail of client
     * @return Response
     */
    @GET
    @Path ("mail/{id}/{mail}")
    @Blocking
    @Produces(MediaType.APPLICATION_JSON)
    public final Response sendMailWithAttachedTrainingSession(@PathParam("id") final long trainingSessionId, @PathParam("mail") final String mail) {
        if(trainingSessionId > 0 && mail.contains("@")) {
            return trainingSessionService.sendMailWithAttachedTrainingSession(trainingSessionId, mail);
        }
        return Response.status(400).build();
    }

    /**** POST-REQUEST-HANDLERS ****/
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public final Response createNewTrainingSession(final TrainingSession trainingSession) {
        if(trainingSession != null) {
            return trainingSessionService.createNewTrainingSession(trainingSession);
        }
        Log.error("Passed training session is null");
        return Response.status(400).build();
    }

    @POST
    @Path("/random")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public final Response createNewRandomTrainingSession(final TrainingSession trainingSession) {
        if(trainingSession != null) {
            return trainingSessionService.generateRandomTraining(trainingSession);
        }
        Log.error("Passed training session is null");
        return Response.status(400).build();
    }

    /**** PUT-REQUEST-HANDLERS ****/
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public final Response updateExistingTrainingSession(TrainingSession updatedTrainingSession) {
        if (updatedTrainingSession != null) {
            return trainingSessionService.updateExistingTrainingSession(updatedTrainingSession);
        }
        Log.error("Passed training session is null");
        return Response.status(400).build();
    }

    /**** DELETE-REQUEST-HANDLERS ****/
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public final Response deleteTrainingSession(@PathParam("id") long trainingSessionId) {
        if (trainingSessionId > 0) {
            return trainingSessionService.deleteTrainingSession(trainingSessionId);
        }
        Log.error("Passed training session id is null");
        return Response.status(400).build();
    }
}
