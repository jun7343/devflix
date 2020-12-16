package com.sitebase.controller;

import com.sitebase.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RequestMapping("/")
@Controller
public class HomeController {

    @Autowired
    private BookService service;


    @GetMapping
    public String home() {
        return "home";
    }

    @GetMapping("/test")
    public String test() {
        return "test";
    }
}
