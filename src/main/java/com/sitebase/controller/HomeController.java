package com.sitebase.controller;

import com.sitebase.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.DispatcherServlet;

@RequestMapping("/")
@Controller
public class HomeController {

    @Autowired
    private BookService service;


    @GetMapping
    public String home(DispatcherServlet servlet) {

        return "home";
    }


    @GetMapping("/test")
    @ResponseBody
    public String test() {
        return "sadasd";
    }
}
