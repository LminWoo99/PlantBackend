package Plant.PlantProject.controller.user;

import Plant.PlantProject.common.config.auth.JwtTokenUtil;
import Plant.PlantProject.vo.request.MemberRequestDto;
import Plant.PlantProject.vo.response.MemberResponseDto;
import Plant.PlantProject.service.user.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.net.URI;
import java.util.List;
import org.springframework.http.HttpStatus;
/*
* 리팩토링 매우 필요!
*
*/

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class MemberController {
    private final MemberService memberService;
    private final JwtTokenUtil jwtTokenUtil;

    @GetMapping("/users")
    @Operation(summary = "유저 전체 조회", description = "유저 전체 조회를 할 수 있는 API")
    public ResponseEntity<List<MemberResponseDto>> findAll() {
        return ResponseEntity.ok().body(memberService.findAll());
    }

    @PostMapping("/user/save")
    @Operation(summary = "회원 가입", description = "회원가입을 할 수 있는 API")
    public ResponseEntity<Long> saveUser(@RequestBody MemberRequestDto memberRequestDto) {
        URI uri = URI.create(
                ServletUriComponentsBuilder
                        .fromCurrentContextPath()
                        .path("/api/user/save").toUriString());

        return ResponseEntity.created(uri).body(memberService.joinUser(memberRequestDto));
    }
    @GetMapping("/user/username-chk")
    @Operation(summary = "회원 가입시 중복 검사", description = "회원 가입시 유저 아이디를 기준을 가입한 이름이 있는지 확인할 수 있는 API")
    public ResponseEntity<?> duplicateMember(@RequestParam String username){
        HttpStatus httpStatus = memberService.duplicateMemberUsername(username);

        return new ResponseEntity<>(httpStatus);
    }
    @GetMapping("/user/nickname-chk")
    @Operation(summary = "회원 가입시 중복 검사", description = "회원 가입시 닉네임 이름을 기준을 중복검사를 할 수 있는 API")
    public ResponseEntity<?> duplicateMemberNickname(@RequestParam String nickname){
        HttpStatus httpStatus = memberService.duplicateMemberNickname(nickname);

        return new ResponseEntity<>(httpStatus);
    }
    @GetMapping("/user/email-chk")
    @Operation(summary = "회원 가입시 중복 검사", description = "회원 가입시 이메일 이름을 기준을 중복검사를 할 수 있는 API")
    public ResponseEntity<?> duplicateMemberEmail(@RequestParam String email){
        HttpStatus httpStatus = memberService.duplicateMemberEmail(email);

        return new ResponseEntity<>(httpStatus);
    }
    @GetMapping("/user/id")
    @Operation(summary = "아이디 찾기", description = "아이디 분실시 email 기준으로 아이디 찾기 할 수 있는 API")
    public ResponseEntity<MemberResponseDto> findIdByEmail(@RequestParam String email){
        MemberResponseDto memberResponseDto = memberService.findUsernameByEmail(email);
        return ResponseEntity.ok().body(memberResponseDto);
    }
    @PostMapping("/user/pwd")
    @Operation(summary = "비밀번호 재설정", description = "유저 패스워드 분실시 아이디 기준으로 계정 조회 후 재설정 할 수 있는 API")
    public void resetPassword(@RequestBody MemberRequestDto memberRequestDto){
        memberService.resetPassword(memberRequestDto);
    }

    @GetMapping("/user")
    @Operation(summary = "회원 찾기- 회원 아이디 기준", description = "회원 아이디 기준 회원 찾기 할 수 있는 API")
    public ResponseEntity<MemberResponseDto> findByUsername(@RequestParam String username) {
        MemberResponseDto memberResponseDto = memberService.findByUsername(username);
        return ResponseEntity.ok().body(memberResponseDto);
    }
    @GetMapping("/user/pk")
    @Operation(summary = "회원 찾기- 회원 pk 기준", description = "회원 기본키 기준 회원 찾기 할 수 있는 API")
    public ResponseEntity<MemberResponseDto> findById(@RequestParam Long id) {
        MemberResponseDto memberResponseDto = memberService.findById(id);
        return ResponseEntity.ok().body(memberResponseDto);
    }

    @GetMapping("/user/token")
    public ResponseEntity<MemberResponseDto> getJoinMember(@RequestHeader("Authorization") String jwtToken) {
        String username = jwtTokenUtil.getCurrentMember(jwtToken);
        MemberResponseDto memberResponseDto = memberService.findByUsername(username);
        return ResponseEntity.ok(memberResponseDto);

    }
}


