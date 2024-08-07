package Plant.PlantProject.common.config.auth;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
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
    public String generateAccessToken(String memberNo) {
        return JWT.create()
                .withSubject(memberNo)
                .withExpiresAt(new Date(System.currentTimeMillis() + 30 * 60 * 1000))
                .sign(algorithm);
    }

    public String generateRefreshToken(String memberNo) {
        return JWT.create()
                .withSubject(memberNo)
                .withExpiresAt(new Date(System.currentTimeMillis() +  14 * 24 * 60 * 60 * 1000))
                .sign(algorithm);
    }
    public String getCurrentMember(String jwt) {

        String username = null;

        try {
            jwt = jwt.replace("Bearer ", "");
            //복호화
            Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(jwt);
            username = decodedJWT.getSubject();

        } catch (Exception e) {
            return username;
        }
        if (username == null || username.isEmpty()) {
            return username;
        }
        return username;
    }
    public boolean isJwtValid(String jwt) {
        boolean returnValue = true;
        String username = null;

        try {
            //복호화
            Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());
            JWTVerifier verifier = JWT.require(algorithm).build();

            DecodedJWT decodedJWT = verifier.verify(jwt);
            username = decodedJWT.getSubject();
            String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//            username = Jwts.parser().setSigningKey(env.getProperty("token.secret"))
//                    .parseClaimsJws(jwt).getBody()
//                    .getSubject();
        } catch (Exception e) {
            returnValue = false;
        }
        if (username == null || username.isEmpty()) {
            returnValue = false;
        }


        return returnValue;
    }
}
