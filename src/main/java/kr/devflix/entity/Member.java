package kr.devflix.entity;

import kr.devflix.constant.MemberStatus;
import com.vladmihalcea.hibernate.type.array.ListArrayType;
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

    @Column(nullable = false, columnDefinition = "varchar[]")
    @Type(type = "list-array")
    private List<String> authority;

    @Column(name = "path_base")
    private String pathBase;

    @Column(name = "image_path")
    private String imagePath;

    @Column
    private String description;

    @Column
    private String github;

    @Column
    private String facebook;

    @Column
    private String twiter;

    @Column
    private String instagram;

    @Column(name = "linked_in")
    private String linkedIn;

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
        return this.status != MemberStatus.EXPIRED;
    }

    // 추후에 세팅
    @Override
    public boolean isAccountNonLocked() {
        return this.status != MemberStatus.LOCK;
    }

    // 추후에 세팅
    @Override
    public boolean isCredentialsNonExpired() {
        return this.status != MemberStatus.CREDENTIAL_EXPIRED;
    }

    // 추후에 세팅
    @Override
    public boolean isEnabled() {
        return this.status != MemberStatus.WITHDRAWAL;
    }

    @Builder
    public Member(final Long id, final MemberStatus status, final String email, final String password, final String username,
                  final List<String> authority, final String pathBase, final String imagePath, final String description,
                  final String github, final String facebook, final String twiter, final String instagram,
                  final String linkedIn, final Date createAt, final Date updateAt) {
        this.id = id;
        this.status = status;
        this.email = email;
        this.password = password;
        this.username = username;
        this.authority = authority;
        this.pathBase = pathBase;
        this.imagePath = imagePath;
        this.description = description;
        this.github = github;
        this.facebook = facebook;
        this.twiter = twiter;
        this. instagram = instagram;
        this.linkedIn = linkedIn;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }
}
