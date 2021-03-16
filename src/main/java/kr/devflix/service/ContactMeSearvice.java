package kr.devflix.service;

import kr.devflix.entity.ContactMe;
import kr.devflix.repository.ContactMeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ContactMeSearvice {

    private final ContactMeRepository contactMeRepository;

    @Transactional
    public ContactMe createContactMe(final ContactMe contactMe) {
        return contactMeRepository.save(contactMe);
    }
}
