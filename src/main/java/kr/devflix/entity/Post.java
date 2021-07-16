package kr.devflix.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import kr.devflix.constant.Status;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @ManyToOne(targetEntity = Member.class, fetch = FetchType.LAZY)
    @JoinColumn(name = "writer_id")
    private Member writer;

    @Column(name = "dev_post_url")
    private String devPostUrl;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private int view;

    @Column
    private long commentCount;

    @Column(name = "path_base")
    private String pathBase;

    @Column(name = "image_path")
    @Type(type = "list-array")
    private List<String> images;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public Post(final Long id, final Status status, final Member writer, String devPostUrl,
                final String title, final String content, final int view, final long commentCount, final String pathBase,
                List<String> images, final Date createAt, final Date updateAt) {
        this.id = id;
        this.status = status;
        this.writer = writer;
        this.devPostUrl = devPostUrl;
        this.title = title;
        this.content = content;
        this.view = view;
        this.commentCount = commentCount;
        this.pathBase = pathBase;
        this.images = images;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
