package kr.devflix.repository;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.devflix.constant.Status;
import kr.devflix.entity.Post;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.support.PageableExecutionUtils;

import java.util.List;

import static kr.devflix.entity.QPost.post;
import static kr.devflix.entity.QPostImage.postImage;

public class PostRepositoryImpl implements PostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public PostRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    @Override
    public Page<Post> findAllByStatusOrTitle(Status status, String title, Pageable pageable) {
        BooleanBuilder builder = new BooleanBuilder();

        builder.and(post.status.eq(status));

        if (StringUtils.isNoneBlank(title)) {
            builder.and(post.title.contains(title));
        }

        List<Post> result = queryFactory.selectFrom(post)
                .where(builder)
                .orderBy(post.createAt.desc())
                .limit(pageable.getPageSize())
                .offset(pageable.getOffset())
                .fetch();

        JPAQuery<Post> countQuery = queryFactory.selectFrom(post)
                .where(builder)
                .leftJoin(post.images, postImage);

        return PageableExecutionUtils.getPage(result, pageable, countQuery::fetchCount);
    }
}
