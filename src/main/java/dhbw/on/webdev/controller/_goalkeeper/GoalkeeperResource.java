package dhbw.on.webdev.controller._goalkeeper;

import dhbw.on.webdev.model.entities.Goalkeeper;
import dhbw.on.webdev.service._goalkeeper.GoalkeeperService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.eclipse.microprofile.openapi.annotations.enums.SecuritySchemeType;
import org.eclipse.microprofile.openapi.annotations.security.SecurityScheme;

import java.util.List;

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

    @GET
    @Path("/get/specific")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    public List<Goalkeeper> getAllGoalkeepersFromCurrentUser() {
        return goalkeeperService.getAllGoalkeepersFromCurrentUser();
    }

    @PUT
    @Path("/update")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExistingGoalkeeper(Goalkeeper goalkeeper) {
        return goalkeeperService.updateExistingGoalkeeper(goalkeeper);
    }

    @DELETE
    @Path("/delete/{id}")
    @RolesAllowed("user")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response deleteGoalkeeper(@PathParam("id") long goalkeeperId) {
        return goalkeeperService.deleteGoalkeeper(goalkeeperId);
    }
}
