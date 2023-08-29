package Plant.PlantProject.Entity;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member{


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String userId;
    private String nickname;

    private String username;
    private String password;
    private String email;
    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> role = new ArrayList<>();

    private String refreshToken;



    public Member(String email,String password, String nickname, SocialLogin socialLogin,String username) {
        this.password= password;
        this.nickname = nickname;
        this.email = email;
        this.socialLogin = socialLogin;
        this.username = username;
    }

    public void setSocialLogin(SocialLogin socialLogin) {
        this.socialLogin = socialLogin;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Member(String username, String password) {
        this.username = username;
        this.password = password;
    }


}
