package kr.devflix.entity;

import kr.devflix.constant.Status;
import org.hibernate.annotations.BatchSize;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dev_post_id")
    private DevPost devPost;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private Integer view;

    @Column(name = "path_base")
    private String pathBase;

    @OneToMany(mappedBy = "post", fetch = FetchType.EAGER)
    @BatchSize(size = 50)
    private List<PostImage> images;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    @Column(name = "update_at")
    private LocalDateTime updateAt;

    protected Post() {
    }

    private Post(Long id, Status status, Member writer, DevPost devPost, String title, String content, Integer view,
                 String pathBase, List<PostImage> images, LocalDateTime createAt, LocalDateTime updateAt) {
        this.id = id;
        this.status = status;
        this.writer = writer;
        this.devPost = devPost;
        this.title = title;
        this.content = content;
        this.view = view;
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
        private DevPost devPost;
        private String title;
        private String content;
        private Integer view;
        private String pathBase;
        private List<PostImage> images;
        private LocalDateTime createAt;
        private LocalDateTime updateAt;

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

        public PostBuilder devPost(DevPost devPost) {
            this.devPost = devPost;
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

        public PostBuilder pathBase(String pathBase) {
            this.pathBase = pathBase;
            return this;
        }

        public PostBuilder images(List<PostImage> images) {
            this.images = images;
            return this;
        }

        public PostBuilder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public PostBuilder updateAt(LocalDateTime updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Post build() {
            return new Post(id, status, writer, devPost, title, content, view, pathBase, images, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "Post{" +
                "id=" + id +
                ", status=" + status +
                ", writer=" + writer +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", view=" + view +
                ", pathBase='" + pathBase + '\'' +
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

    public DevPost getDevPost() {
        return devPost;
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

    public List<PostImage> getImages() {
        return images;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }

    public LocalDateTime getUpdateAt() {
        return updateAt;
    }
}
