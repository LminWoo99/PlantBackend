package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Goods;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.GoodsDto;
import Plant.PlantProject.dto.GoodsRequestDto;
import Plant.PlantProject.dto.TradeDto;
import Plant.PlantProject.exception.GoodsNotFoundException;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.service.GoodsService;
import Plant.PlantProject.service.TradeBoardService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Slf4j
public class GoodsController {
    private final GoodsService goodsService;
    private final TradeBoardService tradeBoardService;
    private final GoodsRepository goodsRepository;


    @PostMapping("/goods/{memberId}")
    public ResponseEntity<GoodsDto> saveGoods(@PathVariable Long memberId, @RequestBody GoodsRequestDto goodsRequestDto){


        log.info("찜 저장");
        return ResponseEntity.ok().body(goodsService.saveGoods(goodsRequestDto));
    }
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
    @GetMapping("/goods/status")
    public ResponseEntity<GoodsDto> infoGoodsStatus(@RequestParam Long memberId, @RequestParam Long tradeBoardId) {
        Goods goods = goodsRepository.findByMemberIdAndTradeBoardId(memberId, tradeBoardId).orElseThrow(GoodsNotFoundException::new);
        GoodsDto goodsDto = GoodsDto.convertGoodsToDto(goods);
        return ResponseEntity.ok().body(goodsDto);
    }


    }

