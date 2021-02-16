package com.sitebase.admin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AdminLoginController {

    @RequestMapping(path = "/jkcon/login", method = RequestMethod.GET)
    public String loginForm() {
        return "admin/login/index";
    }
}
