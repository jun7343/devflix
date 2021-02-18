package com.devflix.entity;

import com.devflix.constant.MemberStatus;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberStatus status;

    @Column(unique = true)
    private String email;

    @Column
    private String password;

    @Column
    private String username;

    @Type(type = "list-array")
    @Column(nullable = false, columnDefinition = "varchar[]")
    private List<String> authority;

    @Column(name = "create_at", nullable = false, updatable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        ArrayList<GrantedAuthority> array = new ArrayList<>();
        for (String auth : authority) {
            array.add(() -> auth);
        }
        return array;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    // 추후에 세팅
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    // 추후에 세팅
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    // 추후에 세팅
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    // 추후에 세팅
    @Override
    public boolean isEnabled() {
        return true;
    }

    @Builder
    public Member(final Long id, final MemberStatus status, final String email, final String password, final String username,
                  final List<String> authority, final Date createAt, final Date updateAt) {
        this.id = id;
        this.status = status;
        this.email = email;
        this.password = password;
        this.username = username;
        this.authority = authority;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
