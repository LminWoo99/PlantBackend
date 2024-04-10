package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.vo.response.SnsPostResponseDto;

import java.util.List;

public interface CustomSnsPostRepository {
    public List<SnsPostResponseDto> findAllByHashTag(String hashTagName);
}
