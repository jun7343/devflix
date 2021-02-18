package com.devflix.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@ToString
public class JoinUsDomain {
    private final String email;
    private final String username;
    private final String password;
}
