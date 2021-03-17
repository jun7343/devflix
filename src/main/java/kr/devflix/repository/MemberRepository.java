package kr.devflix.repository;

import kr.devflix.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends PagingAndSortingRepository<Member, Long>, JpaSpecificationExecutor<Member> {
    Member findByEmail(final String email);
    Member findByEmailEquals(final String email);
}
