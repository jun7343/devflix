package com.devflix.entity;

import com.devflix.constant.PostStatus;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class Post {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "post_status")
    @Enumerated(EnumType.STRING)
    private PostStatus postStatus;

    @ManyToOne(targetEntity = Member.class)
    private Member writer;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private long views;

    @Column(name = "path_base")
    private String pathBase;

    @Column(name = "images", columnDefinition = "varchar[]")
    @Type(type = "string-array")
    private String[] images;

    @Column(name = "create_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createDate;

    @Column(name = "update_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateDate;

    @Builder
    public Post(Long id, PostStatus postStatus, Member writer, String title, String content, long views, String pathBase, String[] images, Date createDate, Date updateDate) {
        this.id = id;
        this.postStatus = postStatus;
        this.writer = writer;
        this.title = title;
        this.content = content;
        this.views = views;
        this.pathBase = pathBase;
        this.images = images;
        this.createDate = createDate;
        this.updateDate = updateDate;
    }
}
