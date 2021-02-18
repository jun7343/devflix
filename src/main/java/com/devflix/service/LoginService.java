package com.devflix.service;

import com.devflix.constant.MemberStatus;
import com.devflix.constant.RoleType;
import com.devflix.domain.JoinUsDomain;
import com.devflix.entity.Member;
import com.devflix.repository.MemberRepository;
import com.google.common.collect.ImmutableList;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Date;

@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public void createMember(final JoinUsDomain domain) {
        final Member user = memberRepository.findByEmail(domain.getEmail());

        if (user != null) {
            return;
        }

        Member joinUser = Member.builder()
                .email(domain.getEmail())
                .status(MemberStatus.ACTIVE)
                .password(passwordEncoder.encode(domain.getPassword()))
                .username(domain.getUsername())
                .authority(ImmutableList.of(RoleType.USER))
                .createAt(new Date())
                .updateAt(new Date())
                .build();

        memberRepository.save(joinUser);
    }

    @Override
    public Member loadUserByUsername(final String email) throws UsernameNotFoundException {
        System.out.println(email);
        final Member user = memberRepository.findByEmailEquals(email);

        if (user == null) {
            throw new InternalAuthenticationServiceException("user not found");
        }

        return user;
    }
}
