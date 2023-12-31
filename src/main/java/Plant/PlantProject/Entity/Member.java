package Plant.PlantProject.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Member{


    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String userId;
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
    @ManyToMany(fetch = FetchType.LAZY)
    private Collection<Role> role = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<TradeBoard> tradeBoard = new ArrayList<>();

    private String refreshToken;

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Member(String email, String password, String nickname, SocialLogin socialLogin, String username) {
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
