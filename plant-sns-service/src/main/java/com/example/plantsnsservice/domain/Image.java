package com.example.plantsnsservice.domain;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * mysql에 저장할 image entity
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="image_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;
    @Column(name = "image_name")
    private String name;
    @Column(name = "image_url")
    private String url;
    @Builder
    public Image(SnsPost snsPost, String name, String url) {
        this.snsPost = snsPost;
        this.name = name;
        this.url = url;
    }
}
