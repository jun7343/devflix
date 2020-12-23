package com.sitebase.entity;

import com.vladmihalcea.hibernate.type.array.StringArrayType;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;

import javax.persistence.*;

@Entity
@Getter
@Builder
@ToString
@TypeDef(name = "string-array", typeClass = StringArrayType.class)
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", unique = true, nullable = false)
    private String userId;

    @Column(nullable = false)
    private String password;

    @Column(name = "user_name", nullable = false)
    private String userName;

    @Type(type = "string-array")
    @Column(nullable = false, columnDefinition = "varchar[]")
    private String[] authority;
}
