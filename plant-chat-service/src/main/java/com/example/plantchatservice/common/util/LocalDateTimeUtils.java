package com.example.plantchatservice.common.util;

import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

@Slf4j
@NoArgsConstructor
public class LocalDateTimeUtils {

    /**
    * @title 날짜를 분리해 배열로 반환
    **/
    public static Integer[] toArray(LocalDateTime localDateTime) {
        if (localDateTime == null) {
            return new Integer[6];
        }
        String dateString = localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm-ss"));

        return Arrays.stream(dateString.split("-"))
                .map(Integer::valueOf)
                .toArray(Integer[]::new);
    }

    public static String toString(LocalDateTime localDateTime){
        return localDateTime.format(DateTimeFormatter.ofPattern("yyyy. MM. dd. HH:mm"));
    }
}
