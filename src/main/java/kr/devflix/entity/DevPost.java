package kr.devflix.entity;

import kr.devflix.constant.PostType;
import kr.devflix.constant.Status;
import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(name = "dev_post")
@Getter
@ToString(exclude = "tags")
@EqualsAndHashCode(exclude = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevPost {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String category;

    @Column(name = "post_type")
    @Enumerated(EnumType.STRING)
    private PostType postType;

    @Column
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column
    private Integer view;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String writer;

    @Column(unique = true)
    private String url;

    @Column(name = "upload_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadAt;

    @Column
    private String thumbnail;

    @OneToMany(mappedBy = "devPost")
    private List<DevPostTag> tags = new ArrayList<>();

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public DevPost(Long id, String category, PostType postType, Status status, Integer view, String title,
                    String description, String writer, String url, Date uploadAt, String thumbnail, List<DevPostTag> tags,
                    Date createAt, Date updateAt) {
        this.id = id;
        this.category = category;
        this.postType = postType;
        this.status = status;
        this.view = view;
        this.title = title;
        this.description = description;
        this.writer = writer;
        this.url = url;
        this.uploadAt = uploadAt;
        this.thumbnail = thumbnail;
        this.tags = tags;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
