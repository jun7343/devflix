package kr.devflix.service;

import kr.devflix.entity.MemberConfirm;
import kr.devflix.repository.MemberConfirmRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


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
