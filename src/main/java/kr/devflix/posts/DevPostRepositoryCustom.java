package kr.devflix.posts;

import java.util.List;

public interface DevPostRepositoryCustom {

    List<DevPost> findAllByCategoryOrTagOrLikeTitleAndStatus(String category, String tag, String title, Status status, Integer page, Integer resultMax);

    Long countByCategoryOrTagOrListTitleAndStatus(String category, String tag, String title, Status status);
}
