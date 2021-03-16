package kr.devflix.repository;

import kr.devflix.entity.ContactMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMeRepository extends JpaRepository<ContactMe, Long> {
}
