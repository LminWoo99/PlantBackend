package com.example.plantsnsservice.repository.querydsl;


import com.example.plantsnsservice.domain.entity.SnsComment;
import com.example.plantsnsservice.domain.entity.SnsPost;

import java.util.List;

public interface CustomSnsCommentRepository {

    public List<SnsComment> findSnsCommentByPostId(Long postId);

    public void deleteBySnsPostId(Long postId);

}
