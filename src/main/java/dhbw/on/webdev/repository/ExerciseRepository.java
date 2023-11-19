package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.dto.ExerciseDTO;
import dhbw.on.webdev.model.dto.GoalkeeperDTO;
import dhbw.on.webdev.model.entities.Exercise;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

@ApplicationScoped
public class ExerciseRepository implements PanacheRepository<Exercise> {
    public List<ExerciseDTO> getExercisesByField(final String field, final Object fieldValue) {
        Log.info("Getting exercises by: " + field);
        PanacheQuery<ExerciseDTO> exerciseDTOPanacheQuery = find(field, fieldValue).project(ExerciseDTO.class);
        return exerciseDTOPanacheQuery.list();
    }
}
