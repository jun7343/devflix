package kr.devflix.dto;

import kr.devflix.constant.Status;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;
import java.util.List;

public class PostDto {
    private Long id;
    private Status status;
    private Member writer;
    private String title;
    private String content;
    private Integer view;
    private String pathBase;
    private List<String> images;
    private String devPostUrl;
    private Date createAt;
    private Date updateAt;

    public PostDto(Post post) {
        id = post.getId();
        status = post.getStatus();
        writer = post.getWriter();
        title = post.getTitle();
        content = post.getContent();
        view = post.getView();
        pathBase = post.getPathBase();
        images = post.getImages();
        devPostUrl = post.getDevPostUrl();
        createAt = post.getCreateAt();
        updateAt = post.getUpdateAt();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("status", status)
                .append("writer", writer)
                .append("title", title)
                .append("content", content)
                .append("view", view)
                .append("pathBase", pathBase)
                .append("images", images)
                .append("devPostUrl", devPostUrl)
                .append("createAt", createAt)
                .append("updateAt", updateAt)
                .toString();
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Member getWriter() {
        return writer;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public Integer getView() {
        return view;
    }

    public String getPathBase() {
        return pathBase;
    }

    public List<String> getImages() {
        return images;
    }

    public String getDevPostUrl() {
        return devPostUrl;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
