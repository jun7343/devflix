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
import org.springframework.security.authentication.InternalAuthenticationServiceException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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

        final MemberConfirm confirm = memberConfirmRepository.findByEmailEquals(domain.getEmail());

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
    public MemberConfirm createOrUpdateMemberConfirmByEmail(final String email, final MemberConfirmType type, HttpServletRequest request) {
        MemberConfirm findConfirm = memberConfirmRepository.findByEmailEquals(email);
        UUID uuid = UUID.randomUUID();
        MemberConfirm newConfirm;

        if (findConfirm == null) {
            newConfirm = MemberConfirm.builder()
                    .type(type)
                    .email(email)
                    .confirmCount(1)
                    .uuid(uuid.toString())
                    .createAt(new Date())
                    .updateAt(new Date())
                    .build();
        } else {
            newConfirm = MemberConfirm.builder()
                    .id(findConfirm.getId())
                    .type(type)
                    .email(findConfirm.getEmail())
                    .confirmCount(findConfirm.getConfirmCount() + 1)
                    .uuid(uuid.toString())
                    .createAt(findConfirm.getCreateAt())
                    .updateAt(new Date())
                    .build();
        }

        if (newConfirm.getConfirmCount() <= 5) {
            if (type == MemberConfirmType.EMAIL_AUTHENTICATION) {
                javaMailUtil.emailConfirmSendMail(newConfirm, uuid, request);
            } else if (type == MemberConfirmType.PASSWORD) {
                javaMailUtil.findPasswordSendMail(newConfirm, uuid, request);
            }

            return memberConfirmRepository.save(newConfirm);
        } else {
            return newConfirm;
        }
    }

    @Transactional
    public MemberConfirm findMemberConfirmByEmail(final String email) {
        return memberConfirmRepository.findByEmailEquals(email);
    }

    @Transactional
    public MemberConfirm findMemberConfirmByUuid(final String uuid) {
        return memberConfirmRepository.findByUuidEquals(uuid);
    }

    @Transactional
    public Member updateMemberPasswordAndDeleteMemberConfirm(final String password, final MemberConfirm confirm) {
        final Member user = memberRepository.findByEmail(confirm.getEmail());

        final Member updateUser = Member.builder()
                .id(user.getId())
                .password(passwordEncoder.encode(password))
                .email(user.getEmail())
                .status(user.getStatus())
                .username(user.getUsername())
                .authority(user.getAuthority())
                .createAt(user.getCreateAt())
                .updateAt(new Date())
                .build();

        memberConfirmRepository.delete(confirm);

        return memberRepository.save(updateUser);
    }

    @Override
    public Member loadUserByUsername(final String email) throws UsernameNotFoundException {
        final Member user = memberRepository.findByEmailEquals(email);

        if (user == null) {
            throw new InternalAuthenticationServiceException("user not found");
        }

        return user;
    }
}
