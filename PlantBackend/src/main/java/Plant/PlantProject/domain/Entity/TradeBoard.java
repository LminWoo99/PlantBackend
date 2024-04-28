package Plant.PlantProject.domain.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;


@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class TradeBoard {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;
    private String createBy;

    private Integer price;

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

    //    @OneToMany(mappedBy = "tradeBoardId", orphanRemoval = true)
    //    List<Comment> commentList = new ArrayList<Comment>();
    //    @OneToMany(mappedBy = "tradeBoard", orphanRemoval = true)
    //    List<Goods> goodsList = new ArrayList<>();
    //
    //    @OneToMany(mappedBy = "tradeBoard", orphanRemoval = true)
    //    List<Image> imageList = new ArrayList<Image>();
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer view;
    @Column(columnDefinition = "integer default 0", nullable = false)
    private Integer goodCount;

    private String buyer;
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

    public void updatePost(String title, String content, Integer price) {
        this.title = title;
        this.content = content;
        this.price = price;
    }
   /**
    * 거래 완료 메서드
    */
    public void updateStatus(Status status) {
        this.status = status;
    }
    public static TradeBoard createTradeBoard(Member member, String title, String content,
                                              String createBy, int view, int price, int goodCount, String buyer){
        TradeBoard tradeBoard = new TradeBoard();
        tradeBoard.member=member;
        tradeBoard.title=title;
        tradeBoard.content = content;
        tradeBoard.createBy = createBy;
        tradeBoard.view=view;
        tradeBoard.price = price;
        tradeBoard.status=Status.판매중;
        tradeBoard.goodCount = goodCount;
        tradeBoard.buyer = buyer;
        return tradeBoard;
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
