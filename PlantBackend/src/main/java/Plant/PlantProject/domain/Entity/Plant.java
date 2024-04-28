package Plant.PlantProject.domain.Entity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Plant {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    @Comment("컨텐츠번호")
    private String contentNum;
    @Column
    @Comment("식물이름")
    private String plantName;
    @Column
    @Comment("식물 정보_조언")
    private String plantInfo;
    @Column
    @Comment("높이")
    private String height;
    @Column
    @Comment("번식시기")
    private String breed;
    @Column
    @Comment("관리")
    private String manage;
    @Column
    @Comment("생장속도")
    private String growthRate;
    @Column
    @Comment("생육온도")
    private String growthTmp;
    @Column
    @Comment("비료")
    private String fertilizer;
    @Column
    @Comment("물주기_봄")
    private String plantSpring;
    @Column
    @Comment("물주기_여름")
    private String plantSummner;
    @Column
    @Comment("물주기_가을")
    private String plantAutumn;
    @Column
    @Comment("물주기_겨울")
    private String plantWinter;
    @Column
    @Comment("분류")
    private String category;
    @Column
    @Comment("발화계절")
    private String season;
    @Column
    @Comment("생육형태")
    private String growth;
    @Column
    @Comment("이미지")
    private String image;
    @Column
    @Comment("썸네일")
    private String thumbFile;
    @Column
    @Comment("습도")
    private String humidity;
    @Column
    @Comment("빛")
    private String light;
    @Column
    @Comment("특별정보")
    private String special;

    public Plant(String contentNum, String plantName, String plantInfo, String height, String breed, String manage, String growthRate, String growthTmp, String fertilizer, String plantSpring, String plantSummner, String plantAutumn, String plantWinter, String category, String season, String growth) {
        this.contentNum = contentNum;
        this.plantName = plantName;
        this.plantInfo = plantInfo;
        this.height = height;
        this.breed = breed;
        this.manage = manage;
        this.growthRate = growthRate;
        this.growthTmp = growthTmp;
        this.fertilizer = fertilizer;
        this.plantSpring = plantSpring;
        this.plantSummner = plantSummner;
        this.plantAutumn = plantAutumn;
        this.plantWinter = plantWinter;
        this.category = category;
        this.season = season;
        this.growth = growth;
        this.image = image;
        this.thumbFile = thumbFile;
    }

    public void setHumidity(String humidity) {
        this.humidity = humidity;
    }

    public void setLight(String light) {
        this.light = light;
    }

    public void setSpecial(String special) {
        this.special = special;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setThumbFile(String thumbFile) {
        this.thumbFile = thumbFile;
    }
}