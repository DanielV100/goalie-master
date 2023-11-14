package dhbw.on.webdev.controller._exercise;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.model.Goalkeeper;
import dhbw.on.webdev.service._exercise.ExerciseService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import java.util.List;

@Path("/exercise")
@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class ExerciseResource {
    @Inject
    ExerciseService exerciseService;

    /**** GET-REQUEST-HANDLERS ****/
    @GET
    @Path("/get/all")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }
    @GET
    @Path("/get/specific")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exercise> getAllExercisesFromCurrentUser() {
        return exerciseService.getAllExercisesFromCurrentUser();
    }

    /**** POST-REQUEST-HANDLERS ****/
    @POST
    @Path("/add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewExercise(Exercise exercise) {
        return exerciseService.addNewExercise(exercise);
    }

    /**** PUT-REQUEST-HANDLERS ****/

    @PUT
    @Path("/update")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExistingExercise(Exercise updatedExercise) {
        return exerciseService.updateExistingExercise(updatedExercise);
    }

    /**** DELETE-REQUEST-HANDLERS ****/
    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteExercise(@PathParam("id") long exerciseId) {
        return exerciseService.deleteExercise(exerciseId);
    }

}
