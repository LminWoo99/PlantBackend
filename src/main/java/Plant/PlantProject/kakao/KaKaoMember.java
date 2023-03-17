package Plant.PlantProject.kakao;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
/**
 * packageName    : Plant/PlantProject/kakao
 * fileName       : KaKaoMember.java
 * author         : 이민우
 * date           : 2023-03-17
 * description    : 카카오 로그인 api 멤버
 * ===========================================================
 * DATE              AUTHOR             NOTE
 * -----------------------------------------------------------
 * 2022-03-16        이민우       최초 생성
 * 2022-03-17        이민우       카카오 api에서 이메일과 닉네임만 가져와서 엔티티에 저장
 *
 */
@Entity
@Getter
@NoArgsConstructor(access= AccessLevel.PROTECTED)
public class KaKaoMember {

    @Id
    @GeneratedValue
    @Column(name = "KAKAO_MEMBER_ID")
    private Long id;
    private String email;
    private String nickname;

    @Builder
    public KaKaoMember(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }
}
