package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Member;
import Plant.PlantProject.domain.Entity.Status;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.domain.vo.request.TradeBoardRequestDto;
import Plant.PlantProject.domain.vo.response.TradeBoardResponseDto;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.common.messagequeue.KafkaProducer;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Plant.PlantProject.domain.vo.response.TradeBoardResponseDto.convertTradeBoardToDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeBoardService {
    private final TradeBoardRepository tradeBoardRepository;
    private final MemberRepository memberRepository;
    private final GoodsRepository goodsRepository;
    private final KafkaProducer kafkaProducer;

    /**
     * 거래게시글 저장
     * username은 Security에서 Principal을 가져와서 전달
     * @param : TradeBoardRequestDto tradeBoardRequestDto, String username
     */
    @Transactional
    public Long saveTradePost(TradeBoardRequestDto tradeBoardRequestDto, String username){
        Member member = memberRepository.findByUsername(username).orElseThrow(ErrorCode::throwMemberNotFound);

        TradeBoard tradeBoard=tradeBoardRepository.save(
                TradeBoard.createTradeBoard(member,
                        tradeBoardRequestDto.getTitle(),
                        tradeBoardRequestDto.getContent(),
                        member.getNickname(),
                        tradeBoardRequestDto.getView(),
                        tradeBoardRequestDto.getPrice(),
                        tradeBoardRequestDto.getGoodCount(),
                        tradeBoardRequestDto.getBuyer()
                )
        );

        return tradeBoardRepository.save(tradeBoard).getId();
    }
    /**
     * 거래게시글 수정
     * 수정 사항 옵셥 : 제목, 내용, 가격
     * 쿼리 대신 변경 감지
     * @param : Long id, TradeBoardRequestDto tradeBoardRequestDto
     */
    @Transactional
    public Long updateTradePost(Long id, TradeBoardRequestDto tradeBoardRequestDto) {
        Optional<TradeBoard> optionalTradeBoard = tradeBoardRepository.findById(id);


        if (optionalTradeBoard.isPresent()) {
            TradeBoard tradeBoard = optionalTradeBoard.get();
            //변경감지
            tradeBoard.updatePost(tradeBoardRequestDto.getTitle(), tradeBoardRequestDto.getContent(), tradeBoard.getPrice());
            return tradeBoard.getId();
        } else {
            // 해당 id에 해당하는 게시글이 없는 경우 처리
            throw ErrorCode.throwTradeBoardNotFound();
        }
    }
    /**
     * 거래 완료 상태 변경 메서드
     * 수정 사항 : Status status
     * 쿼리 대신 변경 감지
     * @param : Long id
     */
    @Transactional
    public Long updateStatus(Long id) {
        Optional<TradeBoard> optionalTradeBoard = tradeBoardRepository.findById(id);

        if (optionalTradeBoard.isPresent()) {
            TradeBoard tradeBoard = optionalTradeBoard.get();
            tradeBoard.updateStatus(Status.거래완료);

            // 업데이트된 정보를 TradeBoardDto로 변환하여 반환
            return tradeBoard.getId();
        } else {
            // 해당 id에 해당하는 게시글이 없는 경우 처리
            throw ErrorCode.throwTradeBoardNotFound();
        }
    }
    /**
     * 조회수 증가
     * @param : Long id
     */
    public synchronized Integer updateView(Long id) {
        Integer view = tradeBoardRepository.updateView(id);
        return view;
    }
    /**
     * 거래 완료 상태 변경 메서드
     * 수정 사항 : Status status
     * 쿼리 대신 변경 감지
     * @param : Long id
     */
    @Transactional
    public TradeBoardResponseDto setBuyer(Long id, TradeBoardRequestDto tradeBoardRequestDto) {
        TradeBoard tradeBoard = tradeBoardRepository.findTradeBoardById(id);
        // 구매자 업데이트
        tradeBoardRepository.updateBuyer(tradeBoard.getId(), tradeBoardRequestDto.getBuyer());

        return TradeBoardResponseDto.convertTradeBoardToDto(tradeBoard);

    }
    /**
     * 모든 거래 게시글 조회
     * @param : String search, Pageable pageable
     */
    @Transactional(readOnly = true)
    public Page<TradeBoardResponseDto> pageList(String search, Pageable pageable) {
        Page<TradeBoard> tradeBoards = tradeBoardRepository.findByTitleContainingOrContentContaining(search, search, pageable);

        return tradeBoards.map(tradeBoard -> TradeBoardResponseDto.convertTradeBoardToDto(tradeBoard));
    }
    /**
     * 단건 거래 게시글 조회
     * @param : Long id
     */
    @Transactional(readOnly = true)
    public TradeBoardResponseDto findById(Long id){
        TradeBoard tradeBoard = tradeBoardRepository.findById(id).orElseThrow(ErrorCode::throwTradeBoardNotFound);

        return TradeBoardResponseDto.convertTradeBoardToDto(tradeBoard);

    }
    /**
     * 찜 갯수 증가
     * @param : Long tradeBoardId
     */
    public synchronized void increaseGoodCount(Long tradeBoardId) {
        TradeBoard tradeBoard = tradeBoardRepository.findById(tradeBoardId)
                .orElseThrow(ErrorCode::throwTradeBoardNotFound);

        tradeBoard.increaseGoodsCount(); // TradeBoard 엔티티의 메서드를 호출하여 찜 개수 증가
        tradeBoardRepository.save(tradeBoard);
    }
    /**
     * 찜 갯수 감소
     * @param : Long tradeBoardId
     */
    public synchronized void decreaseGoodCount(Long tradeBoardId) {
        TradeBoard tradeBoard = tradeBoardRepository.findById(tradeBoardId)
                .orElseThrow(ErrorCode::throwTradeBoardNotFound);

        tradeBoard.decreaseGoodsCount(); // TradeBoard 엔티티의 메서드를 호출하여 찜 개수 증가
        tradeBoardRepository.save(tradeBoard);
    }
    /**
     * 게시글 삭제
     * 삭제 시 채팅 마이크로 서비스의 채팅 데이터를 삭제해야하므로
     * 카프카 이벤트를 통한 비동기 통신 및 느슨한 결합
     * @param : Long id
     */
    @Transactional
    public void deletePost(Long id) {
        TradeBoard tradeBoard = tradeBoardRepository.findById(id).orElseThrow(ErrorCode::throwMemberNotFound);
        TradeBoardRequestDto tradeBoardRequestDto=TradeBoardRequestDto.builder()
                .id(tradeBoard.getId())
                .memberNo(tradeBoard.getMember().getId())
                .title(tradeBoard.getTitle())
                .content(tradeBoard.getContent())
                .build();
        tradeBoardRepository.delete(tradeBoard);
        /*send this deletePost to the kafka*/
        kafkaProducer.send("deletePost", tradeBoardRequestDto);
    }
    /**
     * 유저가 올린 거래 게시글 조회
     * @param : Long id
     */
    @Transactional(readOnly = true)
    public List<TradeBoardResponseDto> showTradeInfo(Long id){
        List<TradeBoard> tradeBoards = tradeBoardRepository.findTradeBoardByMemberId(id);
        List<TradeBoardResponseDto> tradeBoardResponseDtos = tradeBoards.stream()
                .map(tradeBoard -> convertTradeBoardToDto(tradeBoard)) // TradeDto로 변환
                .collect(Collectors.toList());
        return tradeBoardResponseDtos;
    }
    /**
     * 구매한 거래 게시글 조회
     * @param : Long id
     */
    @Transactional(readOnly = true)
    public List<TradeBoardResponseDto> showBuyInfo(Long id){
        Member member=memberRepository.findById(id).orElseThrow(ErrorCode::throwMemberNotFound);
        List<TradeBoard> tradeBoards = tradeBoardRepository.findTradeBoardByBuyer(member.getNickname());
        List<TradeBoardResponseDto> tradeBoardResponseDtos = tradeBoards.stream()
                .map(tradeBoard -> convertTradeBoardToDto(tradeBoard))
                .collect(Collectors.toList());
        return tradeBoardResponseDtos;
    }
}
