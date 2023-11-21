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
import dhbw.on.webdev.service.helper.JwtTokenService;
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
    /**** CDI ****/
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
    MailService mailService;

    /**** GET-REQUEST-SERVICES ****/
    /**
     * Getting all training sessions from current user and hiding sensitive data.
     * @return Training session or an empty array list
     */
    public List<TrainingSession> getAllTrainingSessionsFromCurrentUser() {
        try {
            return clearUnnecessaryDataForResponse(trainingSessionRepository.list("user", userRepository.findById(jwtTokenService.getUserIdFromJwtToken())));
        } catch (NullPointerException exception) {
            return new ArrayList<>();
        }
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
        } catch (final Exception exception) {
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
    /**** POST-REQUEST-SERVICES ****/

    /**
     * Method for adding a new training session to the db.
     * @param trainingSession from client
     * @return Response ok() or serverError();
     */
    @Transactional
    public Response createNewTrainingSession(final TrainingSession trainingSession) {
        Log.info("Trying to add new training session");
        trainingSession.setExercises(new ArrayList<>());
        trainingSession.setGoalkeepers(new ArrayList<>());
        if(trainingSession.getGoalkeeperIds() != null) {
            for(long goalkeeperId : trainingSession.getGoalkeeperIds()) {
                //new instance needed, otherwise it's a detached entity
                Goalkeeper goalkeeper = goalkeeperRepository.findById(goalkeeperId);
                trainingSession.getGoalkeepers().add(goalkeeper);
            }
        } else {
            Log.warn("No goalkeepers found in training session: " + trainingSession.getTitle());
        }
        if(trainingSession.getExerciseIds() != null) {
            for(long exerciseId : trainingSession.getExerciseIds()) {
                Exercise exercise = exerciseRepository.findById(exerciseId);
                trainingSession.getExercises().add(exercise);
            }
        } else {
            Log.warn("No exercises found in training session: " + trainingSession.getTitle());
        }
        try {
            trainingSession.setUser(userRepository.findById(jwtTokenService.getUserIdFromJwtToken()));
            trainingSessionRepository.persist(trainingSession);
            return Response.ok().build();
        } catch (Exception exception) {
            Log.error("Error occured while persisting training session", exception);
            return Response.serverError().build();
        }
    }

    @Transactional
    public Response generateRandomTraining(TrainingSession trainingSession) {
        final long userId = jwtTokenService.getUserIdFromJwtToken();
        if(userId > 0) {
            TrainingSession randomTrainingSession = new TrainingSession(trainingSession.getTitle(), trainingSession.getDate());
            List<Long> exerciseIds = new ArrayList<>();
            List<Exercise> exercises = new ArrayList<>();
            if(trainingSession.getGoalkeeperIds() != null) {
                trainingSession.setGoalkeeperIds(trainingSession.getGoalkeeperIds());
                exercises = exerciseRepository.getExercisesByMaximumNumberOfGoalkeeper(trainingSession.getGoalkeeperIds().size(), userId);
            }  else {
                exercises = exerciseRepository.list("user", userId);
            }
            if(exercises.size() > 5) {
                exerciseIds = new ArrayList<>();
                for (Exercise exercise : exercises) {
                    exerciseIds.add(exercise.getId());
                }
            }
            System.out.println("Exercise ID'S" + exerciseIds.size());
            Collections.shuffle(exerciseIds);
            randomTrainingSession.setExerciseIds(exerciseIds.subList(0,5));
            randomTrainingSession.setUser(userRepository.findById(userId));
            createNewTrainingSession(randomTrainingSession);

        }
        return Response.ok().build();
    }

    /**** PUT-REQUEST-SERVICES ****/
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


    /**** DELETE-REQUEST-SERVICES ****/
    @Transactional
    public Response deleteTrainingSession(long trainingSessionId) {
        trainingSessionRepository.deleteById(trainingSessionId);
        return Response.accepted().build();
    }


    /**
     * Hiding all sensitive data from response. Better way to do
     * it is via DTO-Pattern - but in this case it's somehow an overhead.
     * @param trainingSessions as list to clear
     * @return cleared training session as list
     */
    private List<TrainingSession> clearUnnecessaryDataForResponse(List<TrainingSession> trainingSessions) {
        Log.info("Trying to clear sensitive data from training session");
        if(trainingSessions != null) {
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
                trainingSession.setUser(null);
            }
            return trainingSessions;
        } else {
            Log.error("No training session found");
            throw new NullPointerException("No training session found");
        }
    }
}
