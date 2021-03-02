package com.devflix.repository;

import com.devflix.constant.PostType;
import com.devflix.entity.DevPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface DevPostRepository extends PagingAndSortingRepository<DevPost, Long> {
    DevPost findTopOneByCategoryOrderByIdDesc(final String category);

    DevPost findTopOneByCategoryAndPostTypeAndWriterOrderByUploadAtDesc(final String category, PostType postType, final String writer);

    DevPost findTopOneByUrl(final String url);

    Page<DevPost> findAllByCategoryOrderByUploadAtDesc(final String category, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from dev_post where :tag = any(tag) order by upload_at desc", countQuery = "select count(*) from dev_post where :tag = any(tag)")
    Page<DevPost> findAllByTagIn(final String tag, Pageable pageable);

    DevPost findTopOneByCategoryAndPostTypeOrderByUploadAtDesc(String category, PostType postType);
}
