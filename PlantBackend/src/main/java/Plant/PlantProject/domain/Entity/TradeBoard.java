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
import java.time.ZoneId;
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

    @OneToMany(mappedBy = "tradeBoard", cascade = CascadeType.REMOVE)
    List<Image> imageList = new ArrayList<Image>();

    @Column(columnDefinition = "integer default 0")
    private Integer view;
    @Column(columnDefinition = "integer default 0")
    private Integer goodCount;

    private String buyer;
    private String keywordContent;

    @Builder
    public TradeBoard(String createBy, Member member, String title, String content, Integer price, String keywordContent) {
        this.createBy = createBy;
        this.member = member;
        this.title = title;
        this.content = content;
        this.price = price;
        this.view = 0;
        this.goodCount = 0;
        this.status = Status.판매중;
        this.keywordContent = keywordContent;
    }

    public void updatePost(String title, String content, Integer price) {
        this.title = title;
        this.content = content;
        this.price = price;
    }
   /**
    * 거래 완료 메서드
    */
    public void updateBuyer(String buyer, Status status) {
        this.buyer = buyer;
        this.status = status;
    }


    /**
     * 양방향 연관관계 메서드
     */
    public void addImageList(Image image) {
        this.imageList.add(image);
        image.add(this);
    }
    public void increaseGoodsCount() {
        this.goodCount++;
    }public void decreaseGoodsCount() {
        this.goodCount--;
    }
    public void viewsCountUp() {
        this.view++;
    }
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
        this.updatedAt = LocalDateTime.now(ZoneId.of("Asia/Seoul"));
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
