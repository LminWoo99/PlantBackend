package com.example.plantsnsservice.domain.entity;

import com.example.plantsnsservice.vo.request.SnsPostRequestDto;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;


/**
 * mysql에 저장할 sns entity
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 */
@Entity
@Getter
@Slf4j
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsPost {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sns_post_id")
    private Long id;
    @Column(name="sns_post_title")
    private String snsPostTitle;
    @Column(name="sns_post_content", nullable = false)
    private String snsPostContent;
    @Column(name="memebr_no")
    private Integer memberNo;
    @Column(name="created_by")
    private String createdBy;
    //image와 양방향 관계로 변경
    @OneToMany(mappedBy = "snsPost", cascade = CascadeType.REMOVE)
    private List<Image> imageList = new ArrayList<>();

    @Column(name="sns_likes_count", columnDefinition = "integer default 0", nullable = false)
    private Integer snsLikesCount;
    @Column(name="sns_views_count", columnDefinition = "integer default 0", nullable = false)
    private Integer snsViewsCount;
    @Column(name="created_at")
    private LocalDateTime createdAt;

    @Builder
    public SnsPost(String snsPostTitle, String snsPostContent, Integer memberNo, String createdBy, Integer snsLikesCount, Integer snsViewsCount) {
        this.snsPostTitle = snsPostTitle;
        this.snsPostContent = snsPostContent;
        this.memberNo = memberNo;
        this.createdBy = createdBy;
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.snsLikesCount = snsLikesCount;
        this.snsViewsCount = snsViewsCount;
    }

    public void updateSnsPost(SnsPostRequestDto snsPostRequestDto) {
        this.snsPostTitle = snsPostRequestDto.getSnsPostTitle();
        this.snsPostContent = snsPostRequestDto.getSnsPostContent();
    }
    /**
     * 양방향 연관관계 메서드
     */
    public void addImageList(Image image) {
        this.imageList.add(image);
        image.add(this);
    }
    public void viewsCountUp() {
        this.snsViewsCount++;
    }
    public void likesCountUp() {
        this.snsLikesCount++;

    }
    public void likesCountDown() {
        if (this.snsLikesCount>0){
            this.snsLikesCount--;
        }
    }
}
