package com.sitebase.service;

import com.sitebase.command.MemberCommand;
import com.sitebase.constant.ResultType;
import com.sitebase.constant.RoleType;
import com.sitebase.entity.Member;
import com.sitebase.repository.MemberRepository;
import com.sitebase.utils.Result;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LoginService implements UserDetailsService {

    private final MemberRepository memberRepository;

    private final PasswordEncoder passwordEncoder;

    public LoginService(MemberRepository memberRepository, PasswordEncoder passwordEncoder) {
        this.memberRepository = memberRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Result createMember(MemberCommand userCommand) {
        Optional<Member> findUser = memberRepository.findByUserId(userCommand.getId());

        // 생성하려는 아이디가 존재하면 return false;
        if (findUser.isPresent()) {
            return new Result(ResultType.ERROR, "이미 존재한 아이디 입니다.");
        }

        Result result = userCommand.validate();

        if (result.isERROR()) {
            return result;
        }

        Member user = Member.builder()
                .userId(userCommand.getId())
                .userName(userCommand.getName())
                .password(passwordEncoder.encode(userCommand.getPassword()))
                .authority(new String[] {RoleType.ROLE_USER.getRole()})
                .build();

        memberRepository.save(user);

        return new Result(ResultType.SUCCESS);
    }

    @Transactional
    public Boolean getUser(String userId) {
        Optional<Member> user = memberRepository.findByUserId(userId);

        return user.isPresent();
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        Optional<Member> optional = memberRepository.findByUserId(userId);
        Member member = optional.get();

        List<SimpleGrantedAuthority> list = new ArrayList<>();

        for (String str : member.getAuthority()) {
            list.add(new SimpleGrantedAuthority(str));
        }

        return new User(member.getUserId(), member.getPassword(), list);
    }
}
