package dhbw.on.webdev.service.helper;

import dhbw.on.webdev.repository.UserRepository;
import io.quarkus.logging.Log;
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
    public void onStartup(@Observes StartupEvent startupEvent) {
        Log.info("Startup event triggered: " + startupEvent);
        //creating a new user, when no exists
        if(userRepository.count() == 0) {
            userRepository.createUserAndPersist("admin", "admin", "admin");
        }
    }

}
