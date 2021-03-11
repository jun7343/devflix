package kr.devflix.repository;

import kr.devflix.entity.Post;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends PagingAndSortingRepository<Post, Long>, JpaSpecificationExecutor<Post> {
}