package Plant.PlantProject.domain.Entity;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Keyword {
    @Id
    @GeneratedValue   //jpa 어노테이션인데 그냥 기본키 어노테이션으로 알고있으면됨
    private Long id;  //고유번호
    @CreatedDate
    private LocalDateTime createdAt;
    @LastModifiedDate
    private LocalDateTime updatedAt;

    private String keywordContent;

    private Integer memberNo;
    @Builder
    public Keyword(String keywordContent, Integer memberNo) {
        this.keywordContent = keywordContent;
        this.memberNo = memberNo;
        this.createdAt = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();


    }
}
