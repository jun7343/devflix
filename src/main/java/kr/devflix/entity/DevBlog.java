package kr.devflix.entity;

import kr.devflix.constant.Status;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "dev_blog")
public class DevBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dev_blog_id")
    @SequenceGenerator(name = "seq_dev_blog_id", sequenceName = "dev_blog_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(unique = true)
    private String category;

    @Column(name = "blog_name")
    private String blogName;

    @Column(unique = true)
    private String url;

    @Column
    private String thumbnail;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected DevBlog() {
    }

    private DevBlog(Long id, Status status, String category, String blogName, String url,
                   String thumbnail, Date createAt, Date updateAt) {
        this.id = id;
        this.status = status;
        this.category = category;
        this.blogName = blogName;
        this.url = url;
        this.thumbnail = thumbnail;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static DevBlogBuilder builder() {
        return new DevBlogBuilder();
    }

    public static class DevBlogBuilder {
        private Long id;
        private Status status;
        private String category;
        private String blogName;
        private String url;
        private String thumbnail;
        private Date createAt;
        private Date updateAt;

        DevBlogBuilder() {
        }

        public DevBlogBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DevBlogBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public DevBlogBuilder category(String category) {
            this.category = category;
            return this;
        }

        public DevBlogBuilder blogName(String blogName) {
            this.blogName = blogName;
            return this;
        }

        public DevBlogBuilder url(String url) {
            this.url = url;
            return this;
        }

        public DevBlogBuilder thumbnail(String thumbnail) {
            this.thumbnail = thumbnail;
            return this;
        }

        public DevBlogBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public DevBlogBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public DevBlog build() {
            return new DevBlog(id, status, category, blogName, url, thumbnail, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "DevBlog{" +
                "id=" + id +
                ", status=" + status +
                ", category='" + category + '\'' +
                ", blogName='" + blogName + '\'' +
                ", url='" + url + '\'' +
                ", thumbnail='" + thumbnail + '\'' +
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

    public String getCategory() {
        return category;
    }

    public String getBlogName() {
        return blogName;
    }

    public String getUrl() {
        return url;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
