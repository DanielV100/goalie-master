package dhbw.on.webdev.controller._login;

import dhbw.on.webdev.model.entities.UserCredentials;
import dhbw.on.webdev.service.helper.JwtTokenService;
import dhbw.on.webdev.service._login.UserService;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.ws.rs.*;

import jakarta.inject.Inject;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * REST-Endpoint-Handlers for login / user entity.
 * @author daniel
 */
@Path("/login")
public class LoginResource {
    @Inject
    UserService userService;

    @POST
    @PermitAll
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response loginUser(final UserCredentials userCredentials) {
        if(userCredentials != null) {
            return userService.loginUser(userCredentials);
        }
        return Response.status(400).build();
    }

    @PUT
    @RolesAllowed("user")
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.TEXT_PLAIN)
    public Response changeUsersPassword(final String password) {
        if(password != null && password.length() >= 6) {
            return userService.changeUsersPassword(password);
        }
        return Response.status(400).build();
    }

}

