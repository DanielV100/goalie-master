package dhbw.on.webdev.service._login;

import dhbw.on.webdev.model.entities.User;
import dhbw.on.webdev.model.entities.UserCredentials;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service.helper.JwtTokenService;
import io.quarkus.elytron.security.common.BcryptUtil;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.sql.PreparedStatement;
import java.util.List;

/**
 * @author daniel
 */
@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    /**
     * Gets all users from users table in db.
     * @return all users as string list
     */
    public List<User> getAllUsers() {
        return userRepository.listAll();
    }


    /**
     * Send the JWT-Token to authenticated users.
     * @param userCredentials
     * @return http-status-code
     */
    public Response loginUser(final UserCredentials userCredentials) {
        User user = getAuthenticatedUser(userCredentials, getAllUsers());
        if(user != null) {
            return Response.ok(jwtTokenService.generateJwtToken(user.getId(), user.getName())).build();
        } else {
            return Response.status(401).build();
        }
    }

    /**
     * Checks if user and password from client are in server and returns user.
     * @param userCredentials
     * @param allUsers
     * @return User as a objet or null
     */
    private User getAuthenticatedUser(final UserCredentials userCredentials, final List<User> allUsers) {
        User authenticatedUser = null;
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        for (User user : allUsers) {
            if(user.getUsername().equals(username)) {
                if(BcryptUtil.matches(password, user.getPassword())) {
                    authenticatedUser = user;
                }
            }
        }
        return authenticatedUser;
    }

    /**
     * Fast way to chang users password.
     * @param newPassword from client
     * @return Response ok(), serverError()
     */
    @Transactional
    public Response changeUsersPassword(final String newPassword) {
        User user = userRepository.findById(jwtTokenService.getUserIdFromJwtToken());
        if(user != null) {
            user.setPassword(userRepository.hashPassword(newPassword));
            return Response.ok().build();
        }
        return Response.serverError().build();
    }
}
