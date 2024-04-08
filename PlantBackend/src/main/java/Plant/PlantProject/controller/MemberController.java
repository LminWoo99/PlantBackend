package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.config.JwtTokenUtil;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.dto.RoleDto;
import Plant.PlantProject.dto.vo.ResponseTradeBoardDto;
import Plant.PlantProject.repository.RoleRepository;
import Plant.PlantProject.service.MemberService;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;


import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;


import org.apache.catalina.security.SecurityUtil;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
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
import org.springframework.http.HttpStatus;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;

/*
* 리팩토링 매우 필요!
*
*/

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final RoleRepository roleRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/tradeInfo/{id}")
    @Operation(summary = "유저 기능 -거래 정보 조회", description = "유저 기능중 개인이 거래한 내역을 확인 할 수 있는 API")
    public ResponseEntity<List<ResponseTradeBoardDto>> showTradeInfo(@PathVariable Long id){
        List<ResponseTradeBoardDto> responseTradeBoardDtos = memberService.showTradeInfo(id);
        return ResponseEntity.ok().body(responseTradeBoardDtos);
    }
    @GetMapping("/buyInfo/{id}")
    @Operation(summary = "유저 기능 -구매 정보 조회", description = "유저 기능중 개인이 구매한 내역을 확인 할 수 있는 API")
    public ResponseEntity<List<ResponseTradeBoardDto>> showBuyInfo(@PathVariable Long id){
        List<ResponseTradeBoardDto> responseTradeBoardDtos = memberService.showBuyInfo(id);
        return ResponseEntity.ok().body(responseTradeBoardDtos);
    }
    @GetMapping("/users")
    @Operation(summary = "유저 전체 조회", description = "유저 전체 조회를 할 수 있는 API")
    public ResponseEntity<List<MemberDto>> findAll() {
        return ResponseEntity.ok().body(memberService.findAll());
    }

    @PostMapping("/user/save")
    @Operation(summary = "회원 가입", description = "회원가입을 할 수 있는 API")
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
    public void refreshToken(HttpServletRequest request, HttpServletResponse response, @RequestParam String username) throws IOException {
        String authorizationHeader = request.getHeader(AUTHORIZATION);

        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            try {
                String refreshToken = authorizationHeader.substring("Bearer ".length());

                // 데이터베이스에서 리프레시 토큰으로 멤버 조회
                Member member = memberService.findByRefreshToken(username);

                // 멤버가 존재하고, 리프레시 토큰이 일치하면 새로운 액세스 토큰 발급
                if (member != null && member.getRefreshToken().equals(refreshToken)) {
                    Algorithm algorithm = Algorithm.HMAC256("secretKey".getBytes());

                    String accessToken = JWT.create()
                            .withSubject(member.getUsername())
                            .withExpiresAt(new Date(System.currentTimeMillis() + 20 * 60 * 1000))
                            .withIssuer(request.getRequestURI().toString())
                            .withClaim("roles", member.getRole().stream().map(Role::getName).collect(Collectors.toList()))
                            .sign(algorithm);

                    // 새로운 액세스 토큰 반환
                    Map<String, String> tokens = new HashMap<>();
                    tokens.put("access_token", accessToken);
                    tokens.put("refresh_token", refreshToken);

                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                    new ObjectMapper().writeValue(response.getOutputStream(), tokens);
                } else {
                    throw new RuntimeException("Invalid refresh token");
                }
            } catch (Exception e) {
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

    @GetMapping("/duplicate")
    @Operation(summary = "회원 가입시 중복 검사", description = "회원 가입시 유저 이름을 기준을 가입한 이름이 있는지 확인할 수 있는 API")
    public ResponseEntity<?> duplicateMember(@RequestParam String username){
        HttpStatus httpStatus = memberService.duplicateMember(username);

        return new ResponseEntity<>(httpStatus);
    }
    @GetMapping("/duplicate/nickname")
    @Operation(summary = "회원 가입시 중복 검사", description = "회원 가입시 닉네임 이름을 기준을 중복검사를 할 수 있는 API")
    public ResponseEntity<?> duplicateMemberNickname(@RequestParam String nickname){
        HttpStatus httpStatus = memberService.duplicateMemberNickname(nickname);

        return new ResponseEntity<>(httpStatus);
    }
    @GetMapping("findEmail")
    @Operation(summary = "아이디 찾기", description = "아이디 분실시 email 기준으로 아이디 찾기 할 수 있는 API")
    public ResponseEntity<MemberDto> findIdByEmail(@RequestParam String email){
        MemberDto memberDto = memberService.findIdByEmail(email);
        return ResponseEntity.ok().body(memberDto);
    }
    @GetMapping("findPassword")
    @Operation(summary = "비밀번호 찾기", description = "유저 패스워드 분실시 id 기준으로 비밀번호 찾기 할 수 있는 API")
    public ResponseEntity<MemberDto> findPasswordById(@RequestParam String userId){
        MemberDto passwordById = memberService.findPasswordById(userId);
        return ResponseEntity.ok().body(passwordById);
    }
    @PostMapping("findPassword")
    public ResponseEntity<Member> setPassword(@RequestBody MemberDto memberDto){
        Member member=memberService.find(memberDto);
        return ResponseEntity.ok().body(member);
    }

    @GetMapping("/findUsername")
    @Operation(summary = "회원 찾기- 회원 이름 기준", description = "회원 이름 기준 회원 찾기 할 수 있는 API")
    public ResponseEntity<MemberDto> findByUsername(@RequestParam String username) {
        MemberDto memberDto = memberService.findByUsername(username);
        return ResponseEntity.ok().body(memberDto);
    }
    @GetMapping("/findId")
    @Operation(summary = "회원 찾기- 회원 id 기준", description = "회원 id 기준 회원 찾기 할 수 있는 API")
    public ResponseEntity<MemberDto> findById(@RequestParam Long id) {
        MemberDto memberDto = memberService.findById(id);
        return ResponseEntity.ok().body(memberDto);
    }

    @GetMapping("/joinMember")
    public ResponseEntity<MemberDto> getJoinMember(@RequestHeader("Authorization") String jwtToken) {
        String username = jwtTokenUtil.getCurrentMember(jwtToken);
        MemberDto memberDto = memberService.findByUsername(username);
        return ResponseEntity.ok(memberDto);

    }
}


