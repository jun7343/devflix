package kr.devflix.repository;

import kr.devflix.entity.PostComment;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostCommentRepository extends PagingAndSortingRepository<PostComment, Long>, JpaSpecificationExecutor<PostComment> {
}
