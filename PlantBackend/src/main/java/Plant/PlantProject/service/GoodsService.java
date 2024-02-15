package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Goods;
import Plant.PlantProject.dto.GoodsDto;
import Plant.PlantProject.dto.vo.GoodsRequestDto;
import Plant.PlantProject.exception.TradeBoardNotFoundException;
import Plant.PlantProject.exception.UserNotFoundException;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Plant.PlantProject.dto.GoodsDto.convertGoodsToDto;


@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService {
    private final MemberRepository memberRepository;
    private final TradeBoardRepository tradeBoardRepository;
    private final GoodsRepository goodsRepository;
    private final TradeBoardService tradeBoardService;
    public GoodsDto saveGoods(GoodsRequestDto goodsDto){
        // 해당 유저와 게시글에 대한 Goods 객체가 이미 존재하는지 확인
        Optional<Goods> existingGoods = goodsRepository.findByMemberIdAndTradeBoardId(
                goodsDto.getMemberId(), goodsDto.getTradeBoardId());

        if (existingGoods.isPresent()) {
//             이미 찜한 상태라면 삭제
            goodsRepository.delete(existingGoods.get());
            tradeBoardService.decreaseGoodCount(goodsDto.getTradeBoardId());
            return null;
        } else {
            // 새로운 찜 정보 저장
            Goods goods = goodsRepository.save(
                    Goods.createGoods(
                            memberRepository.findById(goodsDto.getMemberId())
                                    .orElseThrow(UserNotFoundException::new),
                            tradeBoardRepository.findById(goodsDto.getTradeBoardId())
                                    .orElseThrow(TradeBoardNotFoundException::new)
                    ));
            tradeBoardService.increaseGoodCount(goodsDto.getTradeBoardId());
            return convertGoodsToDto(goods);
        }
    }
    public List<GoodsDto> searchGoods(Long memberId){
        List<Goods> goods = goodsRepository.findByMemberId(memberId);
        List<GoodsDto> goodsDtos = goods.stream()
                .map(good -> convertGoodsToDto(good)) // TradeDto로 변환
                .collect(Collectors.toList());
        return goodsDtos;
    }


}
