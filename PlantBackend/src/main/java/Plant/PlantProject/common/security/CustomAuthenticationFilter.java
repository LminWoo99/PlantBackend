package Plant.PlantProject.common.security;

import Plant.PlantProject.domain.Entity.Member;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.repository.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;

@Slf4j
@AllArgsConstructor
public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final AuthenticationManager authenticationManager;
    private HashMap<String, String> jsonRequest;

    private  MemberRepository memberRepository;

    public CustomAuthenticationFilter(AuthenticationManager authenticationManager, MemberRepository memberRepository) {
        this.authenticationManager = authenticationManager;
        this.memberRepository=memberRepository;
    }

    @Override
    protected String obtainPassword(HttpServletRequest request) {
        String password  = super.getPasswordParameter();
        if(request.getHeader("Content-Type").equals(MediaType.APPLICATION_JSON_VALUE)) {
            return jsonRequest.get(password);
        }
        return request.getParameter(password);
    }

    @Override
    protected String obtainUsername(HttpServletRequest request) {
        String username  = super.getUsernameParameter();
        if(request.getHeader("Content-Type").equals(MediaType.APPLICATION_JSON_VALUE)) {
            return jsonRequest.get(username);
        }
        return request.getParameter(username);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        if(request.getHeader("Content-Type").equals(MediaType.APPLICATION_JSON_VALUE)) {
            log.info("Json Login Attempt");

            ObjectMapper mapper = new ObjectMapper();
            try {
                this.jsonRequest =
                        mapper.readValue(request.getReader().lines().collect(Collectors.joining()),
                                new TypeReference<HashMap<String, String>>() {
                                });
            } catch (IOException e) {
                e.printStackTrace();
                throw new AuthenticationServiceException("Request Content-Type (application/json) Parsing error");
            }
        }

        String username = obtainUsername(request);
        String password = obtainPassword(request);

        log.info("{} attempt to login with {}", username, password);
        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
        return authenticationManager.authenticate(authenticationToken);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
        User user = (User) authentication.getPrincipal();
        Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());

        String username = user.getUsername();

        log.info("success:" + username);

        String accessToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);

        String refreshToken = JWT.create()
                .withSubject(user.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() +  14 * 24 * 60 * 60 * 1000))
                .withIssuer(request.getRequestURI().toString())
                .sign(algorithm);

        Map<String, Object> tokens = new HashMap<>();
        Member byUsername = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);
        byUsername.setRefreshToken(refreshToken);
        memberRepository.save(byUsername);

        String id = String.valueOf(byUsername.getId());
        String nickname = byUsername.getNickname();
        tokens.put("access_token", accessToken);
        tokens.put("username", username);
        tokens.put("refresh_token", refreshToken);
        tokens.put("nickname", nickname);
        tokens.put("id", id);
        tokens.put("email", byUsername.getEmail());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }
}
