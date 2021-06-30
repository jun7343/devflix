package kr.devflix.posts;

import java.util.List;

public interface DevPostRepositoryCustom {

    List<DevPost> findAllByCategoryOrLikeTitleAndStatusLimitOffset(String category, String title, Status status, int page, int perPage);

    Long countByCategoryOrListTitleAndStatus(String category, String title, Status status);

    Long updateViewById(Long id);
}
