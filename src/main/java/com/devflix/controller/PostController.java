package com.devflix.controller;

import com.devflix.dto.PostDto;
import com.devflix.service.PostService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;

    @RequestMapping(path = "/post", method = RequestMethod.GET)
    public String index() {
        return "/post/list";
    }

    @RequestMapping(path = "/post/write", method = RequestMethod.GET)
    public String writeForm() {
        return "/post/write";
    }

    @RequestMapping(path = "/post/write", method = RequestMethod.POST)
    public String writeAction(PostDto postDto) {
        System.out.println(postDto.toString());
        return "redirect:/post";
    }
}
