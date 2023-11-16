package dhbw.on.webdev.controller._training_session;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.model.TrainingSession;
import dhbw.on.webdev.service._training_session.TrainingSessionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import java.util.List;

@Path("/training_session")
@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class TrainingSessionResource {
    @Inject
    TrainingSessionService trainingSessionService;

    @GET
    @Path("/get/specific")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public List<TrainingSession> getAllTrainingSessionsFromCurrentUser() {
        return trainingSessionService.getAllTrainingSessionsFromCurrentUser();
    }

    @POST
    @Path("/create")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response createNewTrainingSession(TrainingSession trainingSession) {
        return trainingSessionService.createNewTrainingSession(trainingSession);
    }

    @PUT
    @Path("/update")

    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExistingTrainingSession(TrainingSession updatedTrainingSession) {
        return trainingSessionService.updateExistingTrainingSession(updatedTrainingSession);
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteTrainingSession(@PathParam("id") long trainingSessionId) {
        return trainingSessionService.deleteTrainingSession(trainingSessionId);
    }
}
