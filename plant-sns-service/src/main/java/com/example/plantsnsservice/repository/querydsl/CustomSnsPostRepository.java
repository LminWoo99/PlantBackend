package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;

import java.util.List;
import java.util.Map;

public interface CustomSnsPostRepository {
    List<SnsPostResponseDto> findAllByHashTag(String hashTagName);


    List<SnsPost> findAllByCreatedBy(String createdBy);

    List<SnsPost> search(final Map<String, String> searchCondition);
}
