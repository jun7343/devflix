package com.sitebase.controller;

import com.sitebase.constant.RoleType;
import com.sitebase.dto.PostDto;
import com.sitebase.entity.Member;
import com.sitebase.repository.PostRepository;
import com.sitebase.utils.Result;
import javafx.geometry.Pos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class PostController {

    private final static Logger logger = LoggerFactory.getLogger(PostController.class.getSimpleName());
    private final PostRepository postRepository;

    public PostController(PostRepository postRepository) {
        this.postRepository = postRepository;
    }


    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public String index() {
        return "/post/list";
    }

    @RequestMapping(path = "/post/write", method = RequestMethod.GET)
    @Secured(RoleType.USER)
    public String writeForm() {
        return "/post/write";
    }

    @RequestMapping(path = "/post/write", method = RequestMethod.POST)
    @Secured(RoleType.USER)
    public String writeAction(PostDto postDto, @AuthenticationPrincipal Member writer) {
        return "redirect:/post";
    }
}
