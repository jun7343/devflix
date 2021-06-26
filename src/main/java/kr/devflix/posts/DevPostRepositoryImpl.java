package kr.devflix.posts;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import static kr.devflix.posts.QDevPost.devPost;

@RequiredArgsConstructor
public class DevPostRepositoryImpl implements DevPostRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<DevPost> findAllByCategoryOrTagOrLikeTitleAndStatus(final String category,
                                                                final String tag,
                                                                final String title,
                                                                Status status,
                                                                final Integer page,
                                                                final Integer resultMax) {
        List<BooleanExpression> list = new ArrayList<>();

        if (StringUtils.isNoneBlank(category)) {
            list.add(devPost.category.eq(category));
        }

        if (StringUtils.isNoneBlank(tag)) {
            list.add(devPost.tag.any().eq(tag));
        }

        if (StringUtils.isNoneBlank(title)) {
            list.add(devPost.title.like("%" + title + "%"));
        }

        return queryFactory.selectFrom(devPost)
                .where(list.toArray(new BooleanExpression[0]))
                .orderBy(devPost.uploadAt.desc())
                .limit(resultMax)
                .offset(page * resultMax)
                .fetch();
    }

    @Override
    public Long countByCategoryOrTagOrListTitleAndStatus(final String category,
                                                            final String tag,
                                                            final String title,
                                                            Status status) {
        List<BooleanExpression> list = new ArrayList<>();

        if (StringUtils.isNoneBlank(category)) {
            list.add(devPost.category.eq(category));
        }

        if (StringUtils.isNoneBlank(tag)) {
            list.add(devPost.tag.any().eq(tag));
        }

        if (StringUtils.isNoneBlank(title)) {
            list.add(devPost.title.like("%" + title + "%"));
        }

        return queryFactory.selectFrom(devPost)
                .where(list.toArray(new BooleanExpression[0]))
                .fetchCount();
    }
}
