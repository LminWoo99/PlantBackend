package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.domain.entity.SnsComment;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.repository.querydsl.SnsCommentRepository;
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
     * @param : Long Long snsPostId
     */
    @Transactional
    public void deleteSnsCommentBySnsPost(Long snsPostId) {
        snsCommentRepository.deleteBySnsPostId(snsPostId);

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


}
