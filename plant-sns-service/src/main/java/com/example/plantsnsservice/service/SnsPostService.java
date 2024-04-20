package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.domain.entity.Image;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.service.image.ImageService;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.redisson.Redisson;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsPostService {
    private final SnsPostRepository snsPostRepository;
    private final SnsHashTagMapService snsHashTagMapService;
    private final SnsCommentService snsCommentService;
    private final ImageService imageService;
    private final RedissonClient redissonClient;
    /**
     * sns 게시글 생성
     * 게시글 형식은 게시글 문구, 해시태그, 게시글 내용
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public Long createPost(SnsPostRequestDto snsPostRequestDto, List<MultipartFile> files) throws IOException {
        SnsPost snsPost = SnsPost.builder()
                .snsPostTitle(snsPostRequestDto.getSnsPostTitle())
                .snsPostContent(snsPostRequestDto.getSnsPostContent())
                .createdBy(snsPostRequestDto.getCreatedBy())
                .snsLikesCount(snsPostRequestDto.getSnsLikesCount())
                .snsViewsCount(snsPostRequestDto.getSnsViewsCount())
                .build();


        Long id = snsPostRepository.save(snsPost).getId();
        //게시글 저장후 해시태그 저장 메서드 호출
        snsHashTagMapService.createHashTag(snsPost, snsPostRequestDto.getHashTags());
        //이미지가 있을 경우에 업로드, 순환 참조 되지 않도록 설계!
        if (!files.isEmpty()) {
            imageService.uploadImagesToSnsPost(snsPost, files);
        }
        return id;
    }
    /**
     * sns 게시글 단건 조회
     * @param : Long snsPostId(게시글 번호)
     */
    @Transactional(readOnly = true)
    public SnsPostResponseDto findById(Long snsPostId) {
        SnsPost snsPost = snsPostRepository.findById(snsPostId).orElseThrow(ErrorCode::throwSnsPostNotFound);

        SnsPostResponseDto snsPostResponseDto = SnsPostResponseDto.builder()
                .id(snsPost.getId())
                .snsPostTitle(snsPost.getSnsPostTitle())
                .snsPostContent(snsPost.getSnsPostContent())
                .createdBy(snsPost.getCreatedBy())
                .createdAt(snsPost.getCreatedAt())
                .snsLikesCount(snsPost.getSnsLikesCount())
                .snsViewsCount(snsPost.getSnsViewsCount())
                .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                .build();

        snsPostResponseDto.imageUrls(snsPost);

        return snsPostResponseDto;
    }
    /**
     * sns 게시글 수정
     * 게시글 수정은  내용, 해시태그 가능(게시글 내용은 직접 update 쿼리 대신 변경 감지로)
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public void updateSnsPost(SnsPostRequestDto snsPostRequestDto) {
        SnsPost snsPost = snsPostRepository.findById(snsPostRequestDto.getId()).orElseThrow(ErrorCode::throwSnsPostNotFound);

        //변경 감지
        snsPost.updateSnsPost(snsPostRequestDto);

        //해시 태그 삭제 후 업데이트(조인해서 업데이트하는것보다 성능 상 우위)
        snsHashTagMapService.deleteSnsHashTagMap(snsPost);
        snsHashTagMapService.createHashTag(snsPost, snsPostRequestDto.getHashTags());

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

        List<SnsPostResponseDto> snsPosts = snsPostList.stream().map(snsPost -> {
            SnsPostResponseDto snsPostResponseDto = SnsPostResponseDto.builder()
                            .id(snsPost.getId())
                            .snsPostTitle(snsPost.getSnsPostTitle())
                            .snsPostContent(snsPost.getSnsPostContent())
                            .createdBy(snsPost.getCreatedBy())
                            .snsLikesCount(snsPost.getSnsLikesCount())
                            .snsViewsCount(snsPost.getSnsViewsCount())
                            .createdAt(snsPost.getCreatedAt())
                            .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                            .commentCount(snsCommentService.findCommentListByPostId(snsPost.getId()).stream().count())
                            .build();
                    snsPostResponseDto.imageUrls(snsPost);
                    return snsPostResponseDto;
                }).collect(Collectors.toList());
        return snsPosts;
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
    /**
     * 닉네임으로 sns 게시글 조회
     * 프로필에서 자기가 게시한 모든 게시글 확인
     * @param : String createdBy(닉네임)
     */

    public List<SnsPostResponseDto> getSnsPostByCreated(String createdBy) {
        List<SnsPost> snsPostList = snsPostRepository.findAllByCreatedBy(createdBy);
        List<SnsPostResponseDto> snsPosts = snsPostList.stream().map(snsPost -> {
            SnsPostResponseDto snsPostResponseDto = SnsPostResponseDto.builder()
                    .id(snsPost.getId())
                    .snsPostTitle(snsPost.getSnsPostTitle())
                    .snsPostContent(snsPost.getSnsPostContent())
                    .createdBy(snsPost.getCreatedBy())
                    .snsLikesCount(snsPost.getSnsLikesCount())
                    .snsViewsCount(snsPost.getSnsViewsCount())
                    .createdAt(snsPost.getCreatedAt())
                    .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                    .commentCount(snsCommentService.findCommentListByPostId(snsPost.getId()).stream().count())
                    .build();
            snsPostResponseDto.imageUrls(snsPost);
            return snsPostResponseDto;
        }).collect(Collectors.toList());

        return snsPosts;
    }

    /**
     * 게시글 좋아요 증가 메서드
     * redis의 분산락을 통한 동시성 제어
     * 실제락을 거는 시점은 퍼사드 패턴으로 분리하여
     * @Transactional 바깥에서 락을 걸어줌
     * @param : Long id(게시글 번호)
     */
    @Transactional
    public void updateSnsLikesCountUseRedisson(Long id) {
        String name = Thread.currentThread().getName();
        SnsPost snsPost = snsPostRepository.findById(id).orElseThrow(ErrorCode::throwSnsPostNotFound);
        log.info("현재 좋아요수 : {} & 현재 진행중인 스레드: {}", snsPost.getSnsLikesCount(), name);
        snsPost.likesCountUp();
    }
}
