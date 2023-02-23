package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.repository.MemberRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    public List<Member> findAll(){

        return memberRepository.findAll();
    }
    public Member findByUsername(String name){
        return memberRepository.findByName(name);
    }
    }
