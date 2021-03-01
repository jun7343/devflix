package com.devflix.controller;

import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class CategoryTagController {

    private final DevPostService devPostService;
    private final int DEFAULT_PAGE_PER_SIZE = 20;

    @RequestMapping(path = "/{type}/{parameter}", method = RequestMethod.GET)
    public String index(@PathVariable(name = "type")final String type, @PathVariable(name = "parameter")final String parameter,
                        @RequestParam(name = "page", defaultValue = "0")int page, Model model){
        if (StringUtils.equals(type, "category")) {
            Page<DevPost> findList = devPostService.findAllByCategoryOrderByUploadAt(parameter, page, DEFAULT_PAGE_PER_SIZE);

            model.addAttribute("list", findList);
        } else if (StringUtils.equals(type, "tag")) {
            Page<DevPost> findList = devPostService.findAllByTagOrderByUploadAt(parameter, page, DEFAULT_PAGE_PER_SIZE);

            model.addAttribute("list", findList);
        }

        model.addAttribute("type", type);
        model.addAttribute("parameter", parameter);

        return "category-tag";
    }
}
