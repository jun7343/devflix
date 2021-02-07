package com.sitebase.dto;

import lombok.Data;

@Data
public class PostDto {

    private Long id;
    private String title;
    private String content;
    private String pathBase;
    private String[] images;
}
