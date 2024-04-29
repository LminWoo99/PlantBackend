package Plant.PlantProject.service.tradeboard;

import Plant.PlantProject.domain.Entity.Goods;
import Plant.PlantProject.domain.Entity.TradeBoard;
import Plant.PlantProject.vo.response.GoodsResponseDto;
import Plant.PlantProject.vo.request.GoodsRequestDto;
import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.vo.response.TradeBoardResponseDto;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Plant.PlantProject.vo.response.GoodsResponseDto.convertGoodsToDto;


@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class GoodsService {
    private final MemberRepository memberRepository;
    private final TradeBoardRepository tradeBoardRepository;
    private final GoodsRepository goodsRepository;
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
    public GoodsResponseDto findByMemberIdAndTradeBoardId(Long memberId, Long tradeBoardId) {
        Goods goods = goodsRepository.findByMemberIdAndTradeBoardId(memberId, tradeBoardId).orElseThrow(ErrorCode::throwGoodsNotFound);
        return GoodsResponseDto.convertGoodsToDto(goods);
    }
    public void deleteGoods(TradeBoard tradeBoard) {
        goodsRepository.deleteAllByTradeBoard(tradeBoard);
    }

}
