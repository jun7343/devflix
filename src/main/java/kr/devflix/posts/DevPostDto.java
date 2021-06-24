package kr.devflix.posts;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

import static org.springframework.beans.BeanUtils.copyProperties;

@Getter
@Setter
@EqualsAndHashCode
public class DevPostDto {

    private Long id;
    private String category;
    private PostType postType;
    private Status status;
    private Integer view;
    private String title;
    private String description;
    private String writer;
    private String url;
    private Date uploadAt;
    private String thumbnail;
    private List<String> tag;
    private Date createAt;
    private Date updateAt;

    public DevPostDto(final DevPost devPost) {
        copyProperties(devPost, this);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("category", category)
                .append("postType", postType)
                .append("status", status)
                .append("view", view)
                .append("title", title)
                .append("description", description)
                .append("writer", writer)
                .append("url", url)
                .append("uploadAt", uploadAt)
                .append("thumbnail", thumbnail)
                .append("tag", tag)
                .append("createAt", createAt)
                .append("uploadAt", uploadAt)
                .toString();
    }
}
