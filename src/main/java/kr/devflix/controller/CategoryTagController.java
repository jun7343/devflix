package kr.devflix.controller;

import kr.devflix.service.DevBlogService;
import kr.devflix.service.YoutubeChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CategoryTagController {

    private final DevBlogService devBlogService;
    private final YoutubeChannelService youtubeChannelService;

    @RequestMapping(path = "/category", method = RequestMethod.GET)
    public String categoryList(@RequestParam(name = "c", required = false)final String category, Model model){

        model.addAttribute("type", "category");
        model.addAttribute("parameter", category);

        return "category-tag";
    }

    @RequestMapping(path = "/tag", method = RequestMethod.GET)
    public String tagList(@RequestParam(name = "t", required = false)final String tag, Model model){

        model.addAttribute("type", "tag");
        model.addAttribute("parameter", tag);

        return "category-tag";
    }

    @RequestMapping(path = "/all-category", method = RequestMethod.GET)
    public String AllCategoryList() {
        return "all-category";
    }
}
