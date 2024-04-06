package com.example.plantsnsservice.domain;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

/**
 * mysql에 저장할 hashtag entity
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 */
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)

public class HashTag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="hash_tag_id")
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;
    @Column(name="hash_tag_name")
    private String name;
}
