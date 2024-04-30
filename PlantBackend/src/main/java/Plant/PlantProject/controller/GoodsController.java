package Plant.PlantProject.controller;

import Plant.PlantProject.vo.response.GoodsResponseDto;
import Plant.PlantProject.vo.request.GoodsRequestDto;
import Plant.PlantProject.vo.response.TradeBoardResponseDto;
import Plant.PlantProject.service.tradeboard.GoodsService;
import Plant.PlantProject.service.tradeboard.TradeBoardService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class GoodsController {
    private final GoodsService goodsService;

    @PostMapping("/goods/{memberId}")
    @Operation(summary = "찜 저장", description = "마음에 드는 중고 거래 게시글이 있을 경우,나중에 편하게 보기위해 찜을 할 수 있는 API")
    public ResponseEntity<GoodsResponseDto> saveGoods(@PathVariable Long memberId, @RequestBody GoodsRequestDto goodsRequestDto){

        return ResponseEntity.ok().body(goodsService.saveGoods(goodsRequestDto));
    }
    @GetMapping("/goods/{memberId}")
    @Operation(summary = "찜 조회", description = "내가 여태까지 한 찜 조회를 할 수 있는 API")
    public ResponseEntity<List<TradeBoardResponseDto>> searchGoods(@PathVariable Long memberId){
        List<TradeBoardResponseDto> tradeBoardResponseDtoList = goodsService.searchGoods(memberId);

        return ResponseEntity.ok().body(tradeBoardResponseDtoList);

    }
    @GetMapping("/goods/status")
    @Operation(summary = "찜 상태 조회", description = "찜 상태 조회 할 수 있는 API")
    public ResponseEntity<Boolean> infoGoodsStatus(@RequestParam Long memberId, @RequestParam Long tradeBoardId) {
        Boolean statusGood = goodsService.findByMemberIdAndTradeBoardId(memberId, tradeBoardId);

        return ResponseEntity.ok().body(statusGood);
    }


    }

