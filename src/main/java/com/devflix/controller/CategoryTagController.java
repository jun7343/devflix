package com.devflix.controller;

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

    @RequestMapping(path = "/category", method = RequestMethod.GET)
    public String categoryForm(@RequestParam(name = "c", required = false)final String category, Model model){

        model.addAttribute("type", "category");
        model.addAttribute("parameter", category);

        return "category-tag";
    }

    @RequestMapping(path = "/tag", method = RequestMethod.GET)
    public String tagForm(@RequestParam(name = "t", required = false)final String tag, Model model){

        model.addAttribute("type", "tag");
        model.addAttribute("parameter", tag);

        return "category-tag";
    }
}
