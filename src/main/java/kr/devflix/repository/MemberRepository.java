package kr.devflix.repository;

import kr.devflix.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {
    Member findByEmail(final String email);
    Member findByEmailEquals(final String email);
}
