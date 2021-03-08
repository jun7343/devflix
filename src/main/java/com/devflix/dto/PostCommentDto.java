package com.devflix.dto;

import com.devflix.entity.Member;
import com.devflix.entity.Post;
import lombok.*;

import java.util.Date;

@Getter
@ToString
@EqualsAndHashCode
@Builder
@RequiredArgsConstructor
public class PostCommentDto {
    private final Long id;
    private final Member writer;
    private final Post post;
    private final String comment;
    private final Date createAt;
    private final Date updateAt;
}
