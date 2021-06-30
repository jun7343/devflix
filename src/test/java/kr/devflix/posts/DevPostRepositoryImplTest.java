package kr.devflix.posts;

import com.querydsl.jpa.impl.JPAQueryFactory;
import kr.devflix.config.QueryDslConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("local")
@Import(QueryDslConfiguration.class)
class DevPostRepositoryImplTest {

    @Autowired
    private EntityManager entityManager;

    private JPAQueryFactory queryFactory;
    
    @Autowired
    private DevPostRepository devPostRepository;

    @BeforeEach
    public void init() {
        queryFactory = new JPAQueryFactory(entityManager);
    }
}