package Plant.PlantProject.domain.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@DynamicUpdate
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private String nickname;
    @NotNull
    private String username;
    @NotNull
    @JsonIgnore
    private String password;
    @NotNull
    private String email;

    @Enumerated(EnumType.STRING)
    private SocialLogin socialLogin;

    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }
    @Builder
    public Member(Long id, String email, String password, String nickname, SocialLogin socialLogin, String username) {
        this.id = id;
        this.password= password;
        this.nickname = nickname;
        this.email = email;
        this.socialLogin = socialLogin;
        this.username = username;
    }

    public void encryptPassword(String password, SocialLogin socialLogin) {
        this.password = password;
        this.socialLogin = socialLogin;
    }


}
