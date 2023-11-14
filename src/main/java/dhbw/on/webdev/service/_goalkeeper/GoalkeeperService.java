package dhbw.on.webdev.service._goalkeeper;

import dhbw.on.webdev.model.Exercise;
import dhbw.on.webdev.model.Goalkeeper;
import dhbw.on.webdev.repository.GoalkeeperRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._login.JwtTokenService;
import dhbw.on.webdev.service.helper.ServiceHelper;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@ApplicationScoped
public class GoalkeeperService {
    @Inject
    GoalkeeperRepository goalkeeperRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;


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

    public List<Goalkeeper> getAllGoalkeepersFromCurrentUser() {
        return hideUserInformationInResponse(goalkeeperRepository.list("user", userRepository.findById(jwtTokenService.getUserIdFromJwtToken())));
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
    public Response updateExistingGoalkeeper(Goalkeeper goalkeeper) {
        ServiceHelper.updateEntity(goalkeeper, goalkeeperRepository.findById(goalkeeper.getId()));
        goalkeeperRepository.flush();
        return Response.accepted().build();
    }

    @Transactional
    public Response deleteGoalkeeper(long goalkeeperId) {
        goalkeeperRepository.deleteById(goalkeeperId);
        return Response.accepted().build();
    }


}
