package com.devflix.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "post_dev_post")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostDevPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = Post.class)
    @Column
    private Post post;

    @ManyToOne(targetEntity = DevPost.class)
    @Column(name = "dev_post")
    private DevPost devPost;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public PostDevPost(final Long id, final Post post, final DevPost devPost, final Date createAt, final Date updateAt) {
        this.id = id;
        this.post = post;
        this.devPost = devPost;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
