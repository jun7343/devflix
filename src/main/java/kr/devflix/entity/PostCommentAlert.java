package kr.devflix.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_comment_alert")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostCommentAlert {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = PostComment.class)
    @JoinColumn(name = "comment_id")
    private PostComment comment;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "user_id")
    private Member user;

    @Column
    private boolean confirm;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public PostCommentAlert(final Long id, final PostComment comment, final Member user, final boolean confirm,
                            final Date createAt, final Date updateAt) {
        this.id = id;
        this.comment = comment;
        this.user = user;
        this.confirm = confirm;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}