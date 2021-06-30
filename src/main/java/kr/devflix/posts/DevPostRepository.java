package kr.devflix.posts;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DevPostRepository extends JpaRepository<DevPost, Long>, DevPostRepositoryCustom, JpaSpecificationExecutor<DevPost> {

    @Query(nativeQuery = true, value = "select * from dev_post dp where :tag = any(dp.tag) and dp.status = :status order by dp.upload_at desc limit :limit offset :offset")
    List<DevPost> findAllByTagAndStatusOrderByUploadAtLimitOffset(String tag, String status, int limit, int offset);

    @Query(nativeQuery = true, value = "select count(*) from dev_post dp where :tag = any(dp.tag) and dp.status = :status")
    Long countByTagAndStatus(String tag, String status);

    DevPost findTopOneByUrl(final String url);

    @Query(nativeQuery = true, value = "select * from dev_post where :tag = any(tag) and status = :status order by upload_at desc", countQuery = "select count(*) from dev_post where :tag = any(tag) and status = :status")
    Page<DevPost> findAllByTagIn(final String tag, final String status, Pageable pageable);

    DevPost findTopOneByCategoryAndPostTypeOrderByUploadAtDesc(final String category, PostType postType);

    @Query(nativeQuery = true, value = "select * from dev_post where status = :status order by random() limit 1")
    DevPost findOneByStatusOrderByRandom(final String status);

    @Query(value = "update dev_post d set d.status = :status where d.id in (:idList)")
    @Modifying
    void updateStatusByIdList(Status status, List<Long> idList);
}
