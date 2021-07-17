package kr.devflix.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import kr.devflix.constant.Status;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_id")
    @SequenceGenerator(name = "seq_post_id", sequenceName = "post_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(name = "dev_post_url")
    private String devPostUrl;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Integer view;

    @Column
    private Long commentCount;

    @Column(name = "path_base")
    private String pathBase;

    @Column(name = "image_path")
    @Type(type = "list-array")
    private List<String> images;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected Post() {
    }

    private Post(Long id, Status status, Member writer, String devPostUrl, String title, String content, Integer view,
                 Long commentCount, String pathBase, List<String> images, Date createAt, Date updateAt) {
        this.id = id;
        this.status = status;
        this.writer = writer;
        this.devPostUrl = devPostUrl;
        this.title = title;
        this.content = content;
        this.view = view;
        this.commentCount = commentCount;
        this.pathBase = pathBase;
        this.images = images;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static PostBuilder builder() {
        return new PostBuilder();
    }

    public static class PostBuilder {
        private Long id;
        private Status status;
        private Member writer;
        private String devPostUrl;
        private String title;
        private String content;
        private Integer view;
        private Long commentCount;
        private String pathBase;
        private List<String> images;
        private Date createAt;
        private Date updateAt;

        PostBuilder() {
        }

        public PostBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PostBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public PostBuilder writer(Member writer) {
            this.writer = writer;
            return this;
        }

        public PostBuilder devPostUrl(String devPostUrl) {
            this.devPostUrl = devPostUrl;
            return this;
        }

        public PostBuilder title(String title) {
            this.title = title;
            return this;
        }

        public PostBuilder content(String content) {
            this.content = content;
            return this;
        }

        public PostBuilder view(Integer view) {
            this.view = view;
            return this;
        }

        public PostBuilder commentCount(Long commentCount) {
            this.commentCount = commentCount;
            return this;
        }

        public PostBuilder pathBase(String pathBase) {
            this.pathBase = pathBase;
            return this;
        }

        public PostBuilder images(List<String> images) {
            this.images = images;
            return this;
        }

        public PostBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public PostBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Post build() {
            return new Post(id, status, writer, devPostUrl, title, content, view, commentCount, pathBase, images, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", status=" + status +
                ", devPostUrl='" + devPostUrl + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", view=" + view +
                ", commentCount=" + commentCount +
                ", pathBase='" + pathBase + '\'' +
                ", images=" + images +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
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

    public String getDevPostUrl() {
        return devPostUrl;
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

    public Long getCommentCount() {
        return commentCount;
    }

    public String getPathBase() {
        return pathBase;
    }

    public List<String> getImages() {
        return images;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
