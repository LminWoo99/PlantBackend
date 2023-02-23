package Plant.PlantProject.repository;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.dto.MemberDto;
import Plant.PlantProject.service.MemberService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
/**
 * packageName    : Plant/PlantProject/repository
 * fileName       : MemberRepositoryTest
 * author         : 이민우
 * date           : 2023-02-23
 * description    : 멤버 저장 후 이름으로 조회 테스트 성공
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-02-23        이민우       최초 생성
 */
@SpringBootTest
@Transactional
@Rollback(false)
class MemberRepositoryTest {
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    MemberService memberService;
  ;
    @Test
    public void 멤버이름으로조회() throws Exception{
        //given
        MemberDto memberDto = new MemberDto("minu", "민우");
        Member member1 = memberDto.toEntity();
        memberRepository.save(member1);
        //when

        //then
        assertThat(memberRepository.findByName("민우")).isEqualTo(member1);
        }

}