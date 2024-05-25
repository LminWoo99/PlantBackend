package com.example.plantsnsservice.vo.response;

import com.example.plantsnsservice.domain.entity.Image;
import com.example.plantsnsservice.domain.entity.SnsPost;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsPostResponseDto {
    private Long id;
    private Integer memberNo;
    private String snsPostTitle;
    private String snsPostContent;
    private String createdBy;
    private LocalDateTime createdAt;
    private Integer snsLikesCount;
    private Integer snsViewsCount;
    //이미 입력 요청에서 set로 중복 방지하므로 List 사용
    private List<String> hashTags;
    private List<String> imageUrls;
    private Long commentCount;

    private boolean snsLikesStatus;

    public SnsPostResponseDto(Long id, Integer memberNo, String snsPostTitle, String snsPostContent, String createdBy, LocalDateTime createdAt, Integer snsLikesCount, Integer snsViewsCount, List<String> hashTags, List<String> imageUrls) {
        this.id = id;
        this.memberNo = memberNo;
        this.snsPostTitle = snsPostTitle;
        this.snsPostContent = snsPostContent;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
        this.snsLikesCount = snsLikesCount;
        this.snsViewsCount = snsViewsCount;
        this.hashTags = hashTags;
        this.imageUrls = imageUrls;
    }

}