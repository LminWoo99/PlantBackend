package Plant.PlantProject.config;

import Plant.PlantProject.Entity.Role;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.stream.Collectors;

@Component
public class JwtTokenUtil {

    @Value("${JWT.SECRET}")
    private String SECRET_KEY;

    // 토큰 유효 시간 5시간
    Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());

    public String generateAccessToken(UserDetails user) {
        return JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                .withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList()))
                .sign(algorithm);
    }

    public String generateRefreshToken(String email) {
        return JWT.create()
                .withSubject(email)  // using email as the subject
                .withExpiresAt(new Date(System.currentTimeMillis() + 300 * 60 * 1000))
                .sign(algorithm);
    }

}
