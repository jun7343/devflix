package com.sitebase.dao;

import com.sitebase.entity.Book;
import com.sitebase.repository.BookRepository;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@DataJpaTest
public class BookRepositoryTest extends TestCase {

    @Autowired
    private DataSource dataSource;

    @Autowired
    JdbcTemplate template;

    @Autowired
    BookRepository bookRepository;

    @Test
    public void di() {
        Book book = new Book();
        book.setName("junyu");
        book.setAddress("paju unjung");

        Book newBook = bookRepository.save(book);

        assertEquals(newBook.getAddress(), "paju unjung");
    }
}