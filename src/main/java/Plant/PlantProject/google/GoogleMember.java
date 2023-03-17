package Plant.PlantProject.google;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class GoogleMember {
    @Id
    @GeneratedValue
    @Column(name = "GOOGLE_ID")
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String email;

    @Builder
    public GoogleMember(String name, String email) {
        this.name = name;
        this.email = email;
    }

}
