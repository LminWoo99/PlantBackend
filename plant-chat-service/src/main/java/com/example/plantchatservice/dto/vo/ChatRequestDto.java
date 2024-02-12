package com.example.plantchatservice.dto.vo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

import javax.validation.constraints.NotNull;

@Getter
@ToString
@AllArgsConstructor
public class ChatRequestDto {

    @NotNull
    private Integer tradeBoardNo;
    @NotNull
    private Integer createMember;

}
