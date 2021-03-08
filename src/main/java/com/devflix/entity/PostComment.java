package com.devflix.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_comment")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Member.class)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @ManyToOne(targetEntity = Post.class)
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

    @Builder
    public PostComment(final Long id, final Member writer, final Post post, final String comment,
                       final Date createAt, final Date updateAt) {
        this.id = id;
        this.writer = writer;
        this.post = post;
        this.comment = comment;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
