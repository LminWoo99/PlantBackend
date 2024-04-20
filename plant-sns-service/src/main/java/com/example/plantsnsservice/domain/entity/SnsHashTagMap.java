package com.example.plantsnsservice.domain.entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
/**
 * mysql에 저장할 hashtagmap entity
 * 다대다 관계를 사용하지 않기 위한 mapping 테이블
 * 다대다 관계는 조인 테이블 활용 불가, 성능 이슈, 객체 지향 x
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "sns_hash_tag_map")
public class SnsHashTagMap {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "sns_hash_tag_map_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sns_post_id")
    private SnsPost snsPost;

    @ManyToOne(fetch = FetchType.LAZY )
    @JoinColumn(name = "hash_tag_id")
    private HashTag hashTag;
    @Builder
    public SnsHashTagMap(SnsPost snsPost, HashTag hashTag) {
        this.snsPost = snsPost;
        this.hashTag = hashTag;
    }
}
