package com.devflix.controller;

import com.devflix.dto.PostDto;
import com.devflix.entity.Member;
import com.devflix.entity.Post;
import com.devflix.service.PostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
    public String writeAction(@RequestParam(name = "title", required = false)final String title, @RequestParam(name = "post-url", required = false) List<String> devPostURL,
                              @RequestParam(name = "path-base", required = false)final String pathBase, @RequestParam(name = "images", required = false)List<String> images,
                              @RequestParam(name = "content", required = false)final String content, @AuthenticationPrincipal Member writer,
                              RedirectAttributes attrs) {
        if (StringUtils.isBlank(title)) {
            attrs.addFlashAttribute("errorMessage", "제목 기입해 주세요.");

            return "redirect:/post/write";
        }

        PostDto dto = PostDto.builder()
                .title(title)
                .content(content)
                .writer(writer)
                .pathBase(pathBase)
                .images(images)
                .devPostUrl(devPostURL)
                .build();

        postService.createPost(dto);

        return "redirect:/post";
    }

    @RequestMapping(path = "/post/read/{id}", method = RequestMethod.GET)
    public String readForm(@PathVariable(name = "id")long id, Model model) {
        Post post = postService.get(id);

        model.addAttribute("item", post);

        return "/post/read";
    }
}
