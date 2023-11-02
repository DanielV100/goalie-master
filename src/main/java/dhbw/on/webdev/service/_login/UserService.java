package dhbw.on.webdev.service._login;

import dhbw.on.webdev.model.User;
import dhbw.on.webdev.model.UserCredentials;
import dhbw.on.webdev.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;
import java.util.List;

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
    public Response loginUser(UserCredentials userCredentials) {
        User user = getAuthenticatedUser(userCredentials, getAllUsers());
        if(user != null) {
            return Response.ok(jwtTokenService.generateJwtToken(user.id)).build();
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
    private User getAuthenticatedUser(UserCredentials userCredentials, List<User> allUsers) {
        User authenticatedUser = null;
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        for (User user : allUsers) {
            if(user.username.equals(username)) {
                if(user.password.equals(password)) {
                    authenticatedUser = user;
                }
            }
        }
        return authenticatedUser;
    }
}
