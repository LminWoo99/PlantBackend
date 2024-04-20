package com.example.plantsnsservice.vo.response;

import com.example.plantsnsservice.domain.entity.SnsComment;
import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
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
    private Long parentId;

    private List<SnsCommentResponseDto> children = new ArrayList<>();

    private LocalDateTime createdAt;

    public SnsCommentResponseDto(Long id, Long snsPostId, String content, String createdBy, Long parentId, LocalDateTime createdAt) {
        this.id = id;
        this.snsPostId = snsPostId;
        this.content = content;
        this.createdBy = createdBy;
        this.parentId = parentId;
        this.createdAt = createdAt;
    }

    public static SnsCommentResponseDto convertCommentToDto(SnsComment snsComment) {
        return new SnsCommentResponseDto(snsComment.getId(), snsComment.getSnsPost().getId(), snsComment.getContent(), snsComment.getCreatedBy(), (snsComment.getParent() == null ? null : snsComment.getParent().getId()), snsComment.getCreatedAt());
    }
}
