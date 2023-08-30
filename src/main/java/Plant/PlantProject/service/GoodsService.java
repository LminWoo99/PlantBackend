package Plant.PlantProject.service;

import Plant.PlantProject.Entity.Goods;
import Plant.PlantProject.Entity.GoodsStatus;
import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.GoodsDto;
import Plant.PlantProject.dto.GoodsRequestDto;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.exception.GoodsNotFoundException;
import Plant.PlantProject.exception.TradeBoardNotFoundException;
import Plant.PlantProject.exception.UserNotFoundException;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.repository.MemberRepository;
import Plant.PlantProject.repository.TradeBoardRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityNotFoundException;
import java.net.UnknownServiceException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static Plant.PlantProject.Entity.GoodsStatus.찜취소하기;
import static Plant.PlantProject.Entity.GoodsStatus.찜하기;
import static Plant.PlantProject.dto.GoodsDto.convertGoodsToDto;
import static Plant.PlantProject.dto.TradeDto.convertTradeBoardToDto;

@Service
@Slf4j
@RequiredArgsConstructor
public class GoodsService {
    private final MemberRepository memberRepository;
    private final TradeBoardRepository tradeBoardRepository;
    private final GoodsRepository goodsRepository;

    public GoodsDto saveGoods(GoodsRequestDto goodsDto){
        // 해당 유저와 게시글에 대한 Goods 객체가 이미 존재하는지 확인
        Optional<Goods> existingGoods = goodsRepository.findByMemberIdAndTradeBoardId(
                goodsDto.getMemberId(), goodsDto.getTradeBoardId());

        if (existingGoods.isPresent()) {
            // 이미 찜한 상태라면 기존 Goods 객체 반환
            return GoodsDto.convertGoodsToDto(existingGoods.get());
        } else {
            // 새로운 찜 정보 저장
            Goods goods = goodsRepository.save(
                    Goods.createGoods(
                            memberRepository.findById(goodsDto.getMemberId())
                                    .orElseThrow(UserNotFoundException::new),
                            tradeBoardRepository.findById(goodsDto.getTradeBoardId())
                                    .orElseThrow(TradeBoardNotFoundException::new)
                    ));
            return GoodsDto.convertGoodsToDto(goods);
        }
    }
    public GoodsStatus updateGoodStatus(Optional<Goods> goods){
        if(goods.get().getGoodsStatus()==찜취소하기){
            goods.get().setGoodsStatus(찜하기);
        }
        else{
            goods.get().setGoodsStatus(찜취소하기);
        }
        if (goods.isPresent()) {
            Goods good = goods.get();
            good.setGoodsStatus(goods.get().getGoodsStatus());
            Goods save = goodsRepository.save(good);



            return save.getGoodsStatus();
        } else {
            // 해당 id에 해당하는 게시글이 없는 경우 처리
            throw new EntityNotFoundException("Goods not found with id: " + goods.get().getId());
        }

    }

    public List<GoodsDto> searchGoods(Long memberId){
        List<Goods> goods = goodsRepository.findByMemberId(memberId);
        List<GoodsDto> goodsDtos = goods.stream()
                .map(good -> convertGoodsToDto(good)) // TradeDto로 변환
                .collect(Collectors.toList());
        return goodsDtos;
    }
    public void deleteGoods(Long memberId, Long tradeBoardId) {
        Goods goods = goodsRepository.findByMemberIdAndTradeBoardId(memberId, tradeBoardId).orElseThrow(GoodsNotFoundException::new);
        goodsRepository.delete(goods);
    }
}
