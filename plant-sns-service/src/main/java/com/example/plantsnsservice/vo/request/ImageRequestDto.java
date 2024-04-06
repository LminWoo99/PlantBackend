package com.example.plantsnsservice.vo.request;

import com.sun.istack.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ImageRequestDto {
    @NotNull
    private Long snsPostId;
    @NotNull
    private String name;
    @NotNull
    private String url;
}
