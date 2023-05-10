package Plant.PlantProject.Entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/*
 작성자 : 이민우
 작성 일자: 02.18
 특이 사항: 프로필 이미지 빼고 작성, OnetoMany 미작성
*/
@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue   //jpa 어노테이션인데 그냥 기본키 어노테이션으로 알고있으면됨
    @Column(name = "member_id")
    private Long id;  //고유번호
    private String name; //회원 이름
    private String userId; ///유저 아이디 V
    @Setter
    private String password; //비번 V
    private String email; //email
    private String nickname; // 닉네임 V

    private String phoneNumber;
    @Enumerated(EnumType.STRING)
    @Setter
    private Role role;


    @CreatedDate
    private LocalDateTime joinDate; //가입일자

    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;
    @OneToMany(mappedBy = "member")
    List<TradeBoard> tradeBoardList = new ArrayList<TradeBoard>();
    @OneToMany(mappedBy = "member")
    List<InfoBoard> infoBoardList = new ArrayList<InfoBoard>();
    @OneToMany(mappedBy = "member")
    List<Goods> goodsList = new ArrayList<Goods>();
    @OneToMany(mappedBy = "member")
    List<KeyWord> keyWordList = new ArrayList<KeyWord>();

    @Builder
    public Member(String name, String userId, String nickname, String password, String email, SocialLogin socialLogin) {
        this.name = name;
        this.userId = userId;
        this.nickname = nickname;
        this.password = password;
        this.email = email;
        this.socialLogin = socialLogin;
    }

    public Member(String email, String nickname, SocialLogin socialLogin) {
        this.email = email;
        this.nickname = nickname;
        this.socialLogin = socialLogin;
    }
    @PrePersist
    protected void onCreate() {
        this.joinDate = LocalDateTime.now();

    }
}
