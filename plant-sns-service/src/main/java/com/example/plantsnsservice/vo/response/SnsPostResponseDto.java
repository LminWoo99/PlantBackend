package com.example.plantsnsservice.vo.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import java.security.Principal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SnsPostResponseDto {
    private Long id;
    private String snsPostTitle;
    private String snsPostContent;
    private Long memberNo;
    private LocalDateTime createdAt;
    private Integer snsLikesCount;
    private Integer snsViewsCount;


}
