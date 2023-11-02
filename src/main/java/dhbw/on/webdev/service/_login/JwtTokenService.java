package dhbw.on.webdev.service._login;

import io.smallrye.jwt.build.Jwt;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import jakarta.json.JsonNumber;
import org.eclipse.microprofile.jwt.JsonWebToken;


@ApplicationScoped
public class JwtTokenService {
    @Inject
    JsonWebToken jsonWebToken;

    public String generateJwtToken(Long userID) {
        return Jwt.issuer("goalie-master-backend")
                .subject("goalie-master-frontend")
                .groups("user")
                .claim("userID", userID)
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }

    /**
     * Gets userID from JWT-Token.
     * @return
     */
    public long getUserIdFromJwtToken() {
        if (jsonWebToken != null) {
            JsonNumber userID = jsonWebToken.getClaim("userID");
            return userID.longValueExact();
        }
        return -1;
    }
}
