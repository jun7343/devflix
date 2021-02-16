package com.sitebase.entity;

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

@Entity
@Getter
@ToString
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Type(type = "string-array")
    @Column(nullable = false, columnDefinition = "varchar[]")
    private String[] authority;

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
    public Member(Long id, String username, String password, String name, String[] authority, Date createAt, Date updateAt) {
        this.id = id;
        this.username = username;
        this.password = password;
        this.name = name;
        this.authority = authority;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
