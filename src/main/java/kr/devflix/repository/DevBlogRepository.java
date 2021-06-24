package kr.devflix.repository;

import kr.devflix.posts.Status;
import kr.devflix.entity.DevBlog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevBlogRepository extends JpaRepository<DevBlog, Long> {
    List<DevBlog> findAllByStatusOrderByCreateAtDesc(Status status);
}
