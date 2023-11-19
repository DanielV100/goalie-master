package dhbw.on.webdev.controller._exercise;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    @Path("/get/all")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Exercise> getAllExercises() {
        return exerciseService.getAllExercises();
    }
    @GET
    @Path("/get/specific")
    @Produces(MediaType.APPLICATION_JSON)
    public List<ExerciseDTO> getAllExercisesFromCurrentUser() {
        ExerciseDTO exerciseDTO =  exerciseService.getAllExercisesFromCurrentUser().get(0);
        System.out.println(exerciseDTO.descriptionSteps());
        ObjectMapper mapper = new ObjectMapper();
        try {
            List<String> list = mapper.readValue(exerciseDTO.descriptionSteps(), new TypeReference<List<String>>(){});
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return null;
    }

    /**** POST-REQUEST-HANDLERS ****/
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewExercise(Exercise exercise) {
        return exerciseService.addNewExercise(exercise);
    }

    /**** PUT-REQUEST-HANDLERS ****/
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExistingExercise(Exercise updatedExercise) {
        return exerciseService.updateExistingExercise(updatedExercise);
    }

    /**** DELETE-REQUEST-HANDLERS ****/
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteExercise(@PathParam("id") long exerciseId) {
        return exerciseService.deleteExercise(exerciseId);
    }

}
