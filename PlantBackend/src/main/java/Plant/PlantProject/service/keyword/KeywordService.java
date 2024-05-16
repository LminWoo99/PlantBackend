package Plant.PlantProject.service.keyword;

import Plant.PlantProject.common.exception.ErrorCode;
import Plant.PlantProject.domain.Entity.Keyword;
import Plant.PlantProject.domain.NotifiTypeEnum;
import Plant.PlantProject.dto.request.KeywordRequestDto;
import Plant.PlantProject.dto.response.KeywordResponseDto;
import Plant.PlantProject.dto.response.NotificationEventDto;
import Plant.PlantProject.repository.tradeboard.KeywordRepository;
import Plant.PlantProject.service.notification.NotificationSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class KeywordService {
    private final KeywordRepository keyWordRepository;
    private final NotificationSender notificationSender;
    /**
     * 유저가 설정한 키워드 조회
     * @param : Integer memberNo
     */
    public List<KeywordResponseDto> getKeywordList(Integer memberNo) {
        List<Keyword> keywordList = keyWordRepository.findByMemberNo(memberNo);
        if (keywordList.isEmpty()) {
            throw ErrorCode.throwKeywordNotFound();
        }
        List<KeywordResponseDto> keywordResponseDtoList = KeywordResponseDto.listToDto(keywordList);
        return keywordResponseDtoList;

    }
    /**
     * 유저가 알림을 받을 키워드 저장
     * 저장후 카프카 중앙 토픽에 이벤트 발송
     * @param : GoodsRequestDto goodsDto
     */
    public Long saveKeyWord(KeywordRequestDto keyWordRequestDto) {
        Keyword keyWord = Keyword.builder()
                .keywordContent(keyWordRequestDto.getKeywordContent())
                .memberNo(keyWordRequestDto.getMemberNo())
                .build();

        return keyWordRepository.save(keyWord).getId();
    }
    /**
     * 키워드 삭제
     * @param : Long keywordId
     */
    public void deleteKeyWord(Long keywordId) {
        keyWordRepository.deleteById(keywordId);
    }
    /**
     * 게시글에서 키워드 설정후 해당 키워드를 가지고있는
     * 유저 조회
     * @param : Integer tradeBoardNo, String keywordContent
     */
    public void getMembersByKeyword(Integer tradeBoardNo, String keywordContent) {
        List<Integer> memberList = keyWordRepository.findMemberNosByKeywordContent(keywordContent);
        if (!memberList.isEmpty()) {
            sendKeywordNotificationData(tradeBoardNo, memberList, keywordContent);
        }
    }
    /**
     * 게시글에서 키워드 설정후 해당 키워드를 가지고있는 유저 리스트를 받고
     * kafka를 통한 plant-chat-service에 전달
     * @param : Integer tradeBoardNo, List<Integer> memberList
     */
    private void sendKeywordNotificationData(Integer tradeBoardNo, List<Integer> memberList, String keywordContent) {

        List<NotificationEventDto> notificationEventDtoList = memberList.stream().map(memberNo -> {
            NotificationEventDto notificationEventDto = NotificationEventDto.builder()
                    .receiverNo(memberNo)
                    .content(keywordContent)
                    .senderNo(tradeBoardNo)
                    .type(NotifiTypeEnum.KEYWORD)
                    .resource(tradeBoardNo.toString())
                    .build();
            return notificationEventDto;
        }).collect(Collectors.toList());

        // 키워드 이벤트
        for (NotificationEventDto notificationEventDto : notificationEventDtoList) {
            notificationSender.send("notification", notificationEventDto);
        }

    }
}
