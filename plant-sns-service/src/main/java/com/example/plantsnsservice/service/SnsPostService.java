package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.domain.SnsPost;
import com.example.plantsnsservice.repository.HashTagRepository;
import com.example.plantsnsservice.repository.ImageRepository;
import com.example.plantsnsservice.repository.SnsPostRepository;
import com.example.plantsnsservice.repository.querydsl.SnsCommentRepository;
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
    private final ImageRepository imageRepository;
    private final SnsCommentRepository snsCommentRepository;
    private final HashTagRepository hashTagRepository;

    /**
     * sns 게시글 생성
     * 게시글 형식은 게시글 문구, 해시태그, 게시글 내용
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public void createPost(SnsPostRequestDto snsPostRequestDto) {
        SnsPost snsPost = SnsPost.builder()
                .snsPostTitle(snsPostRequestDto.getSnsPostTitle())
                .snsPostContent(snsPostRequestDto.getSnsPostContent())
                .memberNo(snsPostRequestDto.getMemberNo())
                .build();
        snsPostRepository.save(snsPost);
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
                .build())
                .collect(Collectors.toList());
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
