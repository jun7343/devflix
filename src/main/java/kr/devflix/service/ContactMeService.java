package kr.devflix.service;

import kr.devflix.entity.ContactMe;
import kr.devflix.repository.ContactMeRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class ContactMeService {

    private final ContactMeRepository contactMeRepository;

    @Transactional
    public ContactMe createContactMe(final ContactMe contactMe) {
        return contactMeRepository.save(contactMe);
    }

    @Transactional
    public Page<ContactMe> findAll(final int page, final int size) {
        return contactMeRepository.findAll(PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public Page<ContactMe> findAllBySearch(final String title, final String email, final String answer,
                                           final int page, final int size) {
        return contactMeRepository.findAll((root, query, criteriaBuilder) -> {
            List<Predicate> list = new LinkedList<>();

            if (! StringUtils.isBlank(title)) {
                list.add(criteriaBuilder.like(root.get("title"), "%" + title + "%"));
            }

            if (! StringUtils.isBlank(email)) {
                list.add(criteriaBuilder.equal(root.get("email"), email));
            }

            if (! StringUtils.isBlank(answer)) {
                list.add(criteriaBuilder.equal(root.get("confirm"), Boolean.parseBoolean(answer)));
            }

            return criteriaBuilder.and(list.toArray(new Predicate[0]));
        }, PageRequest.of(page, size, Sort.by(Sort.Order.desc("createAt"))));
    }

    @Transactional
    public Optional<ContactMe> findOneById(final long id) {
        return contactMeRepository.findById(id);
    }

    @Transactional
    public void updateContactMe(final ContactMe build) {
        contactMeRepository.save(build);
    }
}
