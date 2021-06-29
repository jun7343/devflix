package kr.devflix.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.devflix.config.QueryDslConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

import static kr.devflix.posts.QDevPost.devPost;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@Import(QueryDslConfiguration.class)
class DevPostRepositoryImplTest {

    @Autowired
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }

    @Test
    @DisplayName("dev post list 출력")
    public void devPostList() {
        queryFactory.selectFrom(devPost)
                .where()
                .fetch();
    }
}