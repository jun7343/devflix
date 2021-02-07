package com.sitebase.entity;

import com.sitebase.constant.PostStatus;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@ToString
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

    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    @Column(name = "updated_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updatedDate;

    @Builder
    public Post(String title, PostStatus postStatus, Member writer, String content, long views,
                String pathBase, String[] images, Date createdDate, Date updatedDate) {
        this.title = title;
        this.postStatus = postStatus;
        this.content = content;
        this.views = views;
        this.pathBase = pathBase;
        this.images = images;
        this.writer = writer;
        this.createdDate = createdDate;
        this.updatedDate = updatedDate;
    }
}
