package Plant.PlantProject.service.user;

import Plant.PlantProject.domain.Entity.Member;
import Plant.PlantProject.domain.Entity.SocialLogin;
import Plant.PlantProject.domain.vo.request.MemberRequestDto;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.domain.vo.response.MemberResponseDto;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService extends DefaultOAuth2UserService implements UserDetailsService{
    private final MemberRepository memberRepository;
    private final TradeBoardRepository tradeBoardRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    /**
     * 유저 전체 조회
     */
    @Transactional(readOnly = true)
    public List<MemberResponseDto> findAll(){
        List<Member> memberList = memberRepository.findAll();

        List<MemberResponseDto> memberResponseDtoList = new ArrayList<>();

        for (Member member : memberList){
            MemberResponseDto memberResponseDto = MemberResponseDto.toDto(member);
            memberResponseDtoList.add(memberResponseDto);
        }
        return memberResponseDtoList;
    }
    /**
     * 유저 기본키 기준으로 단건 조회
     * @param : Long id
     */
    @Transactional(readOnly = true)
    public MemberResponseDto findById(Long id) {
        Member member = memberRepository.findById(id).orElseThrow(ErrorCode::throwMemberNotFound);
        return MemberResponseDto.toDto(member);
    }
    /**
     * 유저 아이디 기준으로 단건 조회
     * @param : String username
     */
    @Transactional(readOnly = true)
    public MemberResponseDto findByUsername(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);
        return MemberResponseDto.toDto(member);
    }
    /**
     * 유저 회원가입
     * 회원가입시 반드시 패스워드는 암호화!
     * @param : MemberRequestDto memberRequestDto
     */
    public Long joinUser(MemberRequestDto memberRequestDto) {
        // 비밀번호 암호화
        Member member = memberRequestDto.toEntity();
        member.encryptPassword(passwordEncoder.encode(member.getPassword()), SocialLogin.NON);

        return memberRepository.save(member).getId();
    }
    /**
     * 유저 회원가입시 유저아이디 중복 확인!
     * 중복되면 Custom 예외 발생
     * @param : String username
     */
    @Transactional(readOnly = true)
    public HttpStatus duplicateMemberUsername(String username){
        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isEmpty()){
            return HttpStatus.OK;
        }
        else{
            throw ErrorCode.throwUserDuplicatedId();
        }

    }
    /**
     * 유저 회원가입시 유저 닉네임 중복 확인!
     * 중복되면 Custom 예외 발생
     * @param : String nickname
     */
    @Transactional(readOnly = true)
    public HttpStatus duplicateMemberNickname(String nickname){
        Member member = memberRepository.findByNickname(nickname);
        if(member == null){
            return HttpStatus.OK;
        }
        else{
            throw ErrorCode.throwUserDuplicatedNickname();
        }

    }
    /**
     * 유저 회원가입시 유저 이메일 중복 확인!
     * 중복되면 Custom 예외 발생
     * @param : String email
     */
    @Transactional(readOnly = true)
    public HttpStatus duplicateMemberEmail(String email){
        Optional<Member> member = memberRepository.findByEmail(email);
        if(member.isEmpty()){
            return HttpStatus.OK;
        }
        else{
            throw ErrorCode.throwUserDuplicatedEmail();
        }

    }
    /**
     * 유저 아이디 찾기
     * 가입된 이메일 통해 아이디를 찾는다
     * @param : String email
     */
    @Transactional(readOnly = true)
    public MemberResponseDto findUsernameByEmail(String email){
        Member member = memberRepository.findByEmail(email).orElseThrow(ErrorCode::throwUnRegisteredEmail);

        return MemberResponseDto.toDto(member);

    }
    /**
     * 유저 비밀번호 리셋 메서드
     * 가입된 이메일 통해 아이디를 찾고
     * 입력된 아이디로 등록된 계정이 존재하지 않으면 예외 처리
     * 있으면 비밀번호 다시 설정
     * @param : MemberRequestDto memberRequestDto
     */
    public void resetPassword(MemberRequestDto memberRequestDto){

        Member member = memberRepository.findByUsername(memberRequestDto.getUsername())
                .orElseThrow(ErrorCode::throwUnRegisteredId);
        // 다시 암호화하여 초기화, 변경감지
        member.encryptPassword(passwordEncoder.encode(member.getPassword()), SocialLogin.NON);
    }

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("userName = " + userName);
        Member user = memberRepository.findByUsername(userName).orElseThrow(ErrorCode::throwMemberNotFound);
        if(user == null) {
            log.error("User not found in the database {}", userName);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", userName);
        }
        //권한 필요없기 때문 리스트 비우기
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();

        return new User(user.getUsername(), user.getPassword(), authorities);
    }
    public Member findByRefreshToken(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);

        return member;
    }

}
