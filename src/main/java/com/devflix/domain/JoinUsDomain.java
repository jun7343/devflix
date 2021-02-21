package com.devflix.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@RequiredArgsConstructor
public class JoinUsDomain {
    private final String email;
    private final String code;
    private final String username;
    private final String password;
}
