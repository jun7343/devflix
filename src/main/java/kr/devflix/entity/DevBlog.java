package kr.devflix.entity;

import kr.devflix.constant.Status;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "dev_blog")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevBlog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    @Builder
    public DevBlog(final Long id, Status status, final String category, final String blogName, final String url,
                   final String thumbnail, final Date createAt, final Date updateAt) {
        this.id = id;
        this.status = status;
        this.category = category;
        this.blogName = blogName;
        this.url = url;
        this.thumbnail = thumbnail;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
