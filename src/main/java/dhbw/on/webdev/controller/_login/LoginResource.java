package dhbw.on.webdev.controller._login;

import dhbw.on.webdev.model.entities.UserCredentials;
import dhbw.on.webdev.service._login.JwtTokenService;
import dhbw.on.webdev.service._login.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.ws.rs.*;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

@Path("/login")
public class LoginResource {
    @Inject
    UserService userService;
    @Inject
    JwtTokenService jwtTokenService;

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(UserCredentials userCredentials) {
        return userService.loginUser(userCredentials);
    }

}

