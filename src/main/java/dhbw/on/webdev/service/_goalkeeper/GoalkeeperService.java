package dhbw.on.webdev.service._goalkeeper;

import dhbw.on.webdev.model.dto.GoalkeeperDTO;
import dhbw.on.webdev.model.entities.Goalkeeper;
import dhbw.on.webdev.repository.GoalkeeperRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service.helper.JwtTokenService;
import dhbw.on.webdev.service.helper.ServiceHelper;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;


import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class GoalkeeperService {
    /**** CDI ****/
    @Inject
    GoalkeeperRepository goalkeeperRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    ServiceHelper serviceHelper;


    /**** GET-REQUEST-SERVICES ****/

    /**
     * Method for getting all goalkeepers from/saved by current user.
     * @return GoalkeeperDTO-List with goalkeepers in it
     */
    public List<GoalkeeperDTO> getAllGoalkeepersFromCurrentUser() {
        final long userId = jwtTokenService.getUserIdFromJwtToken();
        if(userId > 0) {
            return goalkeeperRepository.getGoalkeepersByField("user", userRepository.findById(userId));
        } else {
            return new ArrayList<>();
        }
    }

    /**** POST-REQUEST-SERVICES ****/

    /****
     * Method for adding a new goalkeeper to the database.
     * @param goalkeeper to add
     * @return Response ok() or serverError();
     */
    @Transactional
    public Response addNewGoalkeeper(Goalkeeper goalkeeper) {
        Log.info("Trying to add new goalkeeper: " + goalkeeper.getFirstname() + goalkeeper.getLastname());
        final long userId = jwtTokenService.getUserIdFromJwtToken();
        if(userId > 0) {
            try {
                goalkeeper.setUser(userRepository.findById(userId));
                goalkeeperRepository.persist(goalkeeper);
                Log.info("Adding goalkeeper successful");
                return Response.ok().build();
            } catch (Exception exception) {
                Log.error("Adding goalkeeper didn't work", exception);
            }
        }
        return Response.serverError().build();
    }

    /**** PUT-REQUEST-SERVICES ****/
    @Transactional
    public Response updateExistingGoalkeeper(Goalkeeper updatedGoalkeeper) {
        Goalkeeper goalkeeper = goalkeeperRepository.findById(updatedGoalkeeper.getId());
        if(goalkeeper != null) {
            if(serviceHelper.updateEntity(updatedGoalkeeper, goalkeeper)) {
                goalkeeperRepository.flush();
                return Response.accepted().build();
            } else  {
                Log.error("Updating entity failed");
                return Response.serverError().build();
            }
        } else {
            Log.error("Goalkeeper not found for Id" + updatedGoalkeeper.getId());
            return Response.status(404).build();
        }

    }

    /**** DELETE-REQUEST-SERVICES ****/
    @Transactional
    public Response deleteGoalkeeper(long goalkeeperId) {
        goalkeeperRepository.deleteById(goalkeeperId);
        return Response.accepted().build();
    }




}
