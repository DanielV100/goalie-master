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
    @Inject
    GoalkeeperRepository goalkeeperRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @Inject
    ServiceHelper serviceHelper;



    @Transactional
    /**
     * Method for adding new goalkeeper to db.
     */
    public Response addNewGoalkeeper(Goalkeeper goalkeeper) {
        try {
            //setting primary key to user id from JWT-token
            goalkeeper.setUser(userRepository.findById(jwtTokenService.getUserIdFromJwtToken()));
            goalkeeperRepository.persist(goalkeeper);
            return Response.ok().build();
        } catch (Exception exception) {
            System.out.println(exception);
            return Response.serverError().build();
        }
    }

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

    private List<Goalkeeper> hideUserInformationInResponse(List<Goalkeeper> goalkeepers) {
        List<Goalkeeper> goalkeepersWithNoUserData = new ArrayList<>();
        for (Goalkeeper goalkeeper : goalkeepers) {
            goalkeeper.setUser(null);
            goalkeepersWithNoUserData.add(goalkeeper);
        }
        return goalkeepersWithNoUserData;
    }

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

    @Transactional
    public Response deleteGoalkeeper(long goalkeeperId) {
        goalkeeperRepository.deleteById(goalkeeperId);
        return Response.accepted().build();
    }




}
