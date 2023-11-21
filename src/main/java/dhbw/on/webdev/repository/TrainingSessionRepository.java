package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.entities.TrainingSession;
import dhbw.on.webdev.model.entities.User;
import io.quarkus.hibernate.orm.panache.PanacheQuery;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.panache.common.Sort;
import jakarta.enterprise.context.ApplicationScoped;

import java.util.List;

/**
 * Repository for training sessions.
 * @author daniel
 */
@ApplicationScoped
public class TrainingSessionRepository implements PanacheRepository<TrainingSession> {

    public List<TrainingSession> getAllTrainingSessionsOrderedByDate(final User user) {
        PanacheQuery<TrainingSession> query = find("user = ?1", Sort.by("date").descending(), user);
        return query.list();
    }
}
