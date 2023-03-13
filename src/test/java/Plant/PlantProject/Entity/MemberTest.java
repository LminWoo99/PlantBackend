package Plant.PlantProject.Entity;

import Plant.PlantProject.repository.MemberRepository;
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
class MemberTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    MemberRepository memberRepository;
//    @Test
//    public void testEntity() throws Exception{
//        //given
//        Member memberA = new Member("memberA");
//        em.persist(memberA);
//        //when
//        TradeBoard tradeBoard = new TradeBoard("제목1", memberA);
//        em.persist(tradeBoard);
//        //then
//        em.flush();
//        em.clear();

        }

}