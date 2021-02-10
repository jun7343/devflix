package com.sitebase.service;

import com.sitebase.constant.RoleType;
import com.sitebase.dto.MemberDto;
import com.sitebase.entity.Member;
import com.sitebase.repository.MemberRepository;
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
    public Member createMember(MemberDto dto) {
        Member findUser = memberRepository.findByUsername(dto.getUsername());

        if (findUser != null) {
            return null;
        }

        Member user = Member.builder()
                .username(dto.getUsername())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .authority(new String[] {RoleType.USER})
                .createAt(new Date())
                .updateAt(new Date())
                .build();

        return memberRepository.save(user);
    }

    @Transactional
    public Member findMember(String userName) {
        return memberRepository.findByUsername(userName);
    }

    @Override
    public Member loadUserByUsername(String username) throws UsernameNotFoundException {
        return memberRepository.findByUsername(username);
    }
}
