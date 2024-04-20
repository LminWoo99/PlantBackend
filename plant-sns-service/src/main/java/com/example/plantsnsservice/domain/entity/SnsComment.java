package com.example.plantsnsservice.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * mysql에 저장할 comment entity
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class SnsComment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="sns_comment_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;
    @Column(name="sns_comment_content")
    private String content;
    @Column(name="sns_comment_created_by")
    private String createdBy;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private SnsComment parent;

    @OneToMany(mappedBy = "parent", orphanRemoval = true)
    private List<SnsComment> children = new ArrayList<>();

    private LocalDateTime createdAt;
    @Builder
    public SnsComment(SnsPost snsPost, String content, String createdBy, SnsComment parent) {
        this.snsPost = snsPost;
        this.content = content;
        this.createdBy = createdBy;
        this.parent = parent;
        this.createdAt = LocalDateTime.now();
    }
}
