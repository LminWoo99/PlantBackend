package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Status;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.exception.UserNotFoundException;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.dto.TradeBoardRequestDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/*
 작성자 : 이민우
 작성 일자: 02.19
 내용 : 거래 게시글 서비스 구현
 특이 사항: 없음
*/
@Service
@AllArgsConstructor
public class TradeBoardService {
    private final TradeBoardRepository tradeBoardRepository;
    private final MemberRepository memberRepository;
    // 트랜잭션은 readOnly true 로 설정하면 데이터베이스의 상태를 변경하지 않는 읽기 전용 메서드에서 성능 향상을 기대할 수 있음
// 트랜잭션 설정을 하면 롤백 가능, 즉 DB에서 무언가 잘못되었을 경우 이전 상태로 되돌릴 수 있음
    @Transactional
    public TradeBoardDto saveTradePost(TradeBoardDto tradeBoardDto){

        TradeBoard savedEntity=tradeBoardRepository.save(tradeBoardDto.toEntity());
        System.out.println("service 통과");
        tradeBoardDto.setId(savedEntity.getId());
        return tradeBoardDto;
    }
    @Transactional
    public TradeDto saveTradePost(TradeBoardRequestDto tradeBoardDto){
        TradeBoard tradeBoard=tradeBoardRepository.save(
                TradeBoard.createTradeBoard(memberRepository.findById(tradeBoardDto.getMemberId()).orElseThrow(UserNotFoundException::new),
                        tradeBoardDto.getTitle(),
                        tradeBoardDto.getContent(),
                        tradeBoardDto.getCreateBy(),
                        tradeBoardDto.getView(),
                        tradeBoardDto.getPrice()
                        )
        );

        return TradeDto.convertTradeBoardToDto(tradeBoard);
    }
    @Transactional
    public TradeBoardDto updateTradePost(TradeBoardDto tradeBoardDto) {
        Optional<TradeBoard> optionalTradeBoard = tradeBoardRepository.findById(tradeBoardDto.getId());


        if (optionalTradeBoard.isPresent()) {
            TradeBoard tradeBoard = optionalTradeBoard.get();
            tradeBoard.setTitle(tradeBoardDto.getTitle());
            tradeBoard.setContent(tradeBoardDto.getContent());
            tradeBoard.setStatus(tradeBoardDto.getStatus());
            tradeBoard.setPrice(tradeBoardDto.getPrice());
            TradeBoard savedEntity = tradeBoardRepository.save(tradeBoard);

            // 업데이트된 정보를 TradeBoardDto로 변환하여 반환
            TradeBoardDto updatedTradeBoardDto = new TradeBoardDto();
            updatedTradeBoardDto.setId(savedEntity.getId());
            updatedTradeBoardDto.setTitle(savedEntity.getTitle());
            updatedTradeBoardDto.setContent(savedEntity.getContent());
            // 이 외에 필요한 필드들도 updatedTradeBoardDto에 추가

            return updatedTradeBoardDto;
        } else {
            // 해당 id에 해당하는 게시글이 없는 경우 처리
            throw new EntityNotFoundException("TradeBoard not found with id: " + tradeBoardDto.getId());
        }
    }
    @Transactional
    public TradeBoardDto updateStatus(TradeBoardDto tradeBoardDto) {
        Optional<TradeBoard> optionalTradeBoard = tradeBoardRepository.findById(tradeBoardDto.getId());

        if (optionalTradeBoard.isPresent()) {
            TradeBoard tradeBoard = optionalTradeBoard.get();
            tradeBoard.setStatus(tradeBoardDto.getStatus());
            TradeBoard savedEntity = tradeBoardRepository.save(tradeBoard);

            // 업데이트된 정보를 TradeBoardDto로 변환하여 반환
            TradeBoardDto updatedTradeBoardDto = new TradeBoardDto();
            updatedTradeBoardDto.setId(savedEntity.getId());
            updatedTradeBoardDto.setTitle(savedEntity.getTitle());
            updatedTradeBoardDto.setContent(savedEntity.getContent());
            updatedTradeBoardDto.setStatus(savedEntity.getStatus());
            // 이 외에 필요한 필드들도 updatedTradeBoardDto에 추가

            return updatedTradeBoardDto;
        } else {
            // 해당 id에 해당하는 게시글이 없는 경우 처리
            throw new EntityNotFoundException("TradeBoard not found with id: " + tradeBoardDto.getId());
        }
    }
    @Transactional
    public List<TradeBoardDto> findAll(){
        List<TradeBoard> tradeBoardList =tradeBoardRepository.findAll();
        List<TradeBoardDto> tradeBoardDtoList = new ArrayList<>();
        for (TradeBoard tradeBoard : tradeBoardList) {
            TradeBoardDto tradeBoardDto = TradeBoardDto.builder().
            id(tradeBoard.getId())
                    .createBy(tradeBoard.getCreateBy())
                    .member(tradeBoard.getMember())
                    .title(tradeBoard.getTitle())
                    .content(tradeBoard.getContent())
                    .status(tradeBoard.getStatus())
                    .build();


            tradeBoardDtoList.add(tradeBoardDto);
        }
        return tradeBoardDtoList;
    }
    @Transactional
    public int updateView(Long id) {
        TradeBoardDto tradeBoardDto = new TradeBoardDto();
        int view = tradeBoardRepository.updateView(id);
        return view;

    }
    @Transactional
    public Page<TradeDto> pageList(String search, Pageable pageable) {
        Page<TradeBoard> tradeBoards;

//        if (search != null && !search.trim().isEmpty()) {
            tradeBoards = tradeBoardRepository.findByTitleContainingOrContentContaining(search, search, pageable);
//        } else {
//            tradeBoards = tradeBoardRepository.findAll(pageable);
//        }
        return tradeBoards.map(tradeBoard -> TradeDto.convertTradeBoardToDto(tradeBoard));
//
//        return tradeBoards.map(tradeBoard -> new TradeBoardDto(tradeBoard.getId(), tradeBoard.getCreateBy(), tradeBoard.getMember(), tradeBoard.getTitle(),
//                tradeBoard.getContent(), tradeBoard.getStatus(), tradeBoard.getCreatedAt(), tradeBoard.getUpdatedAt(), tradeBoard.getView()));
    }
    public TradeBoardDto findById(Long id){
        return tradeBoardRepository.findById(id).map(tradeBoard -> new TradeBoardDto(tradeBoard.getId(), tradeBoard.getCreateBy(),tradeBoard.getMember(),tradeBoard.getTitle(),
                tradeBoard.getContent(),tradeBoard.getStatus(), tradeBoard.getCreatedAt(),tradeBoard.getUpdatedAt(), tradeBoard.getView())).get();
    }
    public TradeDto findByIdx(Long id){
        TradeBoard tradeBoard = tradeBoardRepository.findById(id).orElseThrow(UserNotFoundException::new);
        TradeDto tradeDto = TradeDto.convertTradeBoardToDto(tradeBoard);
        return tradeDto;
    }


    public void deletePost(TradeBoardDto tradeBoardDto) {
        tradeBoardRepository.delete(tradeBoardDto.toEntity());
    }

    public TradeBoard findTradeBoardById(Long tradeBoardId) {
        return tradeBoardRepository.findTradeBoardById(tradeBoardId);
    }
}
