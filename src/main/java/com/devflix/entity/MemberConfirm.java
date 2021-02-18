package com.devflix.entity;

import com.devflix.constant.MemberConfirmType;
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
    private MemberConfirmType type;

    @Column
    private String email;

    @Column
    private String code;

    @Column(name = "create_at")
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    @Builder
    public MemberConfirm(final Long id, final MemberConfirmType type, final String email,
                         final String code, final Date createAt, final Date updateAt) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.code = code;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
