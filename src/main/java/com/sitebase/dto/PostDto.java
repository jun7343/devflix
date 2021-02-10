package com.sitebase.dto;

import lombok.*;

@Getter
@ToString
@NoArgsConstructor
public class PostDto {
    private Long id;
    private String title;
    private String content;
    private String pathBase;
    private String[] images;

    @Builder(access = AccessLevel.PRIVATE)
    private PostDto(Long id, @NonNull String title, @NonNull String content, String pathBase, String[] images) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pathBase = pathBase;
        this.images = images;
    }
}
