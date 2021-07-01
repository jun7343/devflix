package kr.devflix.posts;

import java.util.List;

public interface DevPostRepositoryCustom {

    List<DevPost> findAllByCategoryOrTagOrLikeTitleAndStatusLimitOffset(String category, String tag, String title, Status status, int page, int perPage);

    Long countByCategoryOrTagOrListTitleAndStatus(String category, String tag, String title, Status status);

    Long updateViewById(Long id);
}
