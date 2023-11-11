package dhbw.on.webdev.service._training_session;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.model.Goalkeeper;
import dhbw.on.webdev.model.TrainingSession;
import dhbw.on.webdev.repository.ExerciseRepository;
import dhbw.on.webdev.repository.GoalkeeperRepository;
import dhbw.on.webdev.repository.TrainingSessionRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._login.JwtTokenService;
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

    @Transactional
    public Response createNewTrainingSession(TrainingSession trainingSession) {
        trainingSession.exercises = new ArrayList<>();
        trainingSession.goalkeepers = new ArrayList<>();

        for(long goalkeeperId : trainingSession.goalkeeperIds) {
            //new instance needed, otherwise it's a detached entity
            Goalkeeper goalkeeper = goalkeeperRepository.findById(goalkeeperId);
            trainingSession.goalkeepers.add(goalkeeper);
        }
        for(long exerciseId : trainingSession.exerciseIds) {
            Exercise exercise = exerciseRepository.findById(exerciseId);
            trainingSession.exercises.add(exercise);
        }
        trainingSession.user = userRepository.findById(jwtTokenService.getUserIdFromJwtToken());
        trainingSessionRepository.persist(trainingSession);
        return Response.ok().build();
    }

    public List<TrainingSession> getAllTrainingSessionsFromCurrentUser() {
        return clearUnnecessaryDataForResponse(trainingSessionRepository.list("user", userRepository.findById(jwtTokenService.getUserIdFromJwtToken())));
    }

    //Wieso User von Goalkeeper und Exercises nicht gleich bei der Abfrage bereinigen?
    private List<TrainingSession> clearUnnecessaryDataForResponse(List<TrainingSession> trainingSessions) {
        for(TrainingSession trainingSession : trainingSessions) {
            List<Goalkeeper> goalkeepers = trainingSession.goalkeepers;
            for(Goalkeeper goalkeeper : goalkeepers) {
                goalkeeper.user = null;
                goalkeeper.birthday = null;
                goalkeeper.club = null;
                goalkeeper.notes = null;
            }
            List<Exercise> exercises = trainingSession.exercises;
            for(Exercise exercise : exercises) {
                exercise.user = null;
            }
        }
        return trainingSessions;
    }
}
