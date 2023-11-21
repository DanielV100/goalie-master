package dhbw.on.webdev.service.helper;

import io.quarkus.logging.Log;
import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.time.Duration;


/**
 * Class for everything jwt token related.
 * @author daniel
 */
@ApplicationScoped
public class JwtTokenService {
    @Inject
    JsonWebToken jsonWebToken;

    /**
     * Method for generating JWT, which is then saved in session storage
     * @param userID
     * @return JWT-token as string
     */
    public String generateJwtToken(final Long userID, final String name) {
        return Jwt.issuer("goalie-master-backend")
                .subject("goalie-master-frontend")
                .groups("user")
                .claim("userID", userID)
                .claim("name", name)
                .expiresIn(Duration.ofHours(2))
                .sign();
    }

    /**
     * Gets userID from JWT-Token.
     * @return user id as long
     */
    public long getUserIdFromJwtToken() {
        if (jsonWebToken != null) {
            try {
                JsonNumber userID = jsonWebToken.getClaim("userID");
                return userID.longValueExact();
            } catch (Exception exception) {
                Log.error("Couldn't get userID from JwtToken: ", exception);
            }
        }
        return -1;
    }
}
