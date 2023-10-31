package dhbw.on.webdev.service._login;

import io.smallrye.jwt.build.Jwt;
import jakarta.ejb.Singleton;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class JwtTokenGenerator {
    public String generateJwtToken() {
        return Jwt.issuer("goalie-master-backend")
                .subject("goalie-master-frontend")
                .groups("user")
                .expiresAt(System.currentTimeMillis() + 3600)
                .sign();
    }
}
