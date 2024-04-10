package com.example.plantsnsservice.vo.response;

import com.example.plantsnsservice.domain.entity.SnsComment;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsCommentResponseDto {
    private Long id;
    private Long snsPostId;
    @NotNull
    private String content;
    @NotNull
    private String createdBy;
    private SnsComment parent;

    private List<SnsCommentResponseDto> children = new ArrayList<>();

    public SnsCommentResponseDto(Long id, Long snsPostId, String content, String createdBy, SnsComment parent) {
        this.id = id;
        this.snsPostId = snsPostId;
        this.content = content;
        this.createdBy = createdBy;
        this.parent = parent;
    }

    public static SnsCommentResponseDto convertCommentToDto(SnsComment snsComment) {
        return new SnsCommentResponseDto(snsComment.getId(), snsComment.getSnsPost().getId(), snsComment.getContent(), snsComment.getCreatedBy(), snsComment.getParent());
    }
}
