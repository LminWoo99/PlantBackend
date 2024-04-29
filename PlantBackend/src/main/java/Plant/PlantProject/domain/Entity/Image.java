package Plant.PlantProject.domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Image{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "IMAGE_ID")
    private Long id;

    @Column(name = "IMAGE_NAME")
    private String name;

    @Column(name = "IMAGE_URL")
    private String url;

    @Column(name = "IS_REMOVED")
    private boolean removed;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tradeBoard_Id")
    @JsonIgnore
    private TradeBoard tradeBoard;

    @Builder
    public Image(String name, String url, TradeBoard tradeBoard) {
        this.name = name;
        this.url = url;
        this.tradeBoard = tradeBoard;
    }

    public void add(TradeBoard tradeBoard) {
        this.tradeBoard = tradeBoard;
    }
}