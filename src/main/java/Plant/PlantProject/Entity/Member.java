package Plant.PlantProject.Entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;


@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue   //jpa 어노테이션인데 그냥 기본키 어노테이션으로 알고있으면됨
    @Column(name = "MEMBER_ID")
    private Long id;  //고유번호
    private String name; //회원 이름
    private String userId; ///유저 아이디 V
    private String password; //비번 V
    private String email; //email]
    private String nickname; // 닉네임 V

    private String phoneNumber;

    @CreatedDate
    private LocalDateTime joinDate; //가입일자

    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;

    @Builder
    public Member(String name, String userId, String nickname, String password, String email) {
        this.name = name;
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
    }

}
