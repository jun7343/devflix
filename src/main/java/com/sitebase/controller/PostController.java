package com.sitebase.controller;

import com.github.jknack.handlebars.internal.lang3.StringUtils;
import com.sitebase.constant.RoleType;
import com.sitebase.dto.PostDto;
import com.sitebase.entity.Member;
import com.sitebase.service.PostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class PostController {

    private final static Logger logger = LoggerFactory.getLogger(PostController.class.getSimpleName());
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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
    public String writeAction(PostDto postDto, @AuthenticationPrincipal Member writer, RedirectAttributes attrs) {
        System.out.println(postDto.toString());
        boolean success = false;
        String message = "";

        if (StringUtils.isBlank(postDto.getTitle())) {
            message = "제목 기입해 주세요.";
        } else if (StringUtils.isBlank(postDto.getContent())) {
            message = "내용 기입해 주세요.";
        } else {
            success = true;
        }

        if (! success) {
            attrs.addFlashAttribute("result", success);
            attrs.addFlashAttribute("resultMessage", message);

            System.out.println("You????????????????????????");

            return "redirect:/post/write";
        } else {
            postService.write(postDto, writer);
        }

        return "redirect:/post";
    }
}
