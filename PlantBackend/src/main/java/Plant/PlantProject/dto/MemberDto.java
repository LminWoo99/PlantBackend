package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Member;
import Plant.PlantProject.repository.MemberRepository;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;

@Data
@NoArgsConstructor
public class MemberDto {

    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String email;


    @Builder
    public MemberDto(Long id, String nickname, String username, String password, String email) {
        this.id = id;
        this.nickname = nickname;
        this.username = username;
        this.password = password;
        this.email = email;
    }


    public Member toEntity() {
        return Member
                .builder()
                .id(this.id)
                .nickname(this.nickname)
                .username(this.username).password(this.password)
                .email(this.email)
                .role(new ArrayList<>())
                .build();
    }



}
