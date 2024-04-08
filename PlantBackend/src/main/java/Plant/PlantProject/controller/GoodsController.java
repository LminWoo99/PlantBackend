package Plant.PlantProject.controller;

import Plant.PlantProject.Entity.Goods;
import Plant.PlantProject.Entity.TradeBoard;
import Plant.PlantProject.dto.GoodsDto;
import Plant.PlantProject.dto.vo.GoodsRequestDto;
import Plant.PlantProject.dto.vo.ResponseTradeBoardDto;
import Plant.PlantProject.exception.GoodsNotFoundException;
import Plant.PlantProject.repository.GoodsRepository;
import Plant.PlantProject.service.GoodsService;
import Plant.PlantProject.service.TradeBoardService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "찜 저장", description = "마음에 드는 중고 거래 게시글이 있을 경우,나중에 편하게 보기위해 찜을 할 수 있는 API")
    public ResponseEntity<GoodsDto> saveGoods(@PathVariable Long memberId, @RequestBody GoodsRequestDto goodsRequestDto){

        log.info("찜 저장");
        return ResponseEntity.ok().body(goodsService.saveGoods(goodsRequestDto));
    }
    @GetMapping("/goods/{memberId}")
    @Operation(summary = "찜 조회", description = "내가 여태까지 한 찜 조회를 할 수 있는 API")
    public ResponseEntity<List<ResponseTradeBoardDto>> searchGoods(@PathVariable Long memberId){
        List<GoodsDto> goodsDtos = goodsService.searchGoods(memberId);
        List<ResponseTradeBoardDto> tradeBoards = new ArrayList<>();
        for (GoodsDto goodsDto : goodsDtos) {
            ResponseTradeBoardDto responseTradeBoardDto = tradeBoardService.findTradeBoardById(goodsDto.getTradeBoardId());
            tradeBoards.add(responseTradeBoardDto);
        }
        return ResponseEntity.ok().body(tradeBoards);

    }
    @GetMapping("/goods/status")
    @Operation(summary = "찜 상태 조회", description = "찜 상태 조회 할 수 있는 API")
    public ResponseEntity<GoodsDto> infoGoodsStatus(@RequestParam Long memberId, @RequestParam Long tradeBoardId) {
        Goods goods = goodsRepository.findByMemberIdAndTradeBoardId(memberId, tradeBoardId).orElseThrow(GoodsNotFoundException::new);
        GoodsDto goodsDto = GoodsDto.convertGoodsToDto(goods);
        return ResponseEntity.ok().body(goodsDto);
    }


    }

