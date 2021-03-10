package com.devflix.controller;

import com.devflix.constant.Status;
import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.Random;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final DevPostService devPostService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(@RequestParam(name = "page", required = false, defaultValue = "0")int page, Model model) {
        List<DevPost> findAll = devPostService.findAllByStatus(Status.POST);
        Random random = new Random();

        model.addAttribute("banner", findAll.get(random.nextInt(findAll.size())));

        return "home";
    }
}
