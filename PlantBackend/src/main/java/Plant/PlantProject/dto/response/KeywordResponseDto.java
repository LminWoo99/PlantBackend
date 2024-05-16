package Plant.PlantProject.dto.response;

import Plant.PlantProject.domain.Entity.Keyword;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class KeywordResponseDto {
    private Long keywordId;
    private String keywordContent;
    private Integer memberNo;

    public static List<KeywordResponseDto> listToDto(List<Keyword> keywordList) {
        List<KeywordResponseDto> keywordResponseDtoList = keywordList.stream().map(keyword -> {
            KeywordResponseDto keywordResponseDto = KeywordResponseDto.builder()
                    .keywordId(keyword.getId())
                    .keywordContent(keyword.getKeywordContent())
                    .memberNo(keyword.getMemberNo())
                    .build();
            return keywordResponseDto;
        }).collect(Collectors.toList());

        return keywordResponseDtoList;

    }
}
