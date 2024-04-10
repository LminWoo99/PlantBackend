package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsPostService {
    private final SnsPostRepository snsPostRepository;
    private final SnsHashTagMapService snsHashTagMapService;

    /**
     * sns 게시글 생성
     * 게시글 형식은 게시글 문구, 해시태그, 게시글 내용
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public Long createPost(SnsPostRequestDto snsPostRequestDto) {
        SnsPost snsPost = SnsPost.builder()
                .snsPostTitle(snsPostRequestDto.getSnsPostTitle())
                .snsPostContent(snsPostRequestDto.getSnsPostContent())
                .memberNo(snsPostRequestDto.getMemberNo())
                .build();
        //게시글 저장후 해시태그 저장 메서드 호출
        snsHashTagMapService.createHashTag(snsPost, snsPostRequestDto.getHashTags());

        return snsPostRepository.save(snsPost).getId();



    }
    /**
     * sns 게시글 단건 조회
     * @param : Long snsPostId(게시글 번호)
     */
    public SnsPostResponseDto findById(Long snsPostId) {
        SnsPost snsPost = snsPostRepository.findById(snsPostId).orElseThrow(ErrorCode::throwSnsPostNotFound);
        return SnsPostResponseDto.builder()
                .id(snsPost.getId())
                .snsPostTitle(snsPost.getSnsPostTitle())
                .snsPostContent(snsPost.getSnsPostContent())
                .memberNo(snsPost.getMemberNo())
                .createdAt(snsPost.getCreatedAt())
                .snsLikesCount(snsPost.getSnsLikesCount())
                .snsViewsCount(snsPost.getSnsViewsCount())
                .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                .build();
    }
    /**
     * sns 게시글 전체 조회
     * 게시글 수정은 문구랑 내용만 가능(직접 update 쿼리 대신 변경 감지로)
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public void updateSnsPost(SnsPostRequestDto snsPostRequestDto) {
        SnsPost snsPost = snsPostRepository.findById(snsPostRequestDto.getId()).orElseThrow(ErrorCode::throwSnsPostNotFound);
        //변경 감지
        snsPost.updateSnsPost(snsPostRequestDto);

        snsPostRepository.save(snsPost);

    }
    /**
     * sns 게시글 조회
     * 게시글 수정은 문구랑 내용만 가능
     * 게시글은 시간순으로 정렬
     * @param : x
     */
    @Transactional(readOnly = true)
    public List<SnsPostResponseDto> getSnsPostList() {
        List<SnsPost> snsPostList = snsPostRepository.findAllByOrderByCreatedAtDesc();

        return snsPostList.stream().map(snsPost -> SnsPostResponseDto.builder()
                .id(snsPost.getId())
                .snsPostTitle(snsPost.getSnsPostTitle())
                .snsPostContent(snsPost.getSnsPostContent())
                .memberNo(snsPost.getMemberNo())
                .snsLikesCount(snsPost.getSnsLikesCount())
                .snsViewsCount(snsPost.getSnsViewsCount())
                .createdAt(snsPost.getCreatedAt())
                .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                .build())
                .collect(Collectors.toList());
    }

    /**
     * 해시태그 기준으로 sns 게시글 조회
     * 해시태그 없을 경우 예외 처리
     * @param : String hashTagName(해시 태그 이름)
     */
    public List<SnsPostResponseDto> findAllByHashTag(String hashTagName) {
        List<SnsPostResponseDto> snsPosts = snsPostRepository.findAllByHashTag(hashTagName);
        if (snsPosts.isEmpty()) {
            throw new CustomException(ErrorCode.HASH_TAG_NOT_FOUND);
        }
        return snsPosts;
    }
    /**
     * sns 게시글 삭제
     * @param : Long id(게시글 번호)
     */
    @Transactional
    public void deleteSnsPost(Long id) {
        snsPostRepository.deleteById(id);
    }


}
