package com.sitebase.dao;

import com.sitebase.entity.Book;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Repository
public class BookDao {

    @Autowired
    private SessionFactory sessionFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Autowired
    private BookRepository repository;


    public void save(Book book) {
        System.out.println("Dao Call");
        System.out.println(book.toString());
        Book z = repository.save(book);

    }

    public void test() {
        try(Connection connection = dataSource.getConnection()){
            System.out.println(connection);
            String URL = connection.getMetaData().getURL();
            System.out.println(URL);
            String User = connection.getMetaData().getUserName();
            System.out.println(User);

            Statement statement = connection.createStatement();
            String sql = "CREATE TABLE ACCOUNT(" +
                    "ID INTEGER NOT NULL," +
                    "NAME VARCHAR(255)," +
                    "PRIMARY KEY(ID))";
            statement.executeUpdate(sql);
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        jdbcTemplate.execute("INSERT INTO ACCOUNT VALUES(1, 'saelobi')");
    }

    public Book get(int id) {
        return sessionFactory.getCurrentSession().get(Book.class, id);
    }
}
