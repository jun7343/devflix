package com.devflix.entity;

import com.devflix.constant.PostStatus;
import com.devflix.constant.PostType;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity(name = "dev_post")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
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
    private PostStatus status;

    @Column
    private int view;

    @Column
    private String title;

    @Column
    private String description;

    @Column
    private String writer;

    @Column
    private String url;

    @Column(name = "upload_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date uploadAt;

    @Column
    private String thumbnail;

    @Column(columnDefinition = "varchar[]")
    @Type(type = "list-array")
    private List<String> tag;

    @Column(name = "create_at", updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public DevPost(final Long id, final String category, final PostType postType, final PostStatus status, final int view, final String title,
                   final String description, final String writer, final String url, final Date uploadAt, final String thumbnail,
                   final List<String> tag, final Date createAt, final Date updateAt) {
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
        this.tag = tag;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
