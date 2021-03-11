package kr.devflix.dto;

import kr.devflix.constant.Status;
import kr.devflix.entity.DevPost;
import kr.devflix.entity.Member;
import lombok.*;

import java.util.Date;
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
    private final Date createAt;
    private final Date updateAt;
}
