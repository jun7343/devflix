package com.devflix.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class PostDto {
    private final Long id;
    private final String title;
    private final String content;
    private final String pathBase;
    private final String[] images;

    @Builder
    public PostDto(final Long id, final String title, final String content, final String pathBase, final String[] images) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.pathBase = pathBase;
        this.images = images;
    }
}
