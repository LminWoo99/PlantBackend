package Plant.PlantProject.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 작성자 : 이민우
 작성 일자: 02.18
 내용 : 거래 게시글 엔티티 코드
 특이 사항: 사진, 카테고리 빼고 작성
*/
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeBoard {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String createBy;

    private String title;

    private String content;
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;
    @OneToMany(mappedBy = "tradeBoard")
    List<KeyWord> keyWordList = new ArrayList<KeyWord>();
    @OneToMany(mappedBy = "tradeBoard")
    List<Goods> goodsList = new ArrayList<Goods>();

    public TradeBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    @Builder
    public TradeBoard(Long id, String createBy, Member member, String title, String content, Status status) {
        this.id = id;
        this.createBy = createBy;
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
