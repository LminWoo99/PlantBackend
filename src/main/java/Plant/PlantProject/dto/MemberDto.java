package Plant.PlantProject.dto;

import Plant.PlantProject.Entity.Member;
import lombok.Builder;
import lombok.Data;

@Data
public class MemberDto {
    private Long Id;
    private String name; //회원 이름
    private String userId; ///유저 아이디 V
    private String password; //비번 V
    private String email; //email
    private String nickname; // 닉네임 V
    @Builder
    public MemberDto(String name, String userId, String password, String email, String nickname) {
        this.name = name;
        this.userId = userId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
    }

    public MemberDto(String userId, String name) {
        this.userId = userId;
        this.name = name;
    }

    public Member toEntity(){
        return Member.builder()
                .name(name)
                .userId(userId)
                .nickname(nickname)
                .password(password)
                .email(email)
                .build();
    }
}
