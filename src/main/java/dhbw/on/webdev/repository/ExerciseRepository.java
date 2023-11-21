package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.dto.ExerciseDTO;
import dhbw.on.webdev.model.dto.GoalkeeperDTO;
import dhbw.on.webdev.model.entities.Exercise;
import dhbw.on.webdev.model.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Panache repo for accessing the exercises in the db.
 */
@ApplicationScoped
public class ExerciseRepository implements PanacheRepository<Exercise> {
    /**
     * Method for getting goalkeepers by fields and field value.
     * @important use this for response to client - because it's a dto!
     * @param field id, title...
     * @param fieldValue as a object
     * @return List of matching exercises
     */
    public List<ExerciseDTO> getExercisesByField(final String field, final Object fieldValue) {
        Log.info("Getting exercises by: " + field);
        PanacheQuery<ExerciseDTO> exerciseDTOPanacheQuery = find(field, fieldValue).project(ExerciseDTO.class);
        return exerciseDTOPanacheQuery.list();
    }

    /**
     * Method for getting all exercises from user with a minimum amount of goalkeepers.
     * @param maximumNumberOfGoalkeepers
     * @param user
     * @return
     */
    public List<Exercise> getExercisesByMaximumNumberOfGoalkeeper(final int maximumNumberOfGoalkeepers, final User user) {
        Log.info("Getting all exercises ");
        return list("numberOfGoalkeepers <= ?1 and user = ?2", maximumNumberOfGoalkeepers, user);
    }
}
