package com.devflix.controller;

import com.devflix.constant.Status;
import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final int DEFAULT_SIZE_VALUE = 20;
    private final DevPostService devPostService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(@RequestParam(name = "page", required = false, defaultValue = "0")int page, Model model) {
        Page<DevPost> findList = devPostService.findAllByStatusAndPageRequest(Status.POST, page, DEFAULT_SIZE_VALUE);

        model.addAttribute("banner", findList.getContent().get(0));

        return "home";
    }
}
