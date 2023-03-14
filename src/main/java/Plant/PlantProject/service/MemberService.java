package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.Role;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class MemberService implements UserDetailsService {
    private final MemberRepository memberRepository;
    public void save(MemberDto memberDto) {
        memberRepository.save(memberDto.toEntity()
        );
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

        return memberRepository.save(memberDto.toEntity()).getId();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Member findName = memberRepository.findByName(username);
        Member member = findName;
        List<GrantedAuthority> authorities = new ArrayList<>();

        if(("admin").equals(username)){
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }else {
            authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        }
        return new User(member.getName(), member.getPassword() , authorities);
    }
    }
