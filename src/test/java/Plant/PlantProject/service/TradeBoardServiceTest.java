package Plant.PlantProject.service;

import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.repository.TradeBoardRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import static org.assertj.core.api.Assertions.assertThat;

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
 * 2022-02-28        이민우       페이징 테스트 성공(100개 임시 데이터 생성 후 테스트 페이지 갯수, 페이지사이즈 체크 확인)
 * 2022-02-28        이민우       게시글 상세보기 테스트 성공
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

        //when
        tradeBoardService.saveTradePost(tradeBoardDto);

        //then
        System.out.println("tradeBoard의 제목은 = " + tradeBoardDto.gettTitle());
        System.out.println("tradeBoard의 내용은 = " + tradeBoardDto.gettContent());
        System.out.println("tradeBoard의 id = " + tradeBoardService.findById(1L));
        assertThat(tradeBoardDto.toEntity().getTTitle()).isEqualTo("test 타이틀");
        assertThat(tradeBoardDto.gettContent()).isEqualTo("테스트 내용");
//        tradeBoardRepository.delete(tradeBoardDto);
//
//        System.out.println("tradeBoard의 저장 유무 = " + tradeBoardRepository.findById(tradeBoard.getId()));
        }
@Test
public void 게시글페이징 () throws Exception{
    //given
    for(int i=0; i<100; i++){
        tradeBoardService.saveTradePost(new TradeBoardDto("title"+i, "content"+i));
    }
    //when
    Pageable paging = PageRequest.of(0,10,Sort.Direction.ASC,"tTitle");
    Page<TradeBoardDto> result= tradeBoardService.pageList(paging);
    //then
    System.out.println(result.getSize());
    System.out.println(result.getTotalPages());
    }
//    @Test
//    public void 게시글상세보기() throws Exception{
//        //given
//        for(int i=0; i<100; i++){
//            tradeBoardService.saveTradePost(new TradeBoardDto("title"+i, "content"+i));
//        }
//        //when
//        TradeBoardDto tradeBoardDto1=tradeBoardService.findById(1L);
//
//        //then
//        assertThat(tradeBoardDto1.getTitle()).isEqualTo("title0");
//        System.out.println("tradeBoardDto1 = " + tradeBoardDto1.getTitle());
//
//        }
}