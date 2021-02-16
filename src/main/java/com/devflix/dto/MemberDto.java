package com.devflix.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
public class MemberDto {
    private final String username;
    private final String password;
    private final String name;

    @Builder
    public MemberDto(final String username, final String password, final String name) {
        this.username = username;
        this.password = password;
        this.name = name;
    }
}
