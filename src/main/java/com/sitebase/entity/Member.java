package com.sitebase.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.*;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class Member {

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

    @Builder
    public Member(String username, String password, String name, String[] authority) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.authority = authority;
    }
}
