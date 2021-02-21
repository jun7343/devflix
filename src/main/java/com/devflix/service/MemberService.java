package com.devflix.service;

import com.devflix.constant.MemberConfirmType;
import com.devflix.constant.MemberStatus;
import com.devflix.constant.RoleType;
import com.devflix.domain.JoinUsDomain;
import com.devflix.entity.Member;
import com.devflix.entity.MemberConfirm;
import com.devflix.repository.MemberConfirmRepository;
import com.devflix.repository.MemberRepository;
import com.devflix.utils.JavaMailUtil;
import com.google.common.collect.ImmutableList;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.crypto.EncryptedPrivateKeyInfo;
import javax.transaction.Transactional;
import java.util.Date;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
    private final MemberConfirmRepository memberConfirmRepository;
    private final PasswordEncoder passwordEncoder;
    private final JavaMailUtil javaMailUtil;

    @Transactional
    public Member createMemberAndDeleteMemberConfirm(final JoinUsDomain domain) {
        final Member user = memberRepository.findByEmail(domain.getEmail());

        if (user != null) {
            return null;
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

        MemberConfirm confirm = memberConfirmRepository.findByEmailEquals(domain.getEmail());

        if (confirm != null) {
            memberConfirmRepository.delete(confirm);
        }

        return memberRepository.save(joinUser);
    }

    @Transactional
    public Member findUserByEmail(final String email) {
        return memberRepository.findByEmail(email);
    }

    @Transactional
    public MemberConfirm createOrUpdateMemberConfirmByEmail(final String email) {
        MemberConfirm findConfirm = memberConfirmRepository.findByEmailEquals(email);
        UUID uuid = UUID.randomUUID();
        MemberConfirm newConfirm;

        if (findConfirm == null) {
            newConfirm = MemberConfirm.builder()
                    .type(MemberConfirmType.EMAIL_AUTHENTICATION)
                    .email(email)
                    .confirmCount(1)
                    .uuid(uuid.toString())
                    .createAt(new Date())
                    .updateAt(new Date())
                    .build();
        } else {
            newConfirm = MemberConfirm.builder()
                    .id(findConfirm.getId())
                    .type(findConfirm.getType())
                    .email(findConfirm.getEmail())
                    .confirmCount(findConfirm.getConfirmCount() + 1)
                    .uuid(uuid.toString())
                    .createAt(findConfirm.getCreateAt())
                    .updateAt(new Date())
                    .build();
        }

        javaMailUtil.emailConfirmSendMail(newConfirm, uuid);

        return memberConfirmRepository.save(newConfirm);
    }

    @Transactional
    public MemberConfirm findMemberConfirmByEmail(final String email) {
        return memberConfirmRepository.findByEmailEquals(email);
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
