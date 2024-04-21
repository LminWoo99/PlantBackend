package com.example.plantsnsservice.service;

import com.example.plantsnsservice.common.exception.CustomException;
import com.example.plantsnsservice.common.exception.ErrorCode;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.querydsl.SnsPostRepository;
import com.example.plantsnsservice.repository.redis.SnsLikesCountRepository;
import com.example.plantsnsservice.service.image.ImageService;
import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class SnsPostService {
    private final SnsPostRepository snsPostRepository;
    private final SnsHashTagMapService snsHashTagMapService;
    private final SnsCommentService snsCommentService;
    private final ImageService imageService;
    private final SnsLikesCountRepository snsLikesCountRepository;
    /**
     * sns 게시글 생성
     * 게시글 형식은 게시글 문구, 해시태그, 게시글 내용
     *
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public Long createPost(SnsPostRequestDto snsPostRequestDto, List<MultipartFile> files) throws IOException {
        SnsPost snsPost = SnsPost.builder()
                .snsPostTitle(snsPostRequestDto.getSnsPostTitle())
                .snsPostContent(snsPostRequestDto.getSnsPostContent())
                .memberNo(snsPostRequestDto.getMemberNo())
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
     * 조회용이지만 조회수증가를 위해 readonly 제거
     * @param : Long snsPostId(게시글 번호)
     */
    @Transactional
    public SnsPostResponseDto findById(Long snsPostId) {
        SnsPost snsPost = snsPostRepository.findById(snsPostId).orElseThrow(ErrorCode::throwSnsPostNotFound);
        //글 자세히 볼때 조회수 증가(따로 중복 제거는 하지 않음)
        snsPost.viewsCountUp();

        String key = "sns_likes:" + snsPost.getId();
        SnsPostResponseDto snsPostResponseDto = SnsPostResponseDto.builder()
                .id(snsPost.getId())
                .snsPostTitle(snsPost.getSnsPostTitle())
                .snsPostContent(snsPost.getSnsPostContent())
                .createdBy(snsPost.getCreatedBy())
                .createdAt(snsPost.getCreatedAt())
                .snsLikesCount(snsPost.getSnsLikesCount())
                .snsViewsCount(snsPost.getSnsViewsCount())
                .snsLikesStatus(snsLikesCountRepository.isMember(key, snsPost.getMemberNo()) ? true : false)
                .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                .build();

        snsPostResponseDto.imageUrls(snsPost);

        return snsPostResponseDto;
    }
    /**
     * sns 게시글 조건에 따라 검섹(querydsl 동적 쿼리 사용)
     * 사용자는 Optional(해시태그(완전일치), 글 본문 내용(포함), 글 제목(포힘), 닉네임(완전 일치))으로 검색 가능
     * @param : Map<String, String> searchCondition(검색 조건)
     */
    public List<SnsPostResponseDto> getSnsPostListByCondition(Map<String, String> searchCondition) {
        List<SnsPost> snsPostList = snsPostRepository.search(searchCondition);
        return getCollect(snsPostList);

    }

    /**
     * sns 게시글 수정
     * 게시글 수정은  내용, 해시태그 가능(게시글 내용은 직접 update 쿼리 대신 변경 감지로)
     *
     * @param : SnsPostRequestDto snsPostRequestDto
     */
    @Transactional
    public void updateSnsPost(SnsPostRequestDto snsPostRequestDto) {
        SnsPost snsPost = snsPostRepository.findById(snsPostRequestDto.getId()).orElseThrow(ErrorCode::throwSnsPostNotFound);

        //변경 감지
        snsPost.updateSnsPost(snsPostRequestDto);

        //해시 태그 삭제 후 업데이트(조인해서 업데이트하는것보다 성능 상 우위)
        snsHashTagMapService.deleteSnsHashTagMap(snsPost.getId());
        snsHashTagMapService.createHashTag(snsPost, snsPostRequestDto.getHashTags());

    }

    /**
     * sns 게시글 조회
     * 게시글 수정은 문구랑 내용만 가능
     * 게시글은 시간순으로 정렬
     *
     * @param : x
     */
    @Transactional(readOnly = true)
    public List<SnsPostResponseDto> getSnsPostList() {
        List<SnsPost> snsPostList = snsPostRepository.findAllByOrderByCreatedAtDesc();
        return getCollect(snsPostList);
    }

    /**
     * 해시태그 기준으로 sns 게시글 조회
     * 해시태그 없을 경우 예외 처리
     *
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
     *
     * @param : Long snsPostId(게시글 번호)
     */
    @Transactional
    public void deleteSnsPost(Long snsPostId) {
        //단방향관계를 유지하기 위 댓글 먼제 삭제
        snsCommentService.deleteSnsCommentBySnsPost(snsPostId);

        snsHashTagMapService.deleteSnsHashTagMap(snsPostId);
        snsPostRepository.deleteById(snsPostId);
    }

    /**
     * 닉네임으로 sns 게시글 조회
     * 프로필에서 자기가 게시한 모든 게시글 확인
     *
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
     * 락의 범위가 트랜잭션 범위보다 크게
     * 좋아요 중복관리는 redis set 자료구조를 활용
     * @param : Long snsPostId(게시글 번호), Integer memberNo
     * @Transactional 바깥에서 락을 걸어줌
     */
    @Transactional
    public void updateSnsLikesCount(Long snsPostId, Integer memberNo) {

        SnsPost snsPost = snsPostRepository.findById(snsPostId).orElseThrow(ErrorCode::throwSnsPostNotFound);
        String key = "sns_likes:" + snsPostId;
        //좋아요 중복 관리
        boolean alreadyLiked = snsLikesCountRepository.isMember(key,memberNo);

        if (alreadyLiked) {
            // 특정 게시글에 특정 유저가 좋아요 누른적이 있을시
            snsLikesCountRepository.decrement(memberNo, snsPostId);
            snsPost.likesCountDown();
        } else {
            snsLikesCountRepository.increment(memberNo, snsPostId);
            snsPost.likesCountUp();
        }

        snsPostRepository.save(snsPost);
    }
    private List<SnsPostResponseDto> getCollect(List<SnsPost> snsPostList) {
        return snsPostList.stream().map(snsPost -> {
            String key = "sns_likes:" + snsPost.getId();
            SnsPostResponseDto snsPostResponseDto = SnsPostResponseDto.builder()
                    .id(snsPost.getId())
                    .snsPostTitle(snsPost.getSnsPostTitle())
                    .snsPostContent(snsPost.getSnsPostContent())
                    .createdBy(snsPost.getCreatedBy())
                    .snsLikesCount(snsPost.getSnsLikesCount())
                    .snsViewsCount(snsPost.getSnsViewsCount())
                    .createdAt(snsPost.getCreatedAt())
                    .hashTags(snsHashTagMapService.findHashTagListBySnsPost(snsPost))
                    .snsLikesStatus(snsLikesCountRepository.isMember(key, snsPost.getMemberNo()) ? true : false)
                    .commentCount(snsCommentService.findCommentListByPostId(snsPost.getId()).stream().count())
                    .build();
            snsPostResponseDto.imageUrls(snsPost);
            return snsPostResponseDto;
        }).collect(Collectors.toList());
    }
}
