package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.entities.TrainingSession;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class TrainingSessionRepository implements PanacheRepository<TrainingSession> {
}
