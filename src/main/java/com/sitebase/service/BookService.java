package com.sitebase.service;

import com.sitebase.dao.BookDao;
import com.sitebase.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookService {

    @Autowired
    private BookDao dao;

    @Transactional
    public void create() {
        Book book = new Book();
        book.setName("123213123asdasdasd");
        book.setAddress("zzzz");

        System.out.println("Service Call");

        dao.save(book);
        //dao.test();
    }

    public Book get(int id) {
        return dao.get(id);
    }
}
