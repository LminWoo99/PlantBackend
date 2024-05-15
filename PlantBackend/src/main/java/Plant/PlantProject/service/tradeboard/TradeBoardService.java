package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Member;
import Plant.PlantProject.domain.Entity.Status;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.dto.request.TradeBoardRequestDto;
import Plant.PlantProject.dto.response.TradeBoardResponseDto;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.tradeboard.TradeBoardRepository;
import Plant.PlantProject.service.keyword.KeywordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Plant.PlantProject.dto.response.TradeBoardResponseDto.convertTradeBoardToDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class TradeBoardService {
    private final TradeBoardRepository tradeBoardRepository;
    private final MemberRepository memberRepository;
    private final ImageFileUploadService imageFileUploadService;
    private final GoodsService goodsService;
    private final DeleteTradeBoardProducer deleteTradeBoardProducer;
    private final KeywordService keywordService;

    /**
     * 거래게시글 저장
     * @param : TradeBoardRequestDto tradeBoardRequestDto, String username
     */
    @Transactional
    public Long saveTradePost(TradeBoardRequestDto tradeBoardRequestDto, List<MultipartFile> files) throws IOException {
        Member member = memberRepository.findByUsername(tradeBoardRequestDto.getUsername()).orElseThrow(ErrorCode::throwMemberNotFound);

        TradeBoard tradeBoard=tradeBoardRepository.save(
                TradeBoard.createTradeBoard(member,
                        tradeBoardRequestDto.getTitle(),
                        tradeBoardRequestDto.getContent(),
                        member.getNickname(),
                        tradeBoardRequestDto.getPrice(),
                        tradeBoardRequestDto.getKeyWordContent()
                )
        );
        Long id = tradeBoardRepository.save(tradeBoard).getId();
        if (!files.isEmpty()){
            imageFileUploadService.saveImages(files, tradeBoard);
        }
        if (tradeBoardRequestDto.getKeyWordContent() != null) {
            keywordService.getMembersByKeyword(tradeBoard.getId().intValue(), tradeBoardRequestDto.getKeyWordContent());
        }

        return id;
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
            tradeBoard.updatePost(tradeBoardRequestDto.getTitle(), tradeBoardRequestDto.getContent(), tradeBoardRequestDto.getPrice());
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
    private synchronized Integer updateView(Long id) {
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
        // 구매자 업데이트
        Optional<TradeBoard> optionalTradeBoard = tradeBoardRepository.findById(id);

        if (optionalTradeBoard.isPresent()) {
            TradeBoard tradeBoard = optionalTradeBoard.get();
            tradeBoard.updateBuyer(tradeBoardRequestDto.getBuyer(), Status.거래완료);

            // 업데이트된 정보를 TradeBoardDto로 변환하여 반환
            return TradeBoardResponseDto.convertTradeBoardToDto(tradeBoard);
        } else {
            // 해당 id에 해당하는 게시글이 없는 경우 처리
            throw ErrorCode.throwTradeBoardNotFound();
        }



    }
    /**
     * 모든 거래 게시글 조회
     * @param : String search, Pageable pageable
     */
    @Transactional(readOnly = true)
    public Page<TradeBoardResponseDto> pageList(String search, Pageable pageable) {
        Page<TradeBoard> tradeBoards = tradeBoardRepository.findByTitleContainingOrContentContainingOrKeywordContentContaining(search, search,search, pageable);

        return tradeBoards.map(tradeBoard -> TradeBoardResponseDto.convertTradeBoardToDto(tradeBoard));
    }
    /**
     * 단건 거래 게시글 조회
     * @param : Long id
     */
    @Transactional
    public TradeBoardResponseDto findById(Long id){
        TradeBoard tradeBoard = tradeBoardRepository.findById(id).orElseThrow(ErrorCode::throwTradeBoardNotFound);
        updateView(id);
        return TradeBoardResponseDto.convertTradeBoardToDto(tradeBoard);

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
        //관련 연관관계 단방향이므로 모두 삭제후 게시글 삭제
        goodsService.deleteGoods(tradeBoard);


        //게시글 삭제
        tradeBoardRepository.delete(tradeBoard);


        /*send this deletePost to the kafka*/
        deleteTradeBoardProducer.send("deletePost", tradeBoardRequestDto);
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
