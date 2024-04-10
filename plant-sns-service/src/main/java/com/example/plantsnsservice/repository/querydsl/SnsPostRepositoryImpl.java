package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.plantsnsservice.domain.entity.QHashTag.hashTag;
import static com.example.plantsnsservice.domain.entity.QSnsHashTagMap.snsHashTagMap;
import static com.example.plantsnsservice.domain.entity.QSnsPost.snsPost;

@RequiredArgsConstructor
public class SnsPostRepositoryImpl implements CustomSnsPostRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<SnsPostResponseDto> findAllByHashTag(String hashTagName) {
        return jpaQueryFactory.select(Projections.constructor(SnsPostResponseDto.class,
                        snsPost.id,
                        snsPost.snsPostTitle,
                        snsPost.snsPostContent,
                        snsPost.memberNo,
                        snsPost.snsLikesCount,
                        snsPost.snsViewsCount,
                        snsPost.createdAt))
                .from(snsPost)
                .join(snsHashTagMap.snsPost, snsPost)
                .join(snsHashTagMap.hashTag, hashTag)
                .where(hashTag.name.eq(hashTagName))
                .fetch();
    }
}
