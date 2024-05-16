package Plant.PlantProject.controller.keyword;

import Plant.PlantProject.dto.request.KeywordRequestDto;
import Plant.PlantProject.dto.response.KeywordResponseDto;
import Plant.PlantProject.service.keyword.KeywordService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeywordController {
    private final KeywordService keywordService;

    @GetMapping("/keyword/{memberNo}")
    @Operation(summary = "유저 기능- 설정한 키워드 조회", description = "유저가 설정한 키워드 조회 조회 할 수 있는 API")
    private ResponseEntity<List<KeywordResponseDto>> getKeywordList(@PathVariable("memberNo") Integer memberNo) {

        List<KeywordResponseDto> keywordList = keywordService.getKeywordList(memberNo);
        return ResponseEntity.ok().body(keywordList);
    }
    @PostMapping("/keyword")
    @Operation(summary = "유저 기능- 키워드 설정", description = "유저가 알림을 받기 위한 키워드 설정 할 수 있는 API")
    private ResponseEntity<Long> saveKeyWord(@RequestBody KeywordRequestDto keywordRequestDto) {

        Long keyWordId = keywordService.saveKeyWord(keywordRequestDto);
        return ResponseEntity.ok().body(keyWordId);
    }
    @DeleteMapping("/keyword/{keywordId}")
    @Operation(summary = "유저 기능- 설정한 키워드 삭제", description = "유저가 설정한 키워드 삭제할 수 있는 API")
    private void getKeywordList(@PathVariable("keywordId") Long keywordId) {

        keywordService.deleteKeyWord(keywordId);
    }

}
