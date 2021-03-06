package com.devflix.service;

import com.devflix.entity.MemberConfirm;
import com.devflix.repository.MemberConfirmRepository;
import lombok.RequiredArgsConstructor;
import org.junit.Test;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@RequiredArgsConstructor
public class MemberConfirmService {

    private final MemberConfirmRepository memberConfirmRepository;

    @Transactional
    public List<MemberConfirm> findAll() {
        return memberConfirmRepository.findAll();
    }

    @Transactional
    public void deleteMemberConfirm(final MemberConfirm memberConfirm) {
        memberConfirmRepository.delete(memberConfirm);
    }
}
