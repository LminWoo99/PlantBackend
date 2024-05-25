package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;

import java.util.List;
import java.util.Map;

public interface CustomSnsPostRepository {
    List<SnsPostResponseDto> findAllByOrderByCreatedAtDesc();
    List<SnsPostResponseDto> findAllByCreatedBy(String createdBy);

    List<SnsPostResponseDto> search(final Map<String, String> searchCondition);


    public List<SnsPostResponseDto> findTopPostsByWeek();

    List<SnsPostResponseDto> findTopPostsByMonth();

    SnsPostResponseDto getSnsPostById(Long snsPostId);
}
