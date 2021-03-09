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

import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class CategoryTagController {

    private final DevPostService devPostService;
    private final int DEFAULT_PAGE_PER_SIZE = 20;

    @RequestMapping(path = "/{type}/{parameter}", method = RequestMethod.GET)
    public String index(@PathVariable(name = "type")final String type, @PathVariable(name = "parameter")final String parameter,
                        Model model){

        model.addAttribute("type", type);
        model.addAttribute("parameter", parameter);

        return "category-tag";
    }
}
