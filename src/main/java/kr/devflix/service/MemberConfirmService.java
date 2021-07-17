package kr.devflix.service;

import kr.devflix.entity.MemberConfirm;
import kr.devflix.repository.MemberConfirmRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class MemberConfirmService {

    private final MemberConfirmRepository memberConfirmRepository;

    public MemberConfirmService(MemberConfirmRepository memberConfirmRepository) {
        this.memberConfirmRepository = memberConfirmRepository;
    }

    @Transactional
    public List<MemberConfirm> findAll() {
        return memberConfirmRepository.findAll();
    }

    @Transactional
    public void deleteMemberConfirm(final MemberConfirm memberConfirm) {
        memberConfirmRepository.delete(memberConfirm);
    }
}
