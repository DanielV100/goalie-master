package dhbw.on.webdev.service._training_session;

import dhbw.on.webdev.model.entities.Exercise;
import dhbw.on.webdev.model.entities.Goalkeeper;
import dhbw.on.webdev.model.entities.TrainingSession;
import dhbw.on.webdev.service.helper.MailService;
import dhbw.on.webdev.service.helper.PdfService;
import dhbw.on.webdev.repository.ExerciseRepository;
import dhbw.on.webdev.repository.GoalkeeperRepository;
import dhbw.on.webdev.repository.TrainingSessionRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._exercise.ExerciseService;
import dhbw.on.webdev.service._login.JwtTokenService;
import dhbw.on.webdev.service.helper.ServiceHelper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@ApplicationScoped
public class TrainingSessionService {
    @Inject
    TrainingSessionRepository trainingSessionRepository;

    @Inject
    GoalkeeperRepository goalkeeperRepository;

    @Inject
    PdfService pdfService;

    @Inject
    ExerciseRepository exerciseRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    ServiceHelper serviceHelper;

    @Inject
    ExerciseService exerciseService;

    @Inject
    MailService mailService;

    @Transactional
    public Response createNewTrainingSession(TrainingSession trainingSession) {
        trainingSession.setExercises(new ArrayList<>());
        trainingSession.setGoalkeepers(new ArrayList<>());
        if(trainingSession.getGoalkeeperIds() != null) {
            for(long goalkeeperId : trainingSession.getGoalkeeperIds()) {
                //new instance needed, otherwise it's a detached entity
                Goalkeeper goalkeeper = goalkeeperRepository.findById(goalkeeperId);
                trainingSession.getGoalkeepers().add(goalkeeper);
            }
        }
        if(trainingSession.getExerciseIds() != null) {
            for(long exerciseId : trainingSession.getExerciseIds()) {
                Exercise exercise = exerciseRepository.findById(exerciseId);
                trainingSession.getExercises().add(exercise);
            }
        }

        trainingSession.setUser(userRepository.findById(jwtTokenService.getUserIdFromJwtToken()));
        trainingSessionRepository.persist(trainingSession);
        return Response.ok().build();
    }

    public List<TrainingSession> getAllTrainingSessionsFromCurrentUser() {
        return clearUnnecessaryDataForResponse(trainingSessionRepository.list("user", userRepository.findById(jwtTokenService.getUserIdFromJwtToken())));
    }

    /**
     * Method for getting training session as a pdf file.
     * @param trainingSessionId id for training session
     * @return Response ok with pdf or serverError()
     */
    public Response getTrainingSessionAsPdf(final long trainingSessionId) {
        Log.info("Starting to get training session as pdf");
        TrainingSession trainingSession = trainingSessionRepository.findById(trainingSessionId);
        try {
            byte[] pdfBytes = pdfService.convertTrainingSessionToPdf(trainingSession);
            Log.info("Got training session as pdf");
            return Response.ok(pdfBytes)
                    .header("Content-Disposition", "attachment; filename=\"" + pdfService.getFilenameForPdf() + "\"")
                    .build();
        } catch (final IllegalArgumentException | NullPointerException exception) {
            return Response.serverError().build();
        }
    }

    /**
     * Method for sending a mail with the training session (as a pdf) from the passed id.
     * @param trainingSessionId which should be in the pdf
     * @param mail to send to - mail address
     * @return Response ok() or serverError()
     */
    public Response sendMailWithAttachedTrainingSession(final long trainingSessionId, final String mail) {
        Log.info("Starting to send training session as mail");
        TrainingSession trainingSession = trainingSessionRepository.findById(trainingSessionId);
        try {
            byte[] pdfBytes = pdfService.convertTrainingSessionToPdf(trainingSession);
            mailService.sendMail(pdfBytes, mail);
            Log.info("Finished sending mail");
            return Response.ok().build();
        } catch (Exception exception) {
            return Response.serverError().build();
        }
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
        generateRandomTraining("Test", LocalDate.parse("2018-12-27"));
        trainingSessionRepository.deleteById(trainingSessionId);
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

    @Transactional
    public void generateRandomTraining(final String title, final LocalDate date) {
        final long numberOfExercisesInDB = exerciseRepository.count();
        if (numberOfExercisesInDB > 5) {
            TrainingSession randomTrainingSession = new TrainingSession(title, date);
            List<Long> exerciseIds = new ArrayList<>();
            for (int i = 0; i < numberOfExercisesInDB; i++) {
                exerciseIds.add((long)i);
            }
            Collections.shuffle(exerciseIds);
            randomTrainingSession.setExerciseIds(exerciseIds.subList(0,5));
            randomTrainingSession.setUser(userRepository.findById(1L));
            createNewTrainingSession(randomTrainingSession);
        }
    }
}
