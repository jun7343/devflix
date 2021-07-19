package kr.devflix.repository;

import com.querydsl.core.QueryResults;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.devflix.constant.Status;
import kr.devflix.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import static kr.devflix.entity.QPost.post;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Post> findAllByStatusOrTitle(Status status, String title, Pageable pageable) {
        QueryResults<Post> results = queryFactory.selectFrom(post)
                .distinct()
                .where(post.status.eq(status))
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetchResults();


        return new PageImpl<>(results.getResults(), pageable, results.getTotal());
    }
}
