package Plant.PlantProject.vo.response;

import Plant.PlantProject.domain.Entity.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MemberResponseDto {
    private Long id;
    private String nickname;
    private String username;
    private String password;
    private String email;
    public static MemberResponseDto toDto(Member member) {
        return MemberResponseDto.builder()
                .id(member.getId())
                .nickname(member.getNickname())
                .username(member.getUsername())
                .password(member.getPassword())
                .email(member.getEmail())
                .build();
    }
}
