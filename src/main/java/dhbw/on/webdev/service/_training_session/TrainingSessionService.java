package dhbw.on.webdev.service._training_session;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.model.Goalkeeper;
import dhbw.on.webdev.model.TrainingSession;
import dhbw.on.webdev.repository.ExerciseRepository;
import dhbw.on.webdev.repository.GoalkeeperRepository;
import dhbw.on.webdev.repository.TrainingSessionRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._login.JwtTokenService;
import dhbw.on.webdev.service.helper.ServiceHelper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TrainingSessionService {
    @Inject
    TrainingSessionRepository trainingSessionRepository;

    @Inject
    GoalkeeperRepository goalkeeperRepository;


    @Inject
    ExerciseRepository exerciseRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    ServiceHelper serviceHelper;

    @Transactional
    public Response createNewTrainingSession(TrainingSession trainingSession) {
        trainingSession.setExercises(new ArrayList<>());
        trainingSession.setGoalkeepers(new ArrayList<>());
        for(long goalkeeperId : trainingSession.getGoalkeeperIds()) {
            //new instance needed, otherwise it's a detached entity
            Goalkeeper goalkeeper = goalkeeperRepository.findById(goalkeeperId);
            trainingSession.getGoalkeepers().add(goalkeeper);
        }
        for(long exerciseId : trainingSession.getExerciseIds()) {
            Exercise exercise = exerciseRepository.findById(exerciseId);
            trainingSession.getExercises().add(exercise);
        }
        trainingSession.setUser(userRepository.findById(jwtTokenService.getUserIdFromJwtToken()));
        trainingSessionRepository.persist(trainingSession);
        return Response.ok().build();
    }

    public List<TrainingSession> getAllTrainingSessionsFromCurrentUser() {
        return clearUnnecessaryDataForResponse(trainingSessionRepository.list("user", userRepository.findById(jwtTokenService.getUserIdFromJwtToken())));
    }

    //Wieso User von Goalkeeper und Exercises nicht gleich bei der Abfrage bereinigen?
    private List<TrainingSession> clearUnnecessaryDataForResponse(List<TrainingSession> trainingSessions) {
        for(TrainingSession trainingSession : trainingSessions) {
            List<Goalkeeper> goalkeepers = trainingSession.getGoalkeepers();
            for(Goalkeeper goalkeeper : goalkeepers) {
                goalkeeper.setUser(null);
                goalkeeper.setBirthday(null);
                goalkeeper.setClub(null);
                goalkeeper.setNotes(null);
            }
            List<Exercise> exercises = trainingSession.getExercises();
            for(Exercise exercise : exercises) {
                exercise.setUser(null);
            }
        }
        return trainingSessions;
    }

    @Transactional
    public Response deleteTrainingSession(long trainingSessionId) {
        goalkeeperRepository.deleteById(trainingSessionId);
        return Response.accepted().build();
    }

    @Transactional
    public Response updateExistingTrainingSession(TrainingSession updatedTrainingSession) {
        TrainingSession trainingSession = trainingSessionRepository.findById(updatedTrainingSession.getId());
        if(trainingSession != null) {
            if(updatedTrainingSession.getExerciseIds() != null) {
                trainingSession.setExercises(new ArrayList<>());
                for(long exerciseId : updatedTrainingSession.getExerciseIds()) {
                    Exercise exercise = exerciseRepository.findById(exerciseId);
                    trainingSession.getExercises().add(exercise);
                }
            } else {
                Log.warn("No exercises found");
            }
            if(updatedTrainingSession.getGoalkeeperIds() != null) {
                trainingSession.setGoalkeepers(new ArrayList<>());
                for(long goalkeeperId : updatedTrainingSession.getGoalkeeperIds()) {
                    //new instance needed, otherwise it's a detached entity
                    Goalkeeper goalkeeper = goalkeeperRepository.findById(goalkeeperId);
                    trainingSession.getGoalkeepers().add(goalkeeper);
                }
            } else {
                Log.warn("No goalkeepers found");
            }
            if(serviceHelper.updateEntity(updatedTrainingSession, trainingSession)) {
                exerciseRepository.flush();
                return Response.ok().build();
            } else {
                return Response.serverError().build();
            }
        } else {
            Log.error("Exercise not found for Id:" + updatedTrainingSession.getId());
            return Response.status(404).build();
        }
    }
}
