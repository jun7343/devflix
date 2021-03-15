package kr.devflix.repository;

import kr.devflix.constant.PostType;
import kr.devflix.entity.DevPost;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DevPostRepository extends PagingAndSortingRepository<DevPost, Long>, JpaSpecificationExecutor<DevPost> {
    DevPost findTopOneByCategoryAndPostTypeAndWriterOrderByUploadAtDesc(final String category, PostType postType, final String writer);

    DevPost findTopOneByUrl(final String url);

    Page<DevPost> findAllByCategoryOrderByUploadAtDesc(final String category, Pageable pageable);

    @Query(nativeQuery = true, value = "select * from dev_post where :tag = any(tag) and status = :status order by upload_at desc", countQuery = "select count(*) from dev_post where :tag = any(tag) and status = :status")
    Page<DevPost> findAllByTagIn(final String tag, final String status, Pageable pageable);

    DevPost findTopOneByCategoryAndPostTypeOrderByUploadAtDesc(final String category, PostType postType);

    @Query(nativeQuery = true, value = "select * from dev_post where status = :status order by random() limit 1")
    DevPost findOneByStatusOrderByRandom(final String status);
}
