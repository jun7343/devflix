package com.sitebase.service;

import com.sitebase.entity.Book;
import com.sitebase.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class BookService {

    @Autowired
    private BookRepository repository;

    @Transactional
    public void create() {
        Book book = new Book();
        book.setName("123213123asdasdasd");
        book.setAddress("zzzz");

        System.out.println("Service Call");

        repository.save(book);
    }
}
