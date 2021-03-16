package kr.devflix.entity;

import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "contact_me")
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ContactMe {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String email;

    @Column
    private boolean confirm;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Builder
    public ContactMe(final Long id, final String title, final String content, final String email,
                     final boolean confirm, final Date createAt, final Date updateAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.email = email;
        this.confirm = confirm;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
