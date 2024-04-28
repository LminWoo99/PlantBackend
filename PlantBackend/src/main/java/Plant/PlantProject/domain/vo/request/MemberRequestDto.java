package Plant.PlantProject.domain.vo.request;

import Plant.PlantProject.domain.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberRequestDto {

    private String nickname;
    private String username;
    private String password;
    private String email;


    public Member toEntity() {
        return Member.builder()
                .nickname(this.nickname)
                .username(this.username).password(this.password)
                .email(this.email)
                .build();
    }



}
