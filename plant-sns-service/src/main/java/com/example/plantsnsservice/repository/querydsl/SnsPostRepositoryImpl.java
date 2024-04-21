package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.domain.entity.SnsPost;
import com.example.plantsnsservice.vo.SearchParam;
import com.example.plantsnsservice.vo.response.SnsPostResponseDto;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Map;

import static com.example.plantsnsservice.domain.entity.QHashTag.hashTag;
import static com.example.plantsnsservice.domain.entity.QSnsHashTagMap.snsHashTagMap;
import static com.example.plantsnsservice.domain.entity.QSnsPost.snsPost;
import static com.example.plantsnsservice.vo.SearchParam.*;

@RequiredArgsConstructor
public class SnsPostRepositoryImpl implements CustomSnsPostRepository{
    private final JPAQueryFactory jpaQueryFactory;
    @Override
    public List<SnsPostResponseDto> findAllByHashTag(String hashTagName) {
        return jpaQueryFactory.select(Projections.constructor(SnsPostResponseDto.class,
                        snsPost.id,
                        snsPost.snsPostTitle,
                        snsPost.snsPostContent,
                        snsPost.createdBy,
                        snsPost.createdAt,
                        snsPost.snsLikesCount,
                        snsPost.snsViewsCount
                        ))
                .from(snsPost)
                .join(snsHashTagMap.snsPost, snsPost)
                .join(snsHashTagMap.hashTag, hashTag)
                .where(hashTag.name.eq(hashTagName))
                .fetch();
    }
    @Override
    public List<SnsPost> findAllByCreatedBy(String createdBy) {
        return jpaQueryFactory.selectFrom(snsPost)
                .where(snsPost.createdBy.eq(createdBy))
                .fetch();

    }
    @Override
    public List<SnsPost> search(final Map<String, String> searchCondition) {
        return jpaQueryFactory
                .selectFrom(snsPost)
                .where(allCond(searchCondition))
                .fetch();
    }

    // BooleanBuilder
    private BooleanBuilder allCond(Map<String, String> searchCondition) {
        BooleanBuilder builder = new BooleanBuilder();

        return builder
                .and(snsPostTileLike(searchCondition.getOrDefault(SNSPOSTTITLE.getParamKey(), null)))
                .and(hashTagNameEq(searchCondition.getOrDefault(HASHTAGNAME.getParamKey(), null)))
                .and(snsPostContentLike(searchCondition.getOrDefault(SNSPOSTCONTENT.getParamKey(), null)))
                .and(nicknameEq(searchCondition.getOrDefault(NICKNAME.getParamKey(), null)));
    }
    // 조건1
    private BooleanExpression snsPostTileLike(String snsPostTitle) {
        return StringUtils.hasText(snsPostTitle) ? snsPost.snsPostTitle.contains(snsPostTitle) : null;
    }
    // 조건2
    private BooleanExpression hashTagNameEq(String hashTagName) {
        return StringUtils.hasText(hashTagName) ? snsHashTagMap.hashTag.name.eq(hashTagName) : null;
    }
    // 조건3
    private BooleanExpression snsPostContentLike(String snsPostContent) {
        return StringUtils.hasText(snsPostContent) ? snsPost.snsPostContent.contains(snsPostContent) : null;
    }
    // 조건4
    private BooleanExpression nicknameEq(String nickname) {
        return StringUtils.hasText(nickname) ? snsPost.createdBy.eq(nickname) : null;
    }

}
