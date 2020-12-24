package com.sitebase.controller;

import com.sitebase.command.PostCommand;
import com.sitebase.entity.Member;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController {

    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public String index() {
        return "/post/index";
    }

    @RequestMapping(path = "/post/write", method = RequestMethod.GET)
    public String writeForm() {
        return "/post/write";
    }

    @RequestMapping(path = "/post/write", method = RequestMethod.POST)
    public String writeAction(PostCommand postCommand, @AuthenticationPrincipal Member writer) {
        System.out.println(postCommand.toString());
        System.out.println(writer.getName());


        return "redirect:/post";
    }
}
