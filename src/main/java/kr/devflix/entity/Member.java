package kr.devflix.entity;

import com.vladmihalcea.hibernate.type.array.ListArrayType;
import kr.devflix.constant.MemberStatus;
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
@TypeDef(name = "list-array", typeClass = ListArrayType.class)
public class Member implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_member_id")
    @SequenceGenerator(name = "seq_member_id", sequenceName = "member_id_seq", allocationSize = 1, initialValue = 1)
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

    @Override
    public boolean isAccountNonExpired() {
        return this.status != MemberStatus.EXPIRED;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.status != MemberStatus.LOCK;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.status != MemberStatus.CREDENTIAL_EXPIRED;
    }

    @Override
    public boolean isEnabled() {
        return this.status != MemberStatus.WITHDRAWAL;
    }

    protected Member() {
    }

    private Member(Long id, MemberStatus status, String email, String password, String username, List<String> authority,
                   String pathBase, String imagePath, String description, String github, String facebook, String twiter,
                   String instagram, String linkedIn, Date createAt, Date updateAt) {
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

    public static MemberBuilder builder() {
        return new MemberBuilder();
    }

    public static class MemberBuilder {
        private Long id;
        private MemberStatus status;
        private String email;
        private String password;
        private String username;
        private List<String> authority;
        private String pathBase;
        private String imagePath;
        private String description;
        private String github;
        private String facebook;
        private String twiter;
        private String instagram;
        private String linkedIn;
        private Date createAt;
        private Date updateAt;

        MemberBuilder() {
        }

        public MemberBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberBuilder status(MemberStatus status) {
            this.status = status;
            return this;
        }

        public MemberBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberBuilder password(String password) {
            this.password = password;
            return this;
        }

        public MemberBuilder username(String username) {
            this.username = username;
            return this;
        }

        public MemberBuilder authority(List<String> authority) {
            this.authority = authority;
            return this;
        }

        public MemberBuilder pathBase(String pathBase) {
            this.pathBase = pathBase;
            return this;
        }

        public MemberBuilder imagePath(String imagePath) {
            this.imagePath = imagePath;
            return this;
        }

        public MemberBuilder description(String description) {
            this.description = description;
            return this;
        }

        public MemberBuilder github(String github) {
            this.github = github;
            return this;
        }

        public MemberBuilder facebook(String facebook) {
            this.facebook = facebook;
            return this;
        }

        public MemberBuilder twiter(String twiter) {
            this.twiter = twiter;
            return this;
        }

        public MemberBuilder instagram(String instagram) {
            this.instagram = instagram;
            return this;
        }

        public MemberBuilder linkedIn(String linkedIn) {
            this.linkedIn = linkedIn;
            return this;
        }

        public MemberBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public MemberBuilder update(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public Member build() {
            return new Member(id, status, email, password, username, authority, pathBase, imagePath, description, github, facebook,
                    twiter, instagram, linkedIn, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "Member{" +
                "id=" + id +
                ", status=" + status +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", username='" + username + '\'' +
                ", authority=" + authority +
                ", pathBase='" + pathBase + '\'' +
                ", imagePath='" + imagePath + '\'' +
                ", description='" + description + '\'' +
                ", github='" + github + '\'' +
                ", facebook='" + facebook + '\'' +
                ", twiter='" + twiter + '\'' +
                ", instagram='" + instagram + '\'' +
                ", linkedIn='" + linkedIn + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public MemberStatus getStatus() {
        return status;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getAuthority() {
        return authority;
    }

    public String getPathBase() {
        return pathBase;
    }

    public String getImagePath() {
        return imagePath;
    }

    public String getDescription() {
        return description;
    }

    public String getGithub() {
        return github;
    }

    public String getFacebook() {
        return facebook;
    }

    public String getTwiter() {
        return twiter;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getLinkedIn() {
        return linkedIn;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
