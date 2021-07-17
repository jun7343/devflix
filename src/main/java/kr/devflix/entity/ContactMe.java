package kr.devflix.entity;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "contact_me")
public class ContactMe {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_contact_me_id")
    @SequenceGenerator(name = "seq_contact_me_id", sequenceName = "contact_me_id_seq", allocationSize = 1, initialValue = 1)
    private Long id;

    @Column
    private String title;

    @Column
    private String content;

    @Column
    private String email;

    @Column
    private Boolean confirm;

    @Column(name = "create_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createAt;

    @Column(name = "update_at")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateAt;

    protected ContactMe() {
    }

    private ContactMe(Long id, String title, String content, String email, Boolean confirm,
                      Date createAt, Date updateAt) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.email = email;
        this.confirm = confirm;
        this.createAt = createAt;
        this.updateAt = updateAt;
    }

    public static ContactMeBuilder builder() {
        return new ContactMeBuilder();
    }

    public static class ContactMeBuilder {
        private Long id;
        private String title;
        private String content;
        private String email;
        private Boolean confirm;
        private Date createAt;
        private Date updateAt;

        ContactMeBuilder() {
        }

        public ContactMeBuilder id(Long id) {
            this.id = id;
            return this;
        }

        public ContactMeBuilder title(String title) {
            this.title = title;
            return this;
        }

        public ContactMeBuilder content(String content) {
            this.content = content;
            return this;
        }

        public ContactMeBuilder email(String email) {
            this.email = email;
            return this;
        }

        public ContactMeBuilder confirm(Boolean confirm) {
            this.confirm = confirm;
            return this;
        }

        public ContactMeBuilder createAt(Date createAt) {
            this.createAt = createAt;
            return this;
        }

        public ContactMeBuilder updateAt(Date updateAt) {
            this.updateAt = updateAt;
            return this;
        }

        public ContactMe build() {
            return new ContactMe(id, title, content, email, confirm, createAt, updateAt);
        }
    }

    @Override
    public String toString() {
        return "ContactMe{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", email='" + email + '\'' +
                ", confirm=" + confirm +
                ", createAt=" + createAt +
                ", updateAt=" + updateAt +
                '}';
    }

    public Long getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getContent() {
        return content;
    }

    public String getEmail() {
        return email;
    }

    public Boolean getConfirm() {
        return confirm;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public Date getUpdateAt() {
        return updateAt;
    }
}
