package com.example.plantchatservice.domain;

import lombok.*;
import org.hibernate.annotations.DynamicInsert;

import javax.persistence.*;
import java.time.LocalDateTime;
/**
 * mysql에 저장할 chat entity
 * 엔티티에는 @NoArgsConstructor(access = AccessLevel.PROTECTED)
 * @AllArgsConstructor , @Setter지양
 * @DynamicInsert로 수정되는 컬럼만 쿼리
 */
@Entity
@Getter
@Table(name = "CHAT")
@DynamicInsert
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Chat {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_no")
    private Integer chatNo;

    @Column(name = "create_member")
    private Integer createMember;

    @Column(name = "join_member")
    private Integer joinMember;
    @Column(name = "trade_board")
    private Integer tradeBoardNo;

    @Column(name = "reg_date")
    private LocalDateTime regDate;
    @Builder
    public Chat(Integer chatNo, Integer createMember, Integer joinMember, Integer tradeBoardNo, LocalDateTime regDate) {
        this.chatNo = chatNo;
        this.createMember = createMember;
        this.joinMember = joinMember;
        this.tradeBoardNo = tradeBoardNo;
        this.regDate = regDate;
    }
}
