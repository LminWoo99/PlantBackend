package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.Entity.SocialLogin;
import Plant.PlantProject.auth.PrincipalDetails;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static Plant.PlantProject.Entity.SocialLogin.NON;

@Service
@RequiredArgsConstructor
public class MemberService extends DefaultOAuth2UserService implements UserDetailsService{
    private final MemberRepository memberRepository;
    BCryptPasswordEncoder passwordEncoder;
//    @Override
//    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
//
//        OAuth2User oAuth2User = super.loadUser(userRequest);
//
//        String provider = userRequest.getClientRegistration().getRegistrationId();    //google
//        String providerId = oAuth2User.getAttribute("sub");
//        String username = provider+"_"+providerId;  			// 사용자가 입력한 적은 없지만 만들어준다
//
//        String uuid = UUID.randomUUID().toString().substring(0, 6);
//        String password = passwordEncoder.encode("패스워드"+uuid);  // 사용자가 입력한 적은 없지만 만들어준다
//
//        String email = oAuth2User.getAttribute("email");
//        Role role = Role.ROLE_USER;
//
//        Member byUsername = memberRepository.findByName(username);
//
//        //DB에 없는 사용자라면 회원가입처리
//        if(byUsername == null){
//            byUsername = Member.oauth2Register()
//                    .name(username).password(password).email(email).role(role)
//                    .provider(provider).providerId(providerId)
//                    .build();
//            memberRepository.save(byUsername);
//        }
//
//        return new PrincipalDetails(byUsername, oAuth2User.getAttributes());
//    }


    public void save(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity());
    }
    public List<Member> findAll(){

        return memberRepository.findAll();
    }
    public MemberDto findByUsername(String name){
        Long id = memberRepository.findByName(name).getId();
        return memberRepository.findById(id).map(member -> new MemberDto( member.getName(),
                member.getUserId(),member.getPassword(), member.getEmail(),member.getNickname())).get();
    }

    public MemberDto findByUserId(String userId){
        Long id = memberRepository.findByUserId(userId).getId();
        return memberRepository.findById(id).map(member -> new MemberDto( member.getName(),
                member.getUserId(),member.getPassword(), member.getEmail(),member.getNickname())).get();
    }
    @Transactional
    public Long joinUser(MemberDto memberDto) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        memberDto.setPassword(passwordEncoder.encode(memberDto.getPassword()));
        memberDto.setSocialLogin(NON);
        return memberRepository.save(memberDto.toEntity()).getId();
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return null;
    }
}
