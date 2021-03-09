package com.devflix.dto;

import com.devflix.constant.Status;
import com.devflix.entity.DevPost;
import com.devflix.entity.Member;
import lombok.*;

import java.util.List;

@Getter
@ToString
@EqualsAndHashCode
@RequiredArgsConstructor
@Builder
public class PostDto {
    private final Long id;
    private final Status status;
    private final Member writer;
    private final String title;
    private final String content;
    private final int view;
    private final String pathBase;
    private final List<String> images;
    private final List<DevPost> devPostList;
    private final List<String> devPostUrl;
}
