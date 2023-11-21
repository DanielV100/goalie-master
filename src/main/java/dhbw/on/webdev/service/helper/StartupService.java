package dhbw.on.webdev.service.helper;

import dhbw.on.webdev.repository.UserRepository;
import io.quarkus.logging.Log;
import io.quarkus.runtime.StartupEvent;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

/**
 * Class used for controlling what's happening on server start.
 * @author daniel
 */
@ApplicationScoped
public class StartupService {
    @Inject
    UserRepository userRepository;

    /**
     * Startup event generates a new user into database, if no user exists.
     * @param startupEvent
     */
    @Transactional
    public void onStartup(@Observes StartupEvent startupEvent) {
        Log.info("Startup event triggered: " + startupEvent);
        //creating a new user, when no exists
        if(userRepository.count() == 0) {
            userRepository.createUserAndPersist("admin", "admin", "admin");
        }
    }

}
