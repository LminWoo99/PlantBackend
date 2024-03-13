package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.Entity.SocialLogin;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.dto.vo.ResponseTradeBoardDto;
import Plant.PlantProject.exception.MemberNotFoundException;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.RoleRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.security.SecurityUtil;
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
import java.util.stream.Collectors;

import static Plant.PlantProject.dto.vo.ResponseTradeBoardDto.convertTradeBoardToDto;

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
        Member user = memberRepository.findByUsername(userName).orElseThrow(MemberNotFoundException::new);
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


    public Member getUserById(Long id) {
        return memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
    }

    public HttpStatus duplicateMember(String username){

        Optional<Member> member = memberRepository.findByUsername(username);
        if(member.isEmpty()){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.BAD_REQUEST;
        }

    }
    public HttpStatus duplicateMemberNickname(String nickname){

        Member member = memberRepository.findByNickname(nickname);
        if(member == null){
            return HttpStatus.OK;
        }
        else{
            return HttpStatus.BAD_REQUEST;
        }

    }


    public Role saveRole(Role role) {
        log.info("Saving new role {} to the db", role.getName());
        return roleRepository.save(role);
    }

    public void grantRoleToUser(String username, String roleName) {
        log.info("Grant new role {} to {}", roleName, username);
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);
        Role role = roleRepository.findByName(roleName);

        member.getRole().add(role);
    }
    public List<ResponseTradeBoardDto> showTradeInfo(Long id){
        List<TradeBoard> tradeBoards = tradeBoardRepository.findTradeBoardByMemberId(id);
        List<ResponseTradeBoardDto> responseTradeBoardDtos = tradeBoards.stream()
                .map(tradeBoard -> convertTradeBoardToDto(tradeBoard)) // TradeDto로 변환
                .collect(Collectors.toList());
        return responseTradeBoardDtos;
    }public List<ResponseTradeBoardDto> showBuyInfo(Long id){
        Member member=memberRepository.findById(id).orElseThrow(MemberNotFoundException::new);
        List<TradeBoard> tradeBoards = tradeBoardRepository.findTradeBoardByBuyer(member.getNickname());
        List<ResponseTradeBoardDto> responseTradeBoardDtos = tradeBoards.stream()
                .map(tradeBoard -> convertTradeBoardToDto(tradeBoard))
                .collect(Collectors.toList());
        return responseTradeBoardDtos;
    }
    public MemberDto findIdByEmail(String email){
        MemberDto memberDto = memberRepository.findByEmail(email).map(member -> new MemberDto(member.getId(), member.getNickname(),
                member.getUserId(), member.getUsername(), member.getPassword(), member.getEmail()
        )).orElseThrow(MemberNotFoundException::new);
        return memberDto;

    }
    public MemberDto findPasswordById(String username){
        MemberDto memberDto = memberRepository.findByUsername(username).map(member -> new MemberDto(member.getId(), member.getNickname(),
                member.getUserId(), member.getUsername(), member.getPassword(), member.getEmail()
        )).orElseThrow(MemberNotFoundException::new);
        return memberDto;

    }
    public Member find(MemberDto memberDto){
        Member member = memberRepository.findById(memberDto.getId()).orElseThrow(MemberNotFoundException::new);
        BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
        member.setPassword(passwordEncoder.encode(member.getPassword()));
        memberRepository.updatePassword(member.getId(), member.getPassword());
        return member;

    }

    public Member findByRefreshToken(String username) {
        Member member = memberRepository.findByUsername(username).orElseThrow(MemberNotFoundException::new);

        return member;
    }
    public MemberDto findById(Long id) {
        MemberDto memberDto = memberRepository.findById(id).map(member -> new MemberDto(member.getId(), member.getNickname(),
                member.getUserId(), member.getUsername(), member.getPassword(), member.getEmail()
        )).orElseThrow(MemberNotFoundException::new);
        return memberDto;
    }
    public MemberDto findByUsername(String username) {
        MemberDto memberDto = memberRepository.findByUsername(username).map(member -> new MemberDto(member.getId(), member.getNickname(),
                member.getUserId(), member.getUsername(), member.getPassword(), member.getEmail()
        )).orElseThrow(MemberNotFoundException::new);
        return memberDto;
    }

}
