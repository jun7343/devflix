package kr.devflix.repository;

import kr.devflix.constant.Status;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, JpaSpecificationExecutor<Post> {

    @Query(value = "update Post p set p.status = :status where p.writer = :writer")
    @Modifying
    int updateAllStatusByWriter(Status status, final Member writer);
}