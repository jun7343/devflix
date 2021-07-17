package kr.devflix.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_comment_alert")
public class PostCommentAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_post_comment_alert_id")
    @SequenceGenerator(name = "seq_post_comment_alert_id", sequenceName = "post_comment_alert_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne
    @JoinColumn(name = "comment_id")
    private PostComment comment;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Member user;

    @Column
    private Boolean confirm;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected PostCommentAlert() {
    }

    public static PostCommentAlertBuilder builder() {
        return new PostCommentAlertBuilder();
    }

    private PostCommentAlert(Long id, Post post, PostComment comment, Member user, Boolean confirm,
                             Date createAt, Date updateAt) {
        this.id = id;
        this.post = post;
        this.comment = comment;
        this.user = user;
        this.confirm = confirm;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static class PostCommentAlertBuilder {
        private Long id;
        private Post post;
        private PostComment comment;
        private Member user;
        private Boolean confirm;
        private Date createAt;
        private Date updateAt;

        PostCommentAlertBuilder() {
        }

        public PostCommentAlertBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public PostCommentAlertBuilder post(Post post) {
            this.post = post;
            return this;
        }

        public PostCommentAlertBuilder comment(PostComment comment) {
            this.comment = comment;
            return this;
        }

        public PostCommentAlertBuilder user(Member user) {
            this.user = user;
            return this;
        }

        public PostCommentAlertBuilder confirm(Boolean confirm) {
            this.confirm = confirm;
            return this;
        }

        public PostCommentAlertBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public PostCommentAlertBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public PostCommentAlert build() {
            return new PostCommentAlert(id, post, comment, user, confirm, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "PostCommentAlert{" +
                "id=" + id +
                ", confirm=" + confirm +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public Post getPost() {
        return post;
    }

    public PostComment getComment() {
        return comment;
    }

    public Member getUser() {
        return user;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
