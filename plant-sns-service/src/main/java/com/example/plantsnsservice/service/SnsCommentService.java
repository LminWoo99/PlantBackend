package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.domain.NotifiTypeEnum;
import com.example.plantsnsservice.domain.entity.SnsComment;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.repository.querydsl.SnsCommentRepository;
import com.example.plantsnsservice.vo.request.NotificationEventDto;
import com.example.plantsnsservice.vo.request.SnsCommentRequestDto;
import com.example.plantsnsservice.vo.response.SnsCommentResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsCommentService {
    private final SnsCommentRepository snsCommentRepository;
    private final SnsPostRepository snsPostRepository;
    private final NotificationSender notificationSender;

    /**
     * sns 댓글 저장
     * @param : SnsCommentRequestDto snsCommentRequestDto
     */
    @Transactional
    public SnsCommentResponseDto createComment(SnsCommentRequestDto snsCommentRequestDto) {
        SnsPost snsPost = snsPostRepository.findById(snsCommentRequestDto.getSnsPostId()).orElseThrow(ErrorCode::throwSnsPostNotFound);

        SnsComment snsComment=SnsComment.builder()
                .snsPost(snsPost)
                .content(snsCommentRequestDto.getContent())
                .createdBy(snsCommentRequestDto.getCreatedBy())
                .parent(snsCommentRequestDto.getParentId() != null ?
                        snsCommentRepository.findById(snsCommentRequestDto.getParentId()).orElseThrow(ErrorCode::throwCommentNotFound): null)
                .build();

        snsCommentRepository.save(snsComment);
        //게시글에는 본인도 댓글을 달수있기떄문에, 다른 사람이 댓글달때만 알림 전송
        if (!(snsCommentRequestDto.getSenderNo().equals(snsComment.getSnsPost().getMemberNo()))) {
            getNotificationData(snsComment, snsCommentRequestDto.getSenderNo(), snsPost.getMemberNo());
        }
        return SnsCommentResponseDto.convertCommentToDto(snsComment);
    }
    /**
     * sns 댓글 조회 메서드
     * 각각 게시글에 속한 댓글 조회(부모 댓글 기준으로 정렬)
     * @param : Long snsPostId
     */
    public List<SnsCommentResponseDto> findCommentListByPostId(Long snsPostId) {
        List<SnsComment> snsCommentList = snsCommentRepository.findSnsCommentByPostId(snsPostId);
        //entity <==> dto 변환
        List<SnsCommentResponseDto> snsCommentResponseDtoList = snsCommentList.stream().map(snsComment -> SnsCommentResponseDto.convertCommentToDto(snsComment))
                .collect(Collectors.toList());
        //대댓글 중첩구조 변환후 리턴
        return convertNestedStructure(snsCommentResponseDtoList);
    }

    /**
     * sns 댓글 삭제 메서드
     * 부모 댓글 삭제시 댓글에 속한 대댓글 모두 삭제해버리기
     * @param : Long snsCommentId
     */
    public void deleteSnsComment(Long snsCommentId) {
        snsCommentRepository.deleteById(snsCommentId);

    }
    /**
     * 게시글 삭제시 게시글에 있는 댓글  전체삭제 메서드
     * @param : Long snsPostId
     */
    @Transactional
    public void deleteSnsCommentBySnsPost(Long snsPostId) {
        //댓글 <==> 대댓글 대댓글의 중첩구조 변환
        List<SnsCommentResponseDto> snsCommentResponseDtoList = findCommentListByPostId(snsPostId);
        //부모 댓글 id 리스트 화
        List<Long> commentIdList = snsCommentResponseDtoList.stream().map(snsCommentResponseDto -> {
            return snsCommentResponseDto.getId();
        }).collect(Collectors.toList());

        for (Long commentId : commentIdList) {
            snsCommentRepository.deleteById(commentId);
        }

    }

    /**
     * sns 댓글 <==> 대댓글 대댓글의 중첩구조 변환 메서드
     * @param : List<SnsComment> snsCommentList
     */

    private List<SnsCommentResponseDto> convertNestedStructure(List<SnsCommentResponseDto> snsCommentResponseDtoList) {
        List<SnsCommentResponseDto> result = new ArrayList<>();
        Map<Long, SnsCommentResponseDto> map = new HashMap<>();

        snsCommentResponseDtoList.stream().forEach(c -> {
            map.put(c.getId(), c);
            if(c.getParentId() != null) {
                map.get(c.getParentId()).getChildren().add(c);
            }
            else {
                result.add(c);
            }
        });
        return result;
    }

    /**
     * plant-chat-service로 kafka를 통한
     * 메세지 스트리밍
     * @Param SnsComment snsComment, Integer senderNo, Integer receiverNo
     */
    private void getNotificationData(SnsComment snsComment, Integer senderNo, Integer receiverNo) {
        NotificationEventDto notificationEventDto=NotificationEventDto.builder()
                .senderNo(senderNo)
                .receiverNo(receiverNo)
                .type(NotifiTypeEnum.SNS_COMMENT)
                .content(snsComment.getContent())
                .resource(snsComment.getSnsPost().getId().toString())
                .build();

        // 알림 이벤트 발행
        notificationSender.send("notification", notificationEventDto);

    }


}
