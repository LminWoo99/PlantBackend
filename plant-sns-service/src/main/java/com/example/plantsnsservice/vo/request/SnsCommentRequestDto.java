package com.example.plantsnsservice.vo.request;

import com.example.plantsnsservice.domain.entity.SnsComment;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SnsCommentRequestDto {
    @NotNull
    private Long snsPostId;
    @NotNull
    private String content;
    @NotNull
    private String createdBy;
    private Long parentId;
    private List<SnsComment> children = new ArrayList<>();

}
