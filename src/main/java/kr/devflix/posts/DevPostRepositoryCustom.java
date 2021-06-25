package kr.devflix.posts;

import java.util.List;

public interface DevPostRepositoryCustom {

    List<DevPost> findAllByCategoryOrTagOrLikeTitleLimit(String category, String tag, String title, Status status, Integer page, Integer pageSize);
}
