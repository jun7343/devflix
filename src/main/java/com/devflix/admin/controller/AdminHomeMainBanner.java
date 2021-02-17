package com.devflix.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminHomeMainBanner {

    @RequestMapping(path = "/dfa/banner/home-main", method = RequestMethod.GET)
    public String list() {
        return "admin/banner/home-main/index";
    }
}
