package kr.devflix.entity;

import kr.devflix.constant.Status;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_comment")
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_comment_id")
    @SequenceGenerator(name = "seq_post_comment_id", sequenceName = "post_comment_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @Column
    private String comment;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected PostComment() {
    }

    private PostComment(Long id, Status status, Member writer, Post post, String comment, Date createAt, Date updateAt) {
        this.id = id;
        this.status = status;
        this.writer = writer;
        this.post = post;
        this.comment = comment;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static PostCommentBuilder builder() {
        return new PostCommentBuilder();
    }

    public static class PostCommentBuilder {
        private Long id;
        private Status status;
        private Member writer;
        private Post post;
        private String comment;
        private Date createAt;
        private Date updateAt;

        PostCommentBuilder() {
        }

        public PostCommentBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PostCommentBuilder status(Status status) {
            this.status = status;
            return this;
        }

        public PostCommentBuilder writer(Member writer) {
            this.writer = writer;
            return this;
        }

        public PostCommentBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public PostCommentBuilder comment(String comment) {
            this.comment = comment;
            return this;
        }

        public PostCommentBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public PostCommentBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public PostComment build() {
            return new PostComment(id, status, writer, post, comment, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "PostComment{" +
                "id=" + id +
                ", status=" + status +
                ", writer=" + writer +
                ", post=" + post +
                ", comment='" + comment + '\'' +
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

    public Post getPost() {
        return post;
    }

    public String getComment() {
        return comment;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
