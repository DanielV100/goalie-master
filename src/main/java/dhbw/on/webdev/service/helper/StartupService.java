package dhbw.on.webdev.service.helper;

import dhbw.on.webdev.model.entities.User;
import dhbw.on.webdev.repository.UserRepository;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class StartupService {
    @Inject
    UserRepository userRepository;
    @Transactional
    void createUserOnStartup(@Observes StartupEvent ev) {
        userRepository.createUserAndPersist("admin", "admin", "admin");
    }

}
