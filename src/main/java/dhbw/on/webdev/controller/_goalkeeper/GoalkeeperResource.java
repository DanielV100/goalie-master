package dhbw.on.webdev.controller._goalkeeper;

import dhbw.on.webdev.model.Goalkeeper;
import dhbw.on.webdev.service._goalkeeper.GoalkeeperService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.Consumes;
import jakarta.ws.rs.POST;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

@Path("/goalkeeper")
@SecurityScheme(
        scheme = "bearer",
        type = SecuritySchemeType.HTTP,
        bearerFormat = "JWT"
)
public class GoalkeeperResource {
    @Inject
    GoalkeeperService goalkeeperService;

    @POST
    @Path("/add")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewGoalkeeper(Goalkeeper goalkeeper) {
        return goalkeeperService.addNewGoalkeeper(goalkeeper);
    }
}
