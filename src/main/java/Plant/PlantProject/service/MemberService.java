package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.Entity.SocialLogin;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.RoleRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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
import java.util.stream.Collectors;

import static Plant.PlantProject.dto.TradeDto.convertTradeBoardToDto;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class MemberService extends DefaultOAuth2UserService implements UserDetailsService{
    private final MemberRepository memberRepository;
    private final RoleRepository roleRepository;
    private final TradeBoardRepository tradeBoardRepository;

    public List<MemberDto> findAll(){
        List<Member> memberList = memberRepository.findAll();
        List<MemberDto> memberDtoList = new ArrayList<>();
        for (Member member : memberList){
            MemberDto memberDto = MemberDto.builder()
                    .id(member.getId())
                    .nickname(member.getNickname())
                    .userId(member.getUserId())
                    .username(member.getUsername())
                    .password(member.getPassword())
                    .email(member.getEmail())
                    .build();
            memberDtoList.add(memberDto);
        }
        return memberDtoList;
    }
    public MemberDto findByUserName(String username){
        Long id = memberRepository.findByUsername(username).getId();
        return memberRepository.findById(id).map(member -> new MemberDto( member.getId(),member.getUsername(),
                member.getUserId(),member.getPassword(), member.getEmail(),member.getNickname())).get();
    }

    public MemberDto findByUserId(String userId){
        Long id = memberRepository.findByUserId(userId).getId();
        return memberRepository.findById(id).map(member -> new MemberDto(member.getId(), member.getUsername(),
                member.getUserId(),member.getPassword(), member.getEmail(),member.getNickname())).get();
    }

    public Member joinUser(Member member) {
        // 비밀번호 암호화
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        member.setSocialLogin(SocialLogin.NON);
        return memberRepository.save(member);
    }


    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        System.out.println("userName = " + userName);
        Member user = memberRepository.findByUsername(userName);
        if(user == null) {
            log.error("User not found in the database {}", userName);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User found in the database: {}", userName);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRole().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });
        return new User(user.getUsername(), user.getPassword(), authorities);
    }

    public Member getUser(String username) {
        return memberRepository.findByUsername(username);
    }





    public Role saveRole(Role role) {
        log.info("Saving new role {} to the db", role.getName());
        return roleRepository.save(role);
    }

    public void grantRoleToUser(String username, String roleName) {
        log.info("Grant new role {} to {}", roleName, username);
        Member member = memberRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);

        member.getRole().add(role);
    }
    public List<TradeDto> showTradeInfo(Long id){
        log.info("showTradeInfo출력");
        List<TradeBoard> tradeBoards = tradeBoardRepository.findTradeBoardByMemberId(id);
        List<TradeDto> tradeDtos = tradeBoards.stream()
                .map(tradeBoard -> convertTradeBoardToDto(tradeBoard)) // TradeDto로 변환
                .collect(Collectors.toList());
        return tradeDtos;
    }
}
