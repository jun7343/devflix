package com.sitebase.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController {

    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public String index() {
        return "/post/index";
    }
}
