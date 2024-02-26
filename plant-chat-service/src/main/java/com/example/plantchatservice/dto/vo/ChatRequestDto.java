package com.example.plantchatservice.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ChatRequestDto {

    @NotNull
    private Integer tradeBoardNo;
    @NotNull
    private Integer createMember;

}
