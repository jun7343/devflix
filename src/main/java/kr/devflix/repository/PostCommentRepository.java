package kr.devflix.repository;

import kr.devflix.constant.Status;
import kr.devflix.entity.PostComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentRepository extends PagingAndSortingRepository<PostComment, Long>, JpaSpecificationExecutor<PostComment> {

    @Query(value = "update post_comment p set p.status = :status where p.id in (:idList)")
    @Modifying
    void updateStatusByIdList(Status status, List<Long> idList);
}
