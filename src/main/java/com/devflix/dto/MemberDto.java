package com.devflix.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@Builder
public class MemberDto {
    private final String email;
    private final String username;
    private final String password;
}
