package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.repository.TradeBoardRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.core.AutoConfigureCache;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
/**
 * packageName    : Plant/PlantProject/service
 * fileName       : TradeBoardServiceTest
 * author         : 이민우
 * date           : 2023-02-22
 * description    : 거래 게시글 저장 테스트 성공
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-02-22        이민우       최초 생성
 */
@SpringBootTest
@Transactional
@Rollback(false)
class TradeBoardServiceTest {
    @PersistenceContext
    EntityManager em;
    @Autowired
    TradeBoardRepository tradeBoardRepository;
    @Autowired
    TradeBoardService tradeBoardService;
    @Test
    public void savePost() throws Exception{
        //given

        TradeBoardDto tradeBoardDto = new TradeBoardDto("test 타이틀", "테스트 내용");
        TradeBoard tradeBoard = tradeBoardDto.toEntity();

        //when
        tradeBoardRepository.save(tradeBoard);

        //then
        System.out.println("tradeBoard의 제목은 = " + tradeBoard.getTTitle());
        System.out.println("tradeBoard의 내용은 = " + tradeBoard.getTContent());
        System.out.println("tradeBoard의 id = " + tradeBoardService.findById(5L));
        assertThat(tradeBoard.getTTitle()).isEqualTo("test 타이틀");
        assertThat(tradeBoard.getTContent()).isEqualTo("테스트 내용");
        tradeBoardRepository.delete(tradeBoard);

        System.out.println("tradeBoard의 저장 유무 = " + tradeBoardRepository.findById(tradeBoard.getId()));
        }

}