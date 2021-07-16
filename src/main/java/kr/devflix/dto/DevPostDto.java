package kr.devflix.dto;

import kr.devflix.entity.DevPost;
import kr.devflix.constant.PostType;
import kr.devflix.constant.Status;
import lombok.Getter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Getter
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
    private List<String> tags;
    private Date createAt;
    private Date updateAt;

    public DevPostDto(DevPost devPost) {
        id = devPost.getId();
        category = devPost.getCategory();
        postType = devPost.getPostType();
        status = devPost.getStatus();
        view = devPost.getView();
        title = devPost.getTitle();
        description = devPost.getDescription();
        writer = devPost.getWriter();
        url = devPost.getUrl();
        uploadAt = devPost.getUploadAt();
        thumbnail = devPost.getThumbnail();
        tags = devPost.getTags()
                .stream()
                .map(devPostTag -> {return devPostTag.getTag();})
                .collect(Collectors.toList());
        createAt = devPost.getCreateAt();
        updateAt = devPost.getUpdateAt();
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
                .append("tags", tags)
                .append("createAt", createAt)
                .append("updateAt", updateAt)
                .toString();
    }
}
