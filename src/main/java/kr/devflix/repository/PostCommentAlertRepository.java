package kr.devflix.repository;

import kr.devflix.entity.Member;
import kr.devflix.entity.Post;
import kr.devflix.entity.PostCommentAlert;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostCommentAlertRepository extends PagingAndSortingRepository<PostCommentAlert, Long>, JpaSpecificationExecutor<PostCommentAlert> {
    @Query(value = "update post_comment_alert p set p.confirm = true where p.post = :post and p.user = :user")
    @Modifying
    void updateAllConfirmIsTrueByPostAndUser(final Post post, final Member user);
}
