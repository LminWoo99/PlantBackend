package Plant.PlantProject.service;

import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : member, tradeboard 연관관계 테스트
*/
@SpringBootTest
@Transactional
@Rollback(false)
class MemberServiceTest {

    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
    @Test
    public void testEntity() throws Exception{
        //given
        MemberDto memberDto = new MemberDto("mw310", "이민우");
        memberService.save(memberDto);
        System.out.println("memberDto = " + memberService.findByUsername("이민우"));
        System.out.println("memberDto = " + memberService.findByUserId("mw310"));
        Assertions.assertThat((memberService.findByUsername(memberDto.getName())).equals(memberDto));
        Assertions.assertThat((memberService.findByUserId(memberDto.getUserId())).equals(memberDto));

    }

}