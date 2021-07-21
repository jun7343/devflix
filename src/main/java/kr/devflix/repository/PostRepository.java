package kr.devflix.repository;

import kr.devflix.constant.Status;
import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post>, PostRepositoryCustom {

    @Query(value = "update Post p set p.status = :status where p.writer = :writer")
    @Modifying
    void updateAllStatusByWriter(Status status, final Member writer);

    @Query(value = "update Post p set p.status = :status where p.id in (:idList)")
    @Modifying
    void updateStatusByIdList(Status status, List<Long> idList);
}