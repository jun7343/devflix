package kr.devflix.entity;

import kr.devflix.constant.MemberConfirmType;
import lombok.*;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "member_confirm")
@Getter
@ToString
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EqualsAndHashCode
public class MemberConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberConfirmType type;

    @Column(unique = true)
    private String email;

    @Column(name = "confirm_count")
    private int confirmCount;

    @Column
    private String uuid;

    @Column(name = "create_at", updatable = false)
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Builder
    public MemberConfirm(final Long id, final MemberConfirmType type, final String email,
                         final int confirmCount, final String uuid, final Date createAt, final Date updateAt) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.confirmCount = confirmCount;
        this.uuid = uuid;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
