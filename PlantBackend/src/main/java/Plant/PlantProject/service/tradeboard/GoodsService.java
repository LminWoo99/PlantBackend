package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Goods;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.domain.NotifiTypeEnum;
import Plant.PlantProject.service.notification.NotificationSender;
import Plant.PlantProject.dto.response.GoodsResponseDto;
import Plant.PlantProject.dto.request.GoodsRequestDto;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.dto.response.NotificationEventDto;
import Plant.PlantProject.dto.response.TradeBoardResponseDto;
import Plant.PlantProject.repository.tradeboard.GoodsRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.tradeboard.querydsl.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Plant.PlantProject.dto.response.GoodsResponseDto.convertGoodsToDto;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GoodsService {
    private final MemberRepository memberRepository;
    private final TradeBoardRepository tradeBoardRepository;
    private final GoodsRepository goodsRepository;
    private final NotificationSender notificationSender;
    /**
     * 찜 저장
     * @param : GoodsRequestDto goodsDto
     */
    public GoodsResponseDto saveGoods(GoodsRequestDto goodsDto){
        // 해당 유저와 게시글에 대한 Goods 객체가 이미 존재하는지 확인
        Optional<Goods> existingGoods = goodsRepository.findByMemberIdAndTradeBoardId(
                goodsDto.getMemberId(), goodsDto.getTradeBoardId());

        if (existingGoods.isPresent()) {
//             이미 찜한 상태라면 삭제
            goodsRepository.delete(existingGoods.get());
            existingGoods.get().getTradeBoard().decreaseGoodsCount();
            return null;
        } else {
            TradeBoard tradeBoard = tradeBoardRepository.findById(goodsDto.getTradeBoardId())
                    .orElseThrow(ErrorCode::throwTradeBoardNotFound);
            // 새로운 찜 정보 저장
            Goods goods = goodsRepository.save(
                    Goods.createGoods(
                            memberRepository.findById(goodsDto.getMemberId())
                                    .orElseThrow(ErrorCode::throwMemberNotFound),
                            tradeBoard
                    ));
            tradeBoard.increaseGoodsCount();

            sendNotificationData(goodsDto.getMemberId().intValue(), tradeBoard.getMember().getId().intValue(), tradeBoard.getId());

            return convertGoodsToDto(goods);
        }
    }
    /**
     * 자기가 찜한 거래 게시글 정보 조회
     * @param : Long memberId
     */
    public List<TradeBoardResponseDto> searchGoods(Long memberId){
        List<Goods> goods = goodsRepository.findByMemberId(memberId);
        List<TradeBoardResponseDto> tradeBoardResponseDtoList = goods.stream().map(good -> {
            TradeBoardResponseDto tradeBoardResponseDto = TradeBoardResponseDto.convertTradeBoardToDto(good.getTradeBoard());
            return tradeBoardResponseDto;
        }).collect(Collectors.toList());

        return tradeBoardResponseDtoList;
    }
    /**
     * 찜 상태 조회
     * @param : Long memberId, , Long tradeBoardId
     */
    public Boolean findByMemberIdAndTradeBoardId(Long memberId, Long tradeBoardId) {
        Optional<Goods> goods = goodsRepository.findByMemberIdAndTradeBoardId(memberId, tradeBoardId);
        if (goods.isEmpty()) {
            return Boolean.FALSE;
        }
        return  Boolean.TRUE;
    }
    public void deleteGoods(TradeBoard tradeBoard) {
        goodsRepository.deleteAllByTradeBoard(tradeBoard);
    }

    /**
     * plant-chat-service로 kafka를 통한
     * 메세지 스트리밍
     * @Param SnsComment snsComment, Integer senderNo, Integer receiverNo
     */
    private void sendNotificationData(Integer senderNo, Integer receiverNo, Long tradeBoardNo) {
        NotificationEventDto notificationEventDto=NotificationEventDto.builder()
                .senderNo(senderNo)
                .receiverNo(receiverNo)
                .type(NotifiTypeEnum.TRADEBOARD_GOODS)
                .resource(tradeBoardNo.toString())
                .build();

        // 알림 이벤트 발행
        notificationSender.send("notification", notificationEventDto);

    }

}
