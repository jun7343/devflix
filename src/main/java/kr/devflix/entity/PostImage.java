package kr.devflix.entity;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PostImage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_image_id")
    @SequenceGenerator(name = "seq_post_image_id", sequenceName = "post_image_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column(name = "path_base")
    private String pathBase;

    @Column(name = "image_name")
    private String imageName;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column(name = "create_at")
    private LocalDateTime createAt;

    protected PostImage() {
    }

    private PostImage(Long id, String pathBase, String imageName, Post post, LocalDateTime createAt) {
        this.id = id;
        this.pathBase = pathBase;
        this.imageName = imageName;
        this.post = post;
        this.createAt = createAt;
    }

    public static PostImageBuilder builder() {
        return new PostImageBuilder();
    }

    public static class PostImageBuilder {
        private Long id;
        private String pathBase;
        private String imageName;
        private Post post;
        private LocalDateTime createAt;

        PostImageBuilder() {}

        public PostImageBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PostImageBuilder pathBase(String pathBase) {
            this.pathBase = pathBase;
            return this;
        }

        public PostImageBuilder imageName(String imageName) {
            this.imageName = imageName;
            return this;
        }

        public PostImageBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public PostImageBuilder createAt(LocalDateTime createAt) {
            this.createAt = createAt;
            return this;
        }

        public PostImage build() {
            return new PostImage(id, pathBase, imageName, post, createAt);
        }
    }

    @Override
    public String toString() {
        return "PostImage{" +
                "id=" + id +
                ", pathBase='" + pathBase + '\'' +
                ", imageName='" + imageName + '\'' +
                ", createAt=" + createAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getPathBase() {
        return pathBase;
    }

    public String getImageName() {
        return imageName;
    }

    public Post getPost() {
        return post;
    }

    public LocalDateTime getCreateAt() {
        return createAt;
    }
}
