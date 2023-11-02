package dhbw.on.webdev.service._goalkeeper;

import dhbw.on.webdev.model.Goalkeeper;
import dhbw.on.webdev.repository.GoalkeeperRepository;
import dhbw.on.webdev.repository.UserRepository;
import dhbw.on.webdev.service._login.JwtTokenService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.core.Response;

@ApplicationScoped
public class GoalkeeperService {
    @Inject
    GoalkeeperRepository goalkeeperRepository;

    @Inject
    UserRepository userRepository;

    @Inject
    JwtTokenService jwtTokenService;

    @Transactional
    public Response addNewGoalkeeper(Goalkeeper goalkeeper) {
        goalkeeper.user = userRepository.findById(jwtTokenService.getUserIdFromJwtToken());
        goalkeeperRepository.persist(goalkeeper);
        return Response.ok().build();
    }
}
