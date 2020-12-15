package com.sitebase.controller;

import com.sitebase.entity.Book;
import com.sitebase.service.BookService;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.sql.DataSource;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import java.util.Set;

@RequestMapping("/")
@Controller
public class HomeController {

    @Autowired
    private BookService service;

    @Autowired
    private DataSource dataSource;

    @GetMapping("/main")
    @ResponseBody
    public String main() {
        return "Main";
    }


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        service.create();
        return "sadasd";
    }
}
