package dhbw.on.webdev.controller._goalkeeper;

import dhbw.on.webdev.model.dto.GoalkeeperDTO;
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
@RolesAllowed("user")
public class GoalkeeperResource {
    /**** CDI ****/
    @Inject
    GoalkeeperService goalkeeperService;

    /**** GET-REQUEST-HANDLERS ****/
    @GET
    @Path("/get/specific")
    @Produces(MediaType.APPLICATION_JSON)
    public List<GoalkeeperDTO> getAllGoalkeepersFromCurrentUser() {
        return goalkeeperService.getAllGoalkeepersFromCurrentUser();
    }

    /**** POST-REQUEST-HANDLERS ****/
    @POST
    @Path("/add")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addNewGoalkeeper(Goalkeeper goalkeeper) {
        return goalkeeperService.addNewGoalkeeper(goalkeeper);
    }

    /**** PUT-REQUEST-HANDLERS ****/
    @PUT
    @Path("/update")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateExistingGoalkeeper(Goalkeeper goalkeeper) {
        return goalkeeperService.updateExistingGoalkeeper(goalkeeper);
    }


    /**** DELETE-REQUEST-HANDLERS ****/
    @DELETE
    @Path("/delete/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteGoalkeeper(@PathParam("id") long goalkeeperId) {
        return goalkeeperService.deleteGoalkeeper(goalkeeperId);
    }
}
