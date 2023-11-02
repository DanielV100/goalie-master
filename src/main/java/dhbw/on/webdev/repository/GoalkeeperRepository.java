package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.Goalkeeper;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class GoalkeeperRepository implements PanacheRepository<Goalkeeper> {
}
