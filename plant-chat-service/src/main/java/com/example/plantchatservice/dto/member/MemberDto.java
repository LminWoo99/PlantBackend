package com.example.plantchatservice.dto.member;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class MemberDto {
    private Long id;
    private String nickname;
    private String userId;
    private String username;
    private String password;
    private String email;
}
