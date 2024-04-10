package com.example.plantsnsservice.service;

import com.example.plantsnsservice.domain.entity.HashTag;
import com.example.plantsnsservice.repository.HashTagRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class HashTagServiceTest {
    @Mock
    HashTagRepository hashTagRepository;

    @InjectMocks
    HashTagService hashTagService;
    @Test
    @DisplayName("해시 태그명 기준으로 해시태그 조회 단위 테스트")
    void findByNameTest() {
        //given
        String hashTagName = "#LeeMinWoo";
        HashTag hashTag=HashTag.builder()
                .name(hashTagName)
                .build();

        when(hashTagRepository.findByName(hashTagName)).thenReturn(Optional.of(hashTag));
        //when
        Optional<HashTag> byName = hashTagService.findByName(hashTagName);
        //then
        assertThat(byName.get().getName()).isEqualTo("#LeeMinWoo");
    }

    @Test
    @DisplayName("sns 게시글 생성시 해시 태그 생성")
    void saveTest() {
        //given
        String hashTagName = "#LeeMinWoo";
        HashTag hashTag=HashTag.builder()
                .name(hashTagName)
                .build();
        when(hashTagRepository.save(any())).thenReturn(hashTag);

        //when
        HashTag returnHashTag = hashTagService.save(hashTagName);
        //then
        assertThat(returnHashTag.getName()).isEqualTo("#LeeMinWoo");

    }

}