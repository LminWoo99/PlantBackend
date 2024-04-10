package com.example.plantsnsservice.repository.querydsl;

import com.example.plantsnsservice.domain.entity.SnsComment;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;

import java.util.List;

import static com.example.plantsnsservice.domain.entity.QSnsComment.snsComment;


@RequiredArgsConstructor
public class SnsCommentRepositoryImpl implements CustomSnsCommentRepository {
    private final JPAQueryFactory jpaQueryFactory;


    //댓글 및 대댓글 조회 , n+1 문제 방지, 부모 댓글의 ID(parent.id) 기준으로 오름차순으로 정렬하며, 부모 댓글이 없는 경우(nulls)는 리스트의 가장 앞에 위치
    @Override
    public List<SnsComment> findSnsCommentByPostId(Long postId) {
        return jpaQueryFactory.selectFrom(snsComment)
                .leftJoin(snsComment.parent)
                .fetchJoin()
                .where(snsComment.snsPost.id.eq(postId))
                .orderBy(
                        snsComment.parent.id.asc().nullsFirst()
                ).fetch();
    }



}
