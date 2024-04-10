package com.example.plantsnsservice.service;

import com.example.plantsnsservice.domain.entity.HashTag;
import com.example.plantsnsservice.repository.HashTagRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HashTagService {
    private final HashTagRepository hashTagRepository;
    /**
     *  해시 태그명 기준으로 해시태그 조회 메서드
     * @param : String hashTagName(해시태그명)
     */
    public Optional<HashTag> findByName(String hashTagName) {
        return hashTagRepository.findByName(hashTagName);
    }
    /**
     * sns 게시글 생성시 해시 태그 생성 메서드
     * @param : String hashTagName(해시태그명)
     */
    public HashTag save(String hashTagName) {
        HashTag hashTag = HashTag.builder()
                .name(hashTagName)
                .build();
        return hashTagRepository.save(hashTag);
    }
}
