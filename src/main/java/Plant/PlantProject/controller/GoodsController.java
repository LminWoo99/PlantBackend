package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Goods;
import Plant.PlantProject.Entity.GoodsStatus;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.GoodsDto;
import Plant.PlantProject.dto.GoodsRequestDto;
import Plant.PlantProject.dto.TradeBoardDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.service.GoodsService;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class GoodsController {
    private final GoodsService goodsService;
    private final TradeBoardService tradeBoardService;
    private final GoodsRepository goodsRepository;

    @GetMapping("/goods/{memberId}")
    public ResponseEntity<List<TradeDto>> searchGoods(@PathVariable Long memberId){
        List<GoodsDto> goodsDtos = goodsService.searchGoods(memberId);
        List<TradeDto> tradeBoards = new ArrayList<>();
        for (GoodsDto goodsDto : goodsDtos) {
            TradeBoard tradeBoard = tradeBoardService.findTradeBoardById(goodsDto.getTradeBoardId());
            TradeDto tradeDto = TradeDto.convertTradeBoardToDto(tradeBoard);
            tradeBoards.add(tradeDto);
        }
        return ResponseEntity.ok().body(tradeBoards);

    }
    @PostMapping("/goods/{memberId}")
    public ResponseEntity<GoodsDto> saveGoods(@PathVariable Long memberId, @RequestBody GoodsRequestDto goodsRequestDto){
        System.out.println("찜 번호"+goodsRequestDto.getMemberId());
        System.out.println(goodsRequestDto.getTradeBoardId());

        log.info("찜 저장");
        return ResponseEntity.ok().body(goodsService.saveGoods(goodsRequestDto));
    }
    @DeleteMapping("/goods/delete")
    public void deleteGoods(@RequestParam Long memberId, @RequestParam Long tradeBoardId){
        goodsService.deleteGoods(memberId, tradeBoardId);

    }
    @GetMapping("/goods/status")
    public ResponseEntity<GoodsStatus> infoGoodsStatus(@RequestParam Long memberId, @RequestParam Long tradeBoardId){
        Optional<Goods> goods = goodsRepository.findByMemberIdAndTradeBoardId(memberId, tradeBoardId);
        if (goods.isEmpty()){
            return ResponseEntity.ok().body(null);
        }
        else{
            GoodsStatus goodsStatus = goodsService.updateGoodStatus(goods);
            return ResponseEntity.ok().body(goodsStatus);
        }

    }
}
