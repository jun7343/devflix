package kr.devflix.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "dev_post_tag")
public class DevPostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_dev_post_tag_id")
    @SequenceGenerator(name = "seq_dev_post_tag_id", sequenceName = "dev_post_tag_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dev_post_id")
    private DevPost devPost;

    @Column
    private String tag;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    protected DevPostTag() {
    }

    private DevPostTag(final Long id, final DevPost devPost, final String tag, final Date createAt) {
        this.id = id;
        this.devPost = devPost;
        this.tag = tag;
        this.createAt = createAt;
    }

    public static DevPostTagBuilder builder() {
        return new DevPostTagBuilder();
    }

    public static class DevPostTagBuilder {
        private Long id;
        private DevPost devPost;
        private String tag;
        private Date createAt;

        DevPostTagBuilder() {
        }

        public DevPostTagBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public DevPostTagBuilder devPost(DevPost devPost) {
            this.devPost = devPost;
            return this;
        }

        public DevPostTagBuilder tag(String tag) {
            this.tag = tag;
            return this;
        }

        public DevPostTagBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public DevPostTag build() {
            return new DevPostTag(id, devPost, tag, createAt);
        }
    }

    @Override
    public String toString() {
        return "DevPostTag{" +
                "id=" + id +
                ", tag='" + tag + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public DevPost getDevPost() {
        return devPost;
    }

    public String getTag() {
        return tag;
    }

    public Date getCreateAt() {
        return createAt;
    }
}
