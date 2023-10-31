package dhbw.on.webdev.service._login;

import dhbw.on.webdev.model.User;
import dhbw.on.webdev.model.UserCredentials;
import dhbw.on.webdev.repository.UserRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.core.Response;

import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class UserService {
    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenGenerator jwtTokenGenerator;

    public List<User> getAllUsers() {
        return userRepository.listAll();
    }
    public Response loginUser(UserCredentials userCredentials) {
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        List<User> allUsers = getAllUsers();
        for (User user : allUsers) {
            if(user.username.equals(username)) {
                if(user.password.equals(password)) {
                    return Response.ok(jwtTokenGenerator.generateJwtToken()).build();
                }
            }
        }
        return Response.status(401).build();
    }

}
