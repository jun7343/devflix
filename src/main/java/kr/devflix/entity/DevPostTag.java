package kr.devflix.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "dev_post_tag")
@Getter
@ToString(exclude = "devPost")
@EqualsAndHashCode(exclude = "devPost")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DevPostTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "dev_post_id")
    private DevPost devPost;

    @Column
    private String tag;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Builder
    public DevPostTag(final Long id, final DevPost devPost, final String tag, final Date createAt) {
        this.id = id;
        this.devPost = devPost;
        this.tag = tag;
        this.createAt = createAt;
    }
}
