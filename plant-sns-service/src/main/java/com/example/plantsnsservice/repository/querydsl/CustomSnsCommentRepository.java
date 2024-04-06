package com.example.plantsnsservice.repository.querydsl;


import com.example.plantsnsservice.domain.SnsComment;

import java.util.List;

public interface CustomSnsCommentRepository {

    public List<SnsComment> findSnsCommentByPostId(Long postId);


}
