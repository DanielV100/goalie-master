package dhbw.on.webdev.controller._exercise;

import dhbw.on.webdev.model.dto.ExerciseDTO;
import dhbw.on.webdev.model.entities.Exercise;
import dhbw.on.webdev.service._exercise.ExerciseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import java.util.List;

/**
 * REST-Endpoint-Handlers for exercise entity.
 * @author daniel
 */
@Path("/exercise")
@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
@RolesAllowed("user")
public class ExerciseResource {
    /**** CDI ****/
    @Inject
    ExerciseService exerciseService;

    /**** GET-REQUEST-HANDLERS ****/
    @GET
    @Path("/get/specific")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExerciseDTO> getAllExercisesFromCurrentUser() {
        return exerciseService.getAllExercisesFromCurrentUser();
    }

    /**** POST-REQUEST-HANDLERS ****/
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewExercise(Exercise exercise) {
        if (exercise != null) {
            return exerciseService.addNewExercise(exercise);
        }
        return Response.status(400).build();
    }

    /**** PUT-REQUEST-HANDLERS ****/
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExistingExercise(Exercise updatedExercise) {
        if(updatedExercise != null) {
            return exerciseService.updateExistingExercise(updatedExercise);
        }
        return Response.status(400).build();
    }

    /**** DELETE-REQUEST-HANDLERS ****/
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteExercise(@PathParam("id") final long exerciseId) {
        return exerciseService.deleteExercise(exerciseId);
    }

}
