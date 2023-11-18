package dhbw.on.webdev.repository;

import dhbw.on.webdev.model.entities.User;
import io.quarkus.elytron.security.common.BcryptUtil;
import io.quarkus.hibernate.orm.panache.PanacheRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.transaction.Transactional;

@ApplicationScoped
public class UserRepository implements PanacheRepository<User> {
    @Transactional
    public void createUserAndPersist(final String username, final String password, final String name) {
        User user = new User(username, name, hashPassword(password));
        persist(user);
    }

    private String hashPassword(final String password) {
        return BcryptUtil.bcryptHash(password);
    }
}
