package kr.devflix.repository;

import kr.devflix.entity.ContactMe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ContactMeRepository extends PagingAndSortingRepository<ContactMe, Long>, JpaSpecificationExecutor<ContactMe> {
}
