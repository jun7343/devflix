package com.devflix.controller;

import com.devflix.entity.DevPost;
import com.devflix.service.DevPostService;
import com.google.common.collect.ImmutableMap;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final int DEFAULT_SIZE_VALUE = 20;
    private final DevPostService devPostService;

    @RequestMapping(path = "/", method = RequestMethod.GET)
    public String index(@RequestParam(name = "page", required = false, defaultValue = "0")int page, Model model) {
        Page<DevPost> pageResult = devPostService.findAllByPageRequest(page, DEFAULT_SIZE_VALUE);
        List<Integer> pageNumList = new ArrayList<>();

        model.addAttribute("list", pageResult.getContent());
        model.addAttribute("banner", pageResult.getContent().get(0));
        model.addAttribute("previousPage", pageResult.getNumber() / 5 != 0);

        if (pageResult.getNumber() / 5 != 0 && ((pageResult.getNumber() / 5) * 5 - 1) > 0) {
            model.addAttribute("previousPageNum", (pageResult.getNumber() / 5) * 5 - 1);
        }

        model.addAttribute("nextPage", (pageResult.getNumber() / 5) * 5 + 6 <= pageResult.getTotalPages());

        if ((pageResult.getNumber() / 5) * 5 + 6 <= pageResult.getTotalPages()) {
            model.addAttribute("nextPageNum", (pageResult.getNumber() / 5 + 1) * 5);
        }

        int start = (pageResult.getNumber() / 5) * 5 + 1;
        int end = Math.min((pageResult.getNumber() / 5 + 1) * 5, pageResult.getTotalPages());

        for (int i = start; i <= end; i++) {
            pageNumList.add(i);
        }

        model.addAttribute("pageNumList", pageNumList);
        model.addAttribute("currentPageNum", pageResult.getNumber() + 1);

        return "home";
    }

    @RequestMapping(path = "/a/view-count", method = RequestMethod.POST)
    public void actionViewCountUpdate(@RequestParam(name = "url", required = false)final String url) {
        if (StringUtils.isBlank(url)) {
            return;
        }
        devPostService.updateViewCount(url);
    }
}
