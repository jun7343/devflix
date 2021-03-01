package com.devflix.repository;

import com.devflix.constant.PostType;
import com.devflix.entity.DevPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevPostRepository extends JpaRepository<DevPost, Long> {
    DevPost findTopOneByCategoryOrderByIdDesc(final String category);

    DevPost findTopOneByCategoryAndPostTypeAndWriterOrderByUploadAtDesc(final String category, PostType postType, final String writer);

    DevPost findTopOneByUrl(final String url);
}
