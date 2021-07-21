package kr.devflix.repository;

import kr.devflix.constant.Status;
import kr.devflix.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface PostRepositoryCustom {

    Page<Post> findAllByStatusOrTitle(Status status, String title, Pageable pageable);
}
