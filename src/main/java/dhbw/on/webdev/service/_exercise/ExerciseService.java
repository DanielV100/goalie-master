package dhbw.on.webdev.service._exercise;

import dhbw.on.webdev.model.dto.ExerciseDTO;
import dhbw.on.webdev.model.entities.Exercise;
import dhbw.on.webdev.repository.ExerciseRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service.helper.JwtTokenService;
import dhbw.on.webdev.service.helper.ServiceHelper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.List;

/**
 * This service class provides all exercise-entity-related services.
 * @author daniel
 */
@ApplicationScoped
public class ExerciseService {
    /**** CDI ****/
    @Inject
    ExerciseRepository exerciseRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    ServiceHelper serviceHelper;

    /**** GET-REQUEST-SERVICES ****/

    /**
     * Method for getting all exercises of current user.
     * @return list of exercises of current user.
     */
    public List<ExerciseDTO> getAllExercisesFromCurrentUser() {
        Log.info("Getting all exercises from current user");
        final long userId = jwtTokenService.getUserIdFromJwtToken();
        if(userId > 0) {
            return exerciseRepository.getExercisesByField("user", userRepository.findById(userId));
        } else {
            return new ArrayList<>();
        }
    }

    /**** POST-REQUEST-SERVICES ****/

    /**
     * Adding new exercise to db.
     * @param exercise comes from client
     * @return http-response ok or server error
     */
    @Transactional
    public Response addNewExercise(Exercise exercise) {
        Log.info("Trying to add new exercises: " + exercise.getTitle());
        exercise.setUser(serviceHelper.getCurrentUser(jwtTokenService.getUserIdFromJwtToken()));
        if(exercise.getSketchDataURL() == null) {
            Log.warn("No sketch sent by client");
            exercise.setSketch(null);
        } else {
            Log.info("Sketch found and set");
            exercise.setSketch(serviceHelper.convertDataUrlToByteArray(exercise.getSketchDataURL()));
        }
        try {
            exerciseRepository.persist(exercise);
            return Response.ok().build();
        } catch (Exception exception) {
            Log.error("Error while trying to persist entity");
            return Response.serverError().build();
        }
    }

    /**** PUT-REQUEST-SERVICES ****/

    /**
     * Method for updating an existing exercise.
     * @param updatedExercise entity from client
     * @return Response ok, server error, or 404
     */
    @Transactional
    public Response updateExistingExercise(Exercise updatedExercise) {
        Log.info("Trying to update exercise");
        Exercise exercise = exerciseRepository.findById(updatedExercise.getId());
        if(exercise != null) {
            //sketch must be set here, otherwise helper method have to be edited, which is more complex
            if(updatedExercise.getSketchDataURL() != null) {
                exercise.setSketch(serviceHelper.convertDataUrlToByteArray(updatedExercise.getSketchDataURL()));
            } else {
                Log.warn("No data url found for exercise: " + updatedExercise.getTitle());
            }
            if(serviceHelper.updateEntity(updatedExercise, exercise)) {
                exerciseRepository.flush();
                return Response.ok().build();
            } else {
                return Response.serverError().build();
            }
        } else {
            Log.error("Exercise not found for Id:" + updatedExercise.getId());
            return Response.status(404).build();
        }
    }


    /**** DELETE-REQUEST-SERVICES ****/

    /**
     * Method for deleting an exercise by its id.
     * @param exerciseId
     * @return Resonse ok(), serverError or http-status code 404
     */
    @Transactional
    public Response deleteExercise(final long exerciseId) {
        Log.info("Trying to delete exercise");
        try {
            if(exerciseRepository.deleteById(exerciseId)) {
                return Response.ok().build();
            } else {
                Log.error("Exercise not found by Id: " + exerciseId);
                return Response.status(404).build();
            }
        } catch (Exception exception) {
            Log.error("Deletion of goalkeeper failed ", exception);
            return Response.serverError().build();
        }
    }
}
