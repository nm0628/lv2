package com.sparta.lv2.dto;


import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Service;

@Getter
@Setter
public class PostRequestDto {
    public String title;
    public String content;
}
