package com.sitebase.dto;

import lombok.*;

@Getter
@ToString
@EqualsAndHashCode
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
public class MemberDto {
    private final String username;
    private final String password;
    private final String name;
}
