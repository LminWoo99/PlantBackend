package Plant.PlantProject.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

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

    private int price;

    private String title;

    private String content;
    @CreatedDate
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime createdAt;
    @LastModifiedDate
    @DateTimeFormat(pattern = "yyyy-mm-dd")
    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    private Status status;

    @OneToMany(mappedBy = "tradeBoard")
    List<KeyWord> keyWordList = new ArrayList<KeyWord>();
    @OneToMany(mappedBy = "tradeBoardId", orphanRemoval = true)
    List<Comment> commentList = new ArrayList<Comment>();


    @OneToMany(mappedBy = "tradeBoard", orphanRemoval = true)
    List<Image> imageList = new ArrayList<Image>();
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int view;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private int goodCount;

    public TradeBoard(String title, String content) {
        this.title = title;
        this.content = content;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public static TradeBoard createTradeBoard(Member member, String title, String content,
                                              String createBy, int view, int price, int goodCount){
        TradeBoard tradeBoard = new TradeBoard();
        tradeBoard.member=member;
        tradeBoard.title=title;
        tradeBoard.content = content;
        tradeBoard.createBy = createBy;
        tradeBoard.view=view;
        tradeBoard.price = price;
        tradeBoard.status=Status.판매중;
        tradeBoard.goodCount = goodCount;

        return tradeBoard;
    }
    @Builder
    public TradeBoard(Long id, String createBy, Member member, String title, String content, Status status, int view) {
        this.id = id;
        this.createBy = createBy;
        this.member = member;
        this.title = title;
        this.content = content;
        this.status = status;
        this.view= view;
    }
    public void increaseGoodsCount() {
        this.goodCount++;
    }public void decreaseGoodsCount() {
        this.goodCount--;
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
