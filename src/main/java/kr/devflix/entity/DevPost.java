package kr.devflix.entity;

import kr.devflix.constant.PostType;
import kr.devflix.constant.Status;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "dev_post")
public class DevPost {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dev_post_id")
    @SequenceGenerator(name = "seq_dev_post_id", sequenceName = "dev_post_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column
    private String category;

    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Integer view;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String writer;

    @Column(unique = true)
    private String url;

    @Column(name = "upload_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadAt;

    @Column
    private String thumbnail;

    @OneToMany(mappedBy = "devPost")
    private List<DevPostTag> tags = new ArrayList<>();

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected DevPost() {
    }

    private DevPost(Long id, String category, PostType postType, Status status, Integer view, String title,
                    String description, String writer, String url, Date uploadAt, String thumbnail, List<DevPostTag> tags,
                    Date createAt, Date updateAt) {
        this.id = id;
        this.category = category;
        this.postType = postType;
        this.status = status;
        this.view = view;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.url = url;
        this.uploadAt = uploadAt;
        this.thumbnail = thumbnail;
        this.tags = tags;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static DevPostBuilder builder() {
        return new DevPostBuilder();
    }

    public static class DevPostBuilder {
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
        private List<DevPostTag> tags;
        private Date createAt;
        private Date updateAt;

        DevPostBuilder() {
        }

        public DevPostBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DevPostBuilder category(String category) {
            this.category = category;
            return this;
        }

        public DevPostBuilder postType(PostType postType) {
            this.postType = postType;
            return this;
        }

        public DevPostBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public DevPostBuilder view(Integer view) {
            this.view = view;
            return this;
        }

        public DevPostBuilder title(String title) {
            this.title = title;
            return this;
        }

        public DevPostBuilder description(String description) {
            this.description = description;
            return this;
        }

        public DevPostBuilder writer(String writer) {
            this.writer = writer;
            return this;
        }

        public DevPostBuilder url(String url) {
            this.url = url;
            return this;
        }

        public DevPostBuilder uploadAt(Date uploadAt) {
            this.uploadAt = uploadAt;
            return this;
        }

        public DevPostBuilder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public DevPostBuilder tags(List<DevPostTag> tags) {
            this.tags = tags;
            return this;
        }

        public DevPostBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public DevPostBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public DevPost build() {
            return new DevPost(id, category, postType, status, view, title, description, writer, url, uploadAt, thumbnail, tags, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "DevPost{" +
                "id=" + id +
                ", category='" + category + '\'' +
                ", postType=" + postType +
                ", status=" + status +
                ", view=" + view +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", writer='" + writer + '\'' +
                ", url='" + url + '\'' +
                ", uploadAt=" + uploadAt +
                ", thumbnail='" + thumbnail + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getCategory() {
        return category;
    }

    public PostType getPostType() {
        return postType;
    }

    public Status getStatus() {
        return status;
    }

    public Integer getView() {
        return view;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public String getWriter() {
        return writer;
    }

    public String getUrl() {
        return url;
    }

    public Date getUploadAt() {
        return uploadAt;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public List<DevPostTag> getTags() {
        return tags;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
