package kr.devflix.entity;

import kr.devflix.constant.MemberConfirmType;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "member_confirm")
public class MemberConfirm {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_member_confirm_id")
    @SequenceGenerator(name = "seq_member_confirm_id", sequenceName = "member_confirm_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column
    @Enumerated(EnumType.STRING)
    private MemberConfirmType type;

    @Column(unique = true)
    private String email;

    @Column(name = "confirm_count")
    private Integer confirmCount;

    @Column
    private String uuid;

    @Column(name = "create_at", updatable = false)
    private Date createAt;

    @Column(name = "update_at")
    private Date updateAt;

    protected MemberConfirm() {
    }

    public static MemberConfirmBuilder builder() {
        return new MemberConfirmBuilder();
    }

    private MemberConfirm(Long id, MemberConfirmType type, String email, Integer confirmCount, String uuid,
                          Date createAt, Date updateAt) {
        this.id = id;
        this.type = type;
        this.email = email;
        this.confirmCount = confirmCount;
        this.uuid = uuid;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static class MemberConfirmBuilder {
        private Long id;
        private MemberConfirmType type;
        private String email;
        private Integer confirmCount;
        private String uuid;
        private Date createAt;
        private Date updateAt;

        MemberConfirmBuilder() {
        }

        public MemberConfirmBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public MemberConfirmBuilder type(MemberConfirmType type) {
            this.type = type;
            return this;
        }

        public MemberConfirmBuilder email(String email) {
            this.email = email;
            return this;
        }

        public MemberConfirmBuilder confirmCount(Integer confirmCount) {
            this.confirmCount = confirmCount;
            return this;
        }

        public MemberConfirmBuilder uuid(String uuid) {
            this.uuid = uuid;
            return this;
        }

        public MemberConfirmBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public MemberConfirmBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public MemberConfirm build() {
            return new MemberConfirm(id, type, email, confirmCount, uuid, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "MemberConfirm{" +
                "id=" + id +
                ", type=" + type +
                ", email='" + email + '\'' +
                ", confirmCount=" + confirmCount +
                ", uuid='" + uuid + '\'' +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public MemberConfirmType getType() {
        return type;
    }

    public String getEmail() {
        return email;
    }

    public Integer getConfirmCount() {
        return confirmCount;
    }

    public String getUuid() {
        return uuid;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
