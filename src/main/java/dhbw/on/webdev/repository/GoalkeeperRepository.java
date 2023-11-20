package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.dto.GoalkeeperDTO;
import dhbw.on.webdev.model.entities.Goalkeeper;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Panache repo for accessing the goalkeepers in the db.
 */
@ApplicationScoped
public class GoalkeeperRepository implements PanacheRepository<Goalkeeper> {
    /**
     * Method for getting goalkeepers by fields and field value.
     * @param field id, firstname, lastname, club, notes...
     * @param fieldValue as a object
     * @return List of matching goalkeepers
     */
    public List<GoalkeeperDTO> getGoalkeepersByField(final String field, final Object fieldValue) {
        Log.info("Getting goalkeepers by: " + field);
        PanacheQuery<GoalkeeperDTO> goalkeeperDTOPanacheQuery = find(field, fieldValue).project(GoalkeeperDTO.class);
        return goalkeeperDTOPanacheQuery.list();
    }
}
