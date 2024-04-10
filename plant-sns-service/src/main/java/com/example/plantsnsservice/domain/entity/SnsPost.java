package com.example.plantsnsservice.domain.entity;

import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.time.LocalDateTime;


/**
 * mysql에 저장할 sns entity
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 */
@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sns_post_id")
    private Long id;
    @Column(name="sns_post_title", nullable = false)
    private String snsPostTitle;
    @Column(name="sns_post_content", nullable = false)
    private String snsPostContent;
    @Column(name="member_no")
    private Long memberNo;
    @Column(name="sns_likes_count")
    private Integer snsLikesCount;
    @Column(name="sns_views_count")
    private Integer snsViewsCount;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Builder
    public SnsPost(String snsPostTitle, String snsPostContent, Long memberNo) {
        this.snsPostTitle = snsPostTitle;
        this.snsPostContent = snsPostContent;
        this.memberNo = memberNo;
    }

    public void updateSnsPost(SnsPostRequestDto snsPostRequestDto) {
        this.snsPostTitle = snsPostRequestDto.getSnsPostTitle();
        this.snsPostContent = snsPostRequestDto.getSnsPostContent();
    }
}
