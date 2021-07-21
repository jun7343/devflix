package kr.devflix.dto;

import kr.devflix.constant.Status;
import kr.devflix.entity.Post;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class PostDto {
    private Long id;
    private Status status;
    private MemberDto writer;
    private String title;
    private String content;
    private Integer view;
    private String pathBase;
    private List<String> images;
    private DevPostDto devPost;
    private LocalDateTime createAt;
    private LocalDateTime updateAt;

    public PostDto() {}

    public PostDto(Post post) {
        id = post.getId();
        status = post.getStatus();
        writer = new MemberDto(post.getWriter());
        title = post.getTitle();
        content = post.getContent();
        view = post.getView();
        pathBase = post.getPathBase();
        images = post.getImages()
                .stream()
                .map(postImage -> {return postImage.getImageName();})
                .collect(Collectors.toList());
        devPost = new DevPostDto(post.getDevPost());
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
                .append("devPost", devPost)
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

    public MemberDto getWriter() {
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

    public DevPostDto getDevPost() {
        return devPost;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void setWriter(MemberDto writer) {
        this.writer = writer;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setView(Integer view) {
        this.view = view;
    }

    public void setPathBase(String pathBase) {
        this.pathBase = pathBase;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public void setDevPost(DevPostDto devPost) {
        this.devPost = devPost;
    }

    public void setCreateAt(LocalDateTime createAt) {
        this.createAt = createAt;
    }

    public void setUpdateAt(LocalDateTime updateAt) {
        this.updateAt = updateAt;
    }
}
