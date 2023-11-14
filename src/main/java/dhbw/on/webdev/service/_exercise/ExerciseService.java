package dhbw.on.webdev.service._exercise;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.repository.ExerciseRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._login.JwtTokenService;
import dhbw.on.webdev.service.helper.ServiceHelper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;

/**
 * This service class provides all exercise-entity-related services.
 * @author daniel
 */
@ApplicationScoped
public class ExerciseService {
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
     * Method for getting all exercises in db.
     * @return all exercises in db as list
     */
    public List<Exercise> getAllExercises() {
        return hideUserInformationInResponse(exerciseRepository.listAll());
    }

    /**
     * Method for getting all exercises of current user.
     * @return list of exercises of current user.
     */
    public List<Exercise> getAllExercisesFromCurrentUser() {
        return hideUserInformationInResponse(exerciseRepository.list("user", userRepository.findById(jwtTokenService.getUserIdFromJwtToken())));
    }

    /**** POST-REQUEST-SERVICES ****/

    /**
     * Adding new exercise to db.
     * @param exercise comes from client
     * @return http-response ok or server error
     */
    @Transactional
    public Response addNewExercise(Exercise exercise) {
        exercise.setUser(serviceHelper.getCurrentUser(jwtTokenService.getUserIdFromJwtToken()));
        if(exercise.getSketchDataURL() == null) {
            exercise.setSketch(null);
        } else {
            exercise.setSketch(convertDataUrlToByteArray(exercise.getSketchDataURL()));
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
    @Transactional
    public Response updateExistingExercise(Exercise updatedExercise) {
        Exercise exercise = exerciseRepository.findById(updatedExercise.getId());
        if(exercise != null) {
            //sketch must be set here, otherwise helper method have to be edited
            if(updatedExercise.getSketchDataURL() != null) {
                exercise.setSketch(convertDataUrlToByteArray(updatedExercise.getSketchDataURL()));
            } else {
                Log.warn("No data url found");
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

    @Transactional
    public Response deleteExercise(long exerciseId) {
        exerciseRepository.deleteById(exerciseId);
        return Response.accepted().build();
    }



    /**
     * Sketch is saved a bytea and not as text - so this method converts incoming data url to byte array.
     * @param dataURL from client
     * @return data url as byte array
     */
    private byte[] convertDataUrlToByteArray(String dataURL) {
        return Base64.getDecoder().decode(dataURL.split(",")[1]);
        //System.out.println("data:image/png;base64," + Base64.getEncoder().encodeToString(sktech));
    }

    /**
     * Sending plain user data to client could be a security risk, so set user data to null before passing to client.
     * @param exercises
     * @return list of exercises without userdata
     */
    private List<Exercise> hideUserInformationInResponse(List<Exercise> exercises) {
        List<Exercise> exercisesWithNoUserData = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exercise.setUser(null);
            exercisesWithNoUserData.add(exercise);
        }
        return exercisesWithNoUserData;
    }



}
