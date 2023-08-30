package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.dto.RoleDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.RoleRepository;
import Plant.PlantProject.service.MemberService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.fasterxml.jackson.databind.ObjectMapper;


import lombok.RequiredArgsConstructor;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URI;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;


/**
 * packageName    : Plant/PlantProject/controller
 * fileName       : MemberController
 * author         : 이민우
 * date           : 2023-02-24
 * description    : 일반 회원가입 , 로그인, 회원 정보 조회
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-05        이민우       MemberController 생성
 * 2022-03-24        이민우       로그인 뷰 엔드포인트 "/loginForm", 회원가입 뷰 엔드포인트 "/joinForm"
 * 2022-03-24        이민우       로그인을 할때는 프론트단에서 데이터 요청 후 일치하면 로그인 완료, 회원가입시 백단에 데이터 받고 프론트에 넘겨줌
 * 2022-05-03        이민우       자체로그인, 구글로그인은 "loginForm",
 */
@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    BCryptPasswordEncoder bCryptPasswordEncoder;
    @GetMapping("/tradeInfo/{id}")
    public ResponseEntity<List<TradeDto>> showTradeInfo(@PathVariable Long id){
        List<TradeDto> tradeDtos = memberService.showTradeInfo(id);
        return ResponseEntity.ok().body(tradeDtos);
    }
    @GetMapping("/users")
    public ResponseEntity<List<MemberDto>> findAll() {
        return ResponseEntity.ok().body(memberService.findAll());
    }

    @PostMapping("/user/save")
    public ResponseEntity<Member> saveUser(@RequestBody MemberDto memberDto) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/user/save").toUriString());

        Member member = memberDto.toEntity();

        member.getRole().add(roleRepository.findByName("ROLE_USER"));
        return ResponseEntity.created(uri).body(memberService.joinUser(member));
    }

    @PostMapping("/role/save")
    public ResponseEntity<Role> saveRole(@RequestBody Role role) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/role/save").toUriString());
        return ResponseEntity.created(uri).body(memberService.saveRole(role));
    }

    @PostMapping("/role/grant-to-user")
    public ResponseEntity<?> grantRole(@RequestBody RoleDto roleDto) {
        memberService.grantRoleToUser(roleDto.getUserName(), roleDto.getRoleName());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/token/refresh")
    public void refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try{
                String refreshToken = authorizationHeader.substring("Bearer ".length());
                Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());

                JWTVerifier verifier = JWT.require(algorithm).build();

                DecodedJWT decodedJWT = verifier.verify(refreshToken);

                String username = decodedJWT.getSubject();
                Member member = memberService.getUser(username);

                String accessToken = JWT.create()
                        .withSubject(member.getUsername())
                        .withExpiresAt(new Date(System.currentTimeMillis() + 10 * 60 * 1000))
                        .withIssuer(request.getRequestURI().toString())
                        .withClaim("roles", member.getRole().stream().map(Role::getName).collect(Collectors.toList()))
                        .sign(algorithm);


                Map<String, String> tokens = new HashMap<>();
                tokens.put("access_token", accessToken);
                tokens.put("refresh_token", refreshToken);

                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), tokens);
            }catch (Exception e) {
                response.setHeader("error", e.getMessage());
                response.setStatus(FORBIDDEN.value());

                Map<String, String> error = new HashMap<>();
                error.put("error_message", e.getMessage());
                response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                new ObjectMapper().writeValue(response.getOutputStream(), error);
            }
        } else {
            throw new RuntimeException("Refresh token is missing");
        }
    }
}


