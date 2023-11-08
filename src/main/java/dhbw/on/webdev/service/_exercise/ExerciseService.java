package dhbw.on.webdev.service._exercise;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.repository.ExerciseRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._login.JwtTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Objects;

@ApplicationScoped
public class ExerciseService {
    @Inject
    ExerciseRepository exerciseRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    /**
     * Adding new exercise to db.
     * @param exercise comes from client
     * @return http-response
     */
    @Transactional
    public Response addNewExercise(Exercise exercise) {
        exercise.user = userRepository.findById(jwtTokenService.getUserIdFromJwtToken());
        if(exercise.sketchDataURL == null) {
            exercise.sketch = null;
        } else {
            exercise.sketch = convertDataUrlToByteArray(exercise.sketchDataURL);
        }
        exerciseRepository.persist(exercise);
        return Response.ok().build();
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
     * Method for getting all exercises in db.
     * @return all exercises in db.
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

    /**
     * Sending plain user data to client could be a security risk, so set user data to null before passing to client.
     * @param exercises
     * @return list of exercises without userdata
     */
    private List<Exercise> hideUserInformationInResponse(List<Exercise> exercises) {
        List<Exercise> exercisesWithNoUserData = new ArrayList<>();
        for (Exercise exercise : exercises) {
            exercise.user = null;
            exercisesWithNoUserData.add(exercise);
        }
        return exercisesWithNoUserData;
    }
}
