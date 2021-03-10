package com.devflix.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequiredArgsConstructor
public class CategoryTagController {

    @RequestMapping(path = "/{type}/{parameter}", method = RequestMethod.GET)
    public String index(@PathVariable(name = "type")final String type, @PathVariable(name = "parameter")final String parameter,
                        Model model){

        model.addAttribute("type", type);
        model.addAttribute("parameter", parameter);

        return "category-tag";
    }
}
