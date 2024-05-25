package com.example.plantsnsservice.service;

import com.example.plantsnsservice.domain.entity.HashTag;
import com.example.plantsnsservice.domain.entity.SnsHashTagMap;
import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.repository.SnsHashTagMapRepository;
import com.example.plantsnsservice.vo.response.SnsHashResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SnsHashTagMapService {
    private final HashTagService hashTagService;
    private final SnsHashTagMapRepository snsHashTagMapRepository;
    /**
     * sns 게시글 생성시 해시 태그 생성 메서드
     * 게시글 생성시 해당 메서드 호출하여 생성
     * 요청으로 들어온 해시태그가 없으면 해시태그 새로 저장
     * @param : SnsPost snsPost, Set<String> hashTags
     */
    @Transactional
    public void createHashTag(SnsPost snsPost, Set<String> hashTags) {
        if (hashTags.size() == 0) return;
        hashTags.stream()
                .map(hashtag ->
                        hashTagService.findByName(hashtag)
                                .orElseGet(() -> hashTagService.save(hashtag)))
                .forEach(hashtag -> mapHashtagToSnsPost(snsPost, hashtag));
    }
    private Long mapHashtagToSnsPost(SnsPost snsPost, HashTag hashTag) {
        return snsHashTagMapRepository.save(new SnsHashTagMap(snsPost, hashTag)).getId();
    }
    @Transactional
    public void deleteSnsHashTagMap(Long snsPostId) {
        snsHashTagMapRepository.deleteBySnsPostId(snsPostId);


    }
    /**
     * sns 게시글에 가장 많이 사용된 해시태그 10개조회
     */
    @Transactional(readOnly = true)
    public List<SnsHashResponseDto> findTop10SnsHashTag() {
        //가장 많이 사용된 해시태그 10개 정렬
        PageRequest pageRequest = PageRequest.of(0, 10);
        Slice<HashTag> hashTagSlice = snsHashTagMapRepository.findTop10SnsHashTag(pageRequest);

        return hashTagSlice.getContent().stream().map(hashTag -> SnsHashResponseDto.builder()
                .id(hashTag.getId())
                .name(hashTag.getName())
                .build()).collect(Collectors.toList());


    }
}
